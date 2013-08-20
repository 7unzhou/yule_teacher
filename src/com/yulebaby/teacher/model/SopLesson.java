package com.yulebaby.teacher.model;

import android.text.Spanned;

public class SopLesson implements BaseModel {

	
	//private static final long serialVersionUID = 6360470601452500871L;
	
	private String id;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	private String title;
	private String url;
	//private 


	
}
