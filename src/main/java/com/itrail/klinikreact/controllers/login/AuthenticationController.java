package com.itrail.klinikreact.controllers.login;

import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import com.itrail.klinikreact.response.BaseError;
import com.itrail.klinikreact.rest.login.IAuthentication;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class AuthenticationController implements IAuthentication {

    @ExceptionHandler(Throwable.class)
    public Mono<BaseError> errBaseResponse( Throwable ex ){
        log.info("Throwable");
        return Mono.just(  new BaseError( 500, ex.getMessage() ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public Mono<BaseError> errBaseResponse( NoSuchElementException ex ){
        log.info("NoSuchElementException");
        return Mono.just( new BaseError( 400, ex.getMessage() ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<BaseError> errBaseResponse( IllegalArgumentException ex ){
        log.info("IllegalArgumentException");
        return Mono.just( new BaseError( 404, ex.getMessage() ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Mono<BaseError> errBaseResponse( BadCredentialsException ex ){
        log.info("BadCredentialsException");
        return Mono.just( new BaseError( 404, ex.getMessage() ));
    }

    @GetMapping(value = "/login")
    public Mono<String> login() {
        return Mono.just( "login");
    }
    /**
     * access to ui for users with role 0 and 1
     */
    @PreAuthorize("hasRole('0')")
    public Mono<String> index() {
        return Mono.just( "redirect:/index.html");
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
