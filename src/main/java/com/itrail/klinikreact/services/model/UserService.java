package com.itrail.klinikreact.services.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.itrail.klinikreact.aspect.ExecuteTimeLog;
import com.itrail.klinikreact.models.User;
import com.itrail.klinikreact.repositories.UserRepository;
import com.itrail.klinikreact.request.UserRequest;
import com.itrail.klinikreact.response.UserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${spring.security.secret}")
    private String secret;
 
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    protected void init(){
        String salt = generateSalt();
        User user = new User();
                                user.setLogin("testone");
                               // user.setPassword(passwordEncoder.encode( secret + "dkjfRk4$451sfdf" + salt ));
                                user.setPassword("dkjfRk4$451sfdf");
                                user.setRole("1");
                                user.setEmail( "Admin@mail.com");
                                user.setSalt( salt );
                                user.setStatus( false );
            //ddUserThree(  user ).subscribe( r -> log.info( r.toString() ));
            //userRepository.findById(33L ).subscribe( us -> log.info( us.toString()));
            //log.info( "init main user");
    }

    public Flux<UserResponse> getUsers() {
        return userRepository.findAll()
            .map(user -> {
                return new UserResponse(user.getLogin(), user.getEmail(), user.getRole(), user.getStatus());
            });
    }

    /**
     * Генерация соли
     * @return String
     */
    private String generateSalt() {
        byte[] saltBytes = new byte[32];
        new SecureRandom().nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
    /**
     * Поиск по логину
     * @param username
     * @return Mono<User>
     */
    public Mono<User> findByLogin( String username ){
        return userRepository.findUserByLogin( username );
        
    }

    public Mono<User> findByEmail( String email ){
        return userRepository.findUserByEmail( email )
            .switchIfEmpty(Mono.error( new NoSuchElementException("Not Found user by email ")));
    }
    /**
     * Проверка паролья при авторизации
     * @param rawPassword - декодированный пароль
     * @param salt - соль
     * @param encodedPassword - закодированный пароль
     * @return boolean
     */
    public boolean checkUserPassword(String rawPassword, String salt, String encodedPassword) {
        return passwordEncoder.matches( secret + rawPassword + salt, encodedPassword);
    }
    /**
     * Проверка размера и кол-во символов для пароля
     * @param password
     * @return boolean
     */
    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-ZА-Я])(?=.*[a-zа-яё])(?=.*\\d)(?=.*[@#$^&+=!№:?:%*(;_)}{])[A-Za-zА-Яа-яёЁ0-9@#$^&+=!№:?:%*(;_)}{]{8,}$");

    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,4}$";
        return email != null && email.matches(emailRegex);
    }
    /**
     * Проверка корректности при создании нового пользователя
     * @param userRequest - входной запрос
     * @return Mono Void
     */
    private Mono<Void> checkCorrectUser( UserRequest userRequest ){
        if ( !isValidPassword( userRequest.getPassword() )){
            return Mono.error( new IllegalArgumentException("Password does not meet complexity requirements!"));
        }
        if (!isValidEmail(userRequest.getEmail())) {
            return Mono.error(new IllegalArgumentException("Invalid email format!"));
        }
        return userRepository.findUserByLogin( userRequest.getLogin() )
            .flatMap( login -> { return Mono.error(new IllegalArgumentException("Not unique username, please specify another!")); })
                .then( userRepository.findUserByEmail( userRequest.getEmail() )
                .flatMap( email -> { return Mono.error(new IllegalArgumentException("Email already in use, please specify another!")); }));
    }
    /**
     * Слздание нового пользователя
     * @param userRequest - входной запрос
     * @return Mono UserResponse 
     */
    @ExecuteTimeLog( operation = "addUserUserRequest" )
    public Mono<UserResponse> addUserUserRequest( UserRequest userRequest ){
        String salt = generateSalt();
        return checkCorrectUser( userRequest )
            .then( userRepository.save( new User( null, 
                                                  userRequest.getLogin(),
                                                  passwordEncoder.encode( secret + userRequest.getPassword() + salt ),
                                                  userRequest.getRole(),
                                                  userRequest.getEmail(),
                                                  salt,
                                                  false ))).flatMap( saveUser ->{
                                                    return Mono.just( new UserResponse( saveUser.getLogin(),
                                                                                        saveUser.getEmail(),
                                                                                        saveUser.getRole(),
                                                                                        saveUser.getStatus() ));
                                                  });


    }

    /**
     * Add user
     * @param user - user
     * @return Mono<User>
     */
    public Mono<Object> addUser(User user) {
        if (!isValidPassword( user.getPassword())){
            return Mono.error( new IllegalArgumentException("Password does not meet complexity requirements!"));
        } 
        if (!isValidEmail(user.getEmail())) {
            return Mono.error(new IllegalArgumentException("Invalid email format!"));
        }
        return userRepository.findUserByLogin( user.getLogin() )
                             .flatMap( existingUser -> {
                                return Mono.error(new IllegalArgumentException("Not unique username, please specify another!"));
                                })
                             .switchIfEmpty(Mono.defer(() -> {
                                return userRepository.findUserByEmail( user.getEmail() )
                                    .flatMap(existingEmailUser -> {
                                        return Mono.error(new IllegalArgumentException("Email already in use, please specify another!"));
                                    })
                                    .switchIfEmpty(Mono.defer(() -> {
                                        String salt = generateSalt();
                                        user.setPassword( passwordEncoder.encode( secret + user.getPassword() + salt ));
                                        user.setSalt(salt);
                                        user.setStatus( false );
                                        return userRepository.save(user);
                                        }).flatMap( userSave ->{
                                            System.out.println( "userSave >>" + userSave );
                                            return Mono.just( new UserResponse( userSave.getLogin(),
                                                                                userSave.getEmail(),
                                                                                userSave.getRole(),
                                                                                userSave.getStatus() ));
                                        }));
                                }));
    }





}
