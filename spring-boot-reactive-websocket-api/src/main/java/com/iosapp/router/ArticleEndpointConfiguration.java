package com.iosapp.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.iosapp.handler.ArticleHandler;

@Configuration
public class ArticleEndpointConfiguration {
	
    @Bean
    RouterFunction<ServerResponse> routes(ArticleHandler handler) { 
        return RouterFunctions
        		.route(RequestPredicates.POST("/article")
        		.and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::create);
    }
}
