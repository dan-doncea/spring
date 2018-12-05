package com.iosapp.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.iosapp.model.Article;

public interface ArticleRepository extends ReactiveMongoRepository<Article, String> {

}
