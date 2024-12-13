package com.itrail.klinikreact.config.server;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ContextPathCompositeHandler;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import lombok.extern.slf4j.Slf4j;
/**
 * Конфигурация для Netty ( Tomcat )
 */
@Slf4j
@Configuration
public class NettyServerConfig {

    @Value("${server.port}")
    private int port;
    
    @Value("${server.servlet.context-path}")
    private String contextPath;
    /**
     * настройка сервера для Netty
     * @return NettyReactiveWebServerFactory
     */
    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory() {
        return new NettyReactiveWebServerFactory() {
            @Override
            public WebServer getWebServer(HttpHandler httpHandler) {
                Map<String, HttpHandler> handlerMap = new HashMap<>();
                handlerMap.put(contextPath, httpHandler);
                handlerMap.put( "/", httpHandler); 
                return super.getWebServer(new ContextPathCompositeHandler(handlerMap));
            }
        };
    }

    public NettyServerCustomizer portCustomizer() {
        return httpServer -> httpServer.port(port);
    }
    /**
     * настройка сервера для Tomcat
     * @return TomcatReactiveWebServerFactory
     */
    /**@Bean
    public ReactiveWebServerFactory reactiveWebServerFactory() {
        return new TomcatReactiveWebServerFactory() {
            @Override
            public WebServer getWebServer(HttpHandler httpHandler) {
                Map<String, HttpHandler> handlerMap = new HashMap<>();
                handlerMap.put(contextPath, httpHandler);
                handlerMap.put( "/", httpHandler); 
                return super.getWebServer(new ContextPathCompositeHandler(handlerMap));
            }
        };
    }

    public NettyServerCustomizer portCustomizer() {
        return httpServer -> httpServer.port(port);
    }*/

    @Bean
    public WebFilter redirectFilter() {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            if (exchange.getRequest().getURI().getPath().equals("/login")) {
                exchange.getResponse().setStatusCode(HttpStatus.FOUND);
                exchange.getResponse().getHeaders().setLocation(URI.create("/react/login"));
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };
    }

}

