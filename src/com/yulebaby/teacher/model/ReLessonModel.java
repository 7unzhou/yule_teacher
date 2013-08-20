package com.yulebaby.teacher.model;

public class ReLessonModel implements BaseModel {
	private boolean isPre;
	private int id;

	public boolean isPre() {
		return isPre;
	}

	public void setPre(boolean isRecent) {
		this.isPre = isRecent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private int type;
	private String typeName;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String title;

}
