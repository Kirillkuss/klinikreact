package com.itrail.klinikreact.controllers.login;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.itrail.klinikreact.rest.login.IAuthentication;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class AuthenticationController implements IAuthentication {

    @PreAuthorize("hasAnyRole('0', '1')")
    @Override
    public Mono<String> path() {
        return Mono.just( "redirect:/index.html");
    }

    @PreAuthorize("hasAnyRole('0', '1')")
    public Mono<String> index() {
        return Mono.just( "redirect:/index.html");
    }

    @Override
    public Mono<String> login() {
        return Mono.just( "login");
    }

    @Override
    public Mono<String> error() {
        return Mono.just( "error");
    }

    public Mono<String> changePassword() {
        return Mono.just( "change-password");
    }

    @Override
    public Mono<String> clearErrorMessage(HttpServletRequest request) {
        request.getSession().removeAttribute("error");
        return Mono.just("redirect:/login"); 
    }

    @Override
    public Mono<String> requestPasswordChange( String user, HttpServletRequest request ) {
       /**  try{
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setLogin( user );
            emailRequest.setSubject("Изменение пароля");
            emailRequest.setBody("Ваш пароль был изменен, используйте этот: ");
            emailService.sendSimpleEmailMessage(emailRequest);;
        }catch( Exception ex ){
            redirectAttributes.addFlashAttribute("error", ex.getMessage() );
        }*/
        return Mono.just("redirect:/change-password"); 
    }

}
