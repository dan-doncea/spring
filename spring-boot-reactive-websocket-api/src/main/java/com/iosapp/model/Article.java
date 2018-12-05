package com.iosapp.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Article {

	@JsonIgnore
	private String id;
	
	private String title;
	
	private String content;

	public Article(){}
	
	public Article(String title, String content) {
		this.id = UUID.randomUUID().toString();
		this.title = title;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
