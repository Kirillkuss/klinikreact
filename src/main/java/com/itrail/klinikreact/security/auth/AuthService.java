package com.itrail.klinikreact.security.auth;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import com.itrail.klinikreact.models.User;
import com.itrail.klinikreact.models.UserBlocking;
import com.itrail.klinikreact.repositories.UserBlockingRepository;
import com.itrail.klinikreact.repositories.UserRepository;
import com.itrail.klinikreact.services.model.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final UserBlockingRepository userBlockingRepository;

    private final String messageError      = "Слишком много неудачных попыток входа, попробуйте позже!";
    private final String messageErrorCount = "Неправильное имя пользователя или пароль, количество попыток: ";

    private final int maxAttempts = 1; 
    public final Map<String, Integer> failedAttempts = new HashMap<>();

    public Mono<User> checkUser(String login, String password) {
        return getCurrentAttempts(login)
                .flatMap(currentAttempts -> userRepository.findUserByLogin(login)
                        .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid username or password!")))
                        .flatMap(user -> validateUserStatus(user)
                                .then(validateUserCredentials(user, password, currentAttempts))
                                .then(Mono.just(user))
                        )
                );
    }

    private Mono<Integer> getCurrentAttempts(String login) {
        return Mono.justOrEmpty(failedAttempts.get(login))
                .defaultIfEmpty(0)
                .map(currentAttempts -> {
                    validateAttempts(currentAttempts);
                    return currentAttempts;
                });
    }

    private Mono<Void> validateAttempts(int currentAttempts) {
        if (currentAttempts >= maxAttempts) {
            return Mono.error(new BadCredentialsException("Maximum number of login attempts exceeded!"));
        }
        return Mono.empty();
    }

    private Mono<Void> validateUserStatus(User user) {
        if (user.getStatus()) {
            return Mono.error(new BadCredentialsException("User blocked!"));
        }
        return Mono.empty();
    }

    private Mono<Void> validateUserCredentials(User user, String password, int currentAttempts) {
        return userService.checkUserPassword(password, user.getSalt(), user.getPassword())
                .flatMap(isValid -> {
                    if (!isValid) {
                        return handleFailedAttempt(user.getLogin(), currentAttempts );
                    }
                    return Mono.empty();
                });
    }

    private Mono<Void> handleFailedAttempt(String login, int currentAttempts) {
        currentAttempts++;
        failedAttempts.put(login, currentAttempts);
        int remainingAttempts = maxAttempts - currentAttempts;
        if (remainingAttempts == 0) {
            failedAttempts.remove(login);
            return addBlocking(login);
        } else {
            return Mono.error(new BadCredentialsException( "Wrong password! Attempts left: " + remainingAttempts));
        }
    }

    public Mono<Void> addBlocking(String login) {
        return userRepository.findUserByLogin(login).flatMap( user ->{
            UserBlocking userBlocking = new UserBlocking();
            userBlocking.setDateBlock(LocalDateTime.now());
            userBlocking.setDatePlanUnblock(LocalDateTime.now().plusMinutes( 1));
            userBlocking.setUserId( user.getId());
            userBlocking.setStatusBlock( 1 );
            userBlocking.setStatus( true );
            return userBlockingRepository.save(userBlocking)
                .flatMap( userblock ->{
                    log.info( "user blocking with login:" + login);   
                    return userService.blockUser(login);
                });
        });     
    } 

    @Scheduled(fixedRate = 3000)
    //@Scheduled(initialDelay = 5000, fixedRate = 60000)
	public void unblockUser() {
        userBlockingRepository.unblockUserBlocking()
            .then( updateUser().flatMap( t ->{
                return Mono.empty();
            }).then())
            .then( userBlockingRepository.unblockUserBlockingStatus() )
                .subscribe();
    }

    private Flux<Object> updateUser(){
        return  userBlockingRepository.getBlockStatus( LocalDateTime.now().minusMinutes(150), LocalDateTime.now() )
            .flatMap( t-> {
                log.info( "id >>> " + t );
                return userRepository.findById( t )
                    .flatMap( user -> {
                        log.info( "unblock user with login >>>>" + user.getLogin() );
                        user.setStatus( false );
                        return userRepository.save( user );
                    });
            });
    }        
}
