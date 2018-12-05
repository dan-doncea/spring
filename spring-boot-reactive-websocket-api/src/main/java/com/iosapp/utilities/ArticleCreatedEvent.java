package com.iosapp.utilities;

import org.springframework.context.ApplicationEvent;

import com.iosapp.model.Article;

@SuppressWarnings("serial")
public class ArticleCreatedEvent extends ApplicationEvent {

	public ArticleCreatedEvent(Article source) {
		super(source);
	}

}
