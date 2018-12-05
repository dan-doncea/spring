package com.iosapp.handler;

import java.net.URI;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.iosapp.model.Article;
import com.iosapp.repository.ArticleRepository;
import com.iosapp.utilities.ArticleCreatedEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ArticleHandler {

	@Autowired
	ArticleRepository articleRepository;
	
	@Autowired
	ApplicationEventPublisher publisher;
	
    public Mono<ServerResponse> create(ServerRequest request) {
        Flux<Article> flux = request
            .bodyToFlux(Article.class)
            .flatMap(toWrite -> articleRepository.save(new Article(toWrite.getTitle(), toWrite.getContent()))
            .doOnSuccess(article -> this.publisher.publishEvent(new ArticleCreatedEvent(article))));
        
        return defaultWriteResponse(flux);
    }
   
    // WARNING: This method may be redundant !
    private static Mono<ServerResponse> defaultWriteResponse(Publisher<Article> articles) {
        return Mono
            .from(articles)
            .flatMap(p -> ServerResponse
                .created(URI.create("/profiles/" + p.getId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .build()
            );
    }
}
