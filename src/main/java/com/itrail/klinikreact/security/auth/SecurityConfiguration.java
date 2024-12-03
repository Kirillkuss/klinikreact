package com.itrail.klinikreact.security.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import com.itrail.klinikreact.security.handler.KlinikaReactAuthenticationFailureHandler;
import com.itrail.klinikreact.security.handler.KlinikaReactAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final KlinikaReactAuthenticationFailureHandler klinikaReactAuthenticationFailureHandler;
    private final KlinikaReactAuthenticationSuccessHandler klinikaReactAuthenticationSuccessHandler;

   /** @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange(exchanges -> exchanges
            .pathMatchers("/login", "/change-password", "/logout", "/icon/").permitAll()
            .pathMatchers("/webjars/swagger-ui/index.html", "/api", "/", "/klinikreact", "/index").authenticated()
            .anyExchange().authenticated())
            .formLogin(formLogin -> formLogin
                .authenticationSuccessHandler( klinikaReactAuthenticationSuccessHandler )
                .authenticationFailureHandler( klinikaReactAuthenticationFailureHandler )
                .loginPage("/login"))
            .csrf(csrf -> csrf.disable())
            .build();
    }*/

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange(exchanges -> exchanges
                .pathMatchers("/login", "/change-password", "/logout", "/icon/").permitAll()
                .pathMatchers("/react/webjars/swagger-ui/index.html").hasRole("2") 
                .pathMatchers("/react/api/", "/react/", "/react/klinikreact", "/react/index", "/react/").hasAnyRole("0", "1") 
                .anyExchange().authenticated())
                .formLogin(formLogin -> formLogin
                    .authenticationSuccessHandler(klinikaReactAuthenticationSuccessHandler)
                    .authenticationFailureHandler(klinikaReactAuthenticationFailureHandler)
                    .loginPage("/login"))    
                .logout(logout -> 
                    logout.logoutUrl("/logout"))
                .csrf(csrf -> csrf.disable())
                .build();
    }

    /**@Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/login", "/change-password", "/logout", "/icon/").permitAll()
                .pathMatchers("/react/webjars/swagger-ui/index.html").hasAnyRole("ROLE_2")
                .pathMatchers("/react/webjars/swagger-ui/index.html", "/react/api/**", "/react/", "/react/klinikreact", "/react/index", "/react/**").hasAnyRole("ROLE_1", "ROLE_0")
                .anyExchange().authenticated()
            .and()
            .formLogin(form -> form
                .loginPage("/login")
                .authenticationSuccessHandler( klinikaReactAuthenticationSuccessHandler)
                .authenticationFailureHandler( klinikaReactAuthenticationFailureHandler)
            )
            .logout(logout -> logout.logoutUrl("/logout"))
            .csrf(csrf -> csrf.disable())
            .build();
            }
   */
    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

} 
