package com.itrail.klinikreact.services.model;

import java.security.SecureRandom;
import java.util.Base64;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.itrail.klinikreact.models.User;
import com.itrail.klinikreact.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        /**User user = new User();
                                user.setLogin("testone");
                                user.setPassword(passwordEncoder.encode( secret + "testone" + salt ));
                                user.setRole( "1");
                                user.setEmail( "Admintttt@mail.com");
                                user.setSalt( salt );
                                user.setStatus( false );
            userRepository.save(  user ).subscribe( r -> log.info( r.toString() ));*/
            userRepository.findById(33L ).subscribe( us -> log.info( us.toString()));
            log.info( "init main user");
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
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,4}$";
        return email != null && email.matches(emailRegex);
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
                                        user.setRole("test");
                                        user.setPassword(passwordEncoder.encode(secret + user.getPassword() + salt));
                                        user.setSalt(salt);
                                        return userRepository.save(user);
                                        }));
                                }));
    }
}
