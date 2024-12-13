package com.itrail.klinikreact.services.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.itrail.klinikreact.services.model.UserService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@PropertySource(value = {"classpath:email.properties"})
public class EmailService {

    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender javaMailSender;
    private final UserService userService;

    public Mono<String> sendNewPasswordToMail( String user ){
        return userService.findUserByLoginOrByMail( user )
            .flatMap( entity -> {
                return userService.generateNewPasswordForEmail( user )
                .flatMap( password -> {
                    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                                      simpleMailMessage.setTo( entity.getEmail() );
                                      simpleMailMessage.setSubject( "Изменение пароля");
                                      simpleMailMessage.setText( "Ваш пароль был изменен, используйте этот: " + password );
                                      simpleMailMessage.setFrom( username );
                    javaMailSender.send( simpleMailMessage );
                    return Mono.just( "success");
                });

            });
        
    }

    
}
