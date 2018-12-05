package com.iosapp.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.iosapp.model.Article;
import com.iosapp.repository.ArticleRepository;

import reactor.core.publisher.Flux;

@Component
public class SampleDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	ArticleRepository articleRepository;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		articleRepository.deleteAll() 
        .thenMany(
            Flux
                .just("1st Article", "2nd Article")
                .map(title -> new Article(title, "New Article")) 
                .flatMap(articleRepository::save) 
        )
        .thenMany(articleRepository.findAll()) 
        .subscribe(article -> System.out.println("Saved article: " + article.getTitle())); 
				
	}

	
}
