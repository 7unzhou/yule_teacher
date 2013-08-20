package com.yulebaby.teacher.model;

public class AblumModel implements BaseModel{
	private boolean isRecent;
	public boolean isRecent() {
		return isRecent;
	}
	public void setRecent(boolean isRecent) {
		this.isRecent = isRecent;
	}
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
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public int getToday() {
		return today;
	}
	public void setToday(int today) {
		this.today = today;
	}
	private String id;
	private String name;
	private String nick;
	private String img;
	private String total;
	private int today;
}
