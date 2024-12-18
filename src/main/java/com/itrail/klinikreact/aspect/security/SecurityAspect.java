package com.itrail.klinikreact.aspect.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Aspect
@Component
public class SecurityAspect {
    /**
     * Для Mono
     * @param joinPoint
     * @param securedControlMono
     * @return
     */
    @Around("@annotation(securedControlMono)")
    public Mono<?> checkCustomSecurityMono(ProceedingJoinPoint joinPoint, SecuredControlMono securedControlMono) {
        return ReactiveSecurityContextHolder.getContext()
            .flatMap(securityContext -> {
                Authentication authentication = securityContext.getAuthentication();
                if (authentication == null || !authentication.isAuthenticated()) {
                    return Mono.error(new SecurityException("User is not authenticated"));
                }
                String[] requiredRoles = securedControlMono.roles();
                boolean hasRole = false;
                for (String role : requiredRoles) {
                    if (authentication.getAuthorities()
                            .stream()
                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role))) {
                        hasRole = true;
                        break;
                    }
                }

                if (!hasRole) {
                    return Mono.error(new SecurityException("The user does not have access to this resource"));
                }
                return Mono.defer(() -> {
                    try {
                        return (Mono<?>)joinPoint.proceed();
                    } catch (Throwable throwable) {
                        return Mono.error(new RuntimeException("Method execution failed", throwable));
                    }
                });

            });
    }
    
    /**
     * Для Flux
     * @param joinPoint
     * @param securedControlFlux
     * @return
     */
    @Around("@annotation(securedControlFlux)")
    public Flux<?> checkCustomSecurityFlux(ProceedingJoinPoint joinPoint, SecuredControlFlux securedControlFlux) {
        return ReactiveSecurityContextHolder.getContext()
            .flatMapMany(securityContext -> {
                Authentication authentication = securityContext.getAuthentication();
                if (authentication == null || !authentication.isAuthenticated()) {
                    return Flux.error(new SecurityException("User is not authenticated"));
                }

                String[] requiredRoles = securedControlFlux.roles();
                boolean hasRole = false;
                for (String role : requiredRoles) {
                    if (authentication.getAuthorities()
                            .stream()
                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role))) {
                        hasRole = true;
                        break;
                    }
                }

                if (!hasRole) {
                    return Flux.error(new SecurityException("The user does not have access to this resource"));
                }

                return Flux.defer(() -> {
                    try {
                        return (Flux<?>) joinPoint.proceed();
                    } catch (Throwable throwable) {
                        return Flux.error(new RuntimeException("Method execution failed", throwable));
                    }
                });
            });
    }
}
