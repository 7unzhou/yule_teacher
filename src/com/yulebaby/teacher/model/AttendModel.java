package com.yulebaby.teacher.model;

public class AttendModel implements BaseModel {

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public String getReson() {
		return reson;
	}
	public void setReson(String reson) {
		this.reson = reson;
	}
	public String getWages() {
		return wages;
	}
	public void setWages(String wages) {
		this.wages = wages;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	//private String title;
	private String id;
	private String name;
	private String img;
	private String workDate;
	private String reson;
	private String wages;
	private String comment;
	

}
