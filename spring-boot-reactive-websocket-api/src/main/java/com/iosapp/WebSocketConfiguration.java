package com.iosapp;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iosapp.utilities.ArticleCreatedEvent;

import reactor.core.publisher.Flux;

@Configuration
public class WebSocketConfiguration {

    @Bean // 1 - This bean will bridge our events to the reactive websocket stream
    Executor executor() {
        return Executors.newSingleThreadExecutor();
    }
 
    @Bean //2 - The HandlerMapping object tells Spring about what handlers are available and what their URLs should be.
    HandlerMapping handlerMapping(WebSocketHandler wsh) {
        return new SimpleUrlHandlerMapping() {
            {          
              setUrlMap(Collections.singletonMap("/ws/articles", wsh)); // 3 - this tells Spring WebFlux to map our WebSocketHandler to a particular URI, /ws/articles
              setOrder(10);
            }
        };
    }
   
    @Bean // 4- WebSocketHandlerAdapter bridges the websocket support in Spring WebFlux with Spring WebFlux’s general routing machinery
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean // 5 - ObjectMapper will be used to turn some objects into JSON to send back to the client
    	  // 6 - ProfileCreatedEventPublisher will consume our application events and forwards them to the reactive websocket stream
    WebSocketHandler webSocketHandler(ObjectMapper objectMapper, ArticleCreatedEventPublisher eventPublisher) {
    	
        Flux<ArticleCreatedEvent> publish = Flux.create(eventPublisher).share(); // 7 - Share() will broadcast all the events to multiple subscribers

        return session -> {
            Flux<WebSocketMessage> messageFlux = publish
                .map(evt -> {
                    try {                       
                        return objectMapper.writeValueAsString(evt.getSource());
                    }
                    catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(str -> {
                    System.out.println("sending " + str);
                    return session.textMessage(str);
                });

            return session.send(messageFlux); 
        };
    }


}
