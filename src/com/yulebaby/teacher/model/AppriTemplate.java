package com.yulebaby.teacher.model;

public class AppriTemplate {
	int id;
	public AppriTemplate(int id, String content) {
		super();
		this.id = id;
		this.content = content;
	}

	String content;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
