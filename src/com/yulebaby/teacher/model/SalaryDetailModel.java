package com.yulebaby.teacher.model;

public class SalaryDetailModel  implements BaseModel  {
	
	private String name;
	private int num;
	private String workDate;
	private String reason;
	private String comment;
	private int wages;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getWages() {
		return wages;
	}
	public void setWages(int wages) {
		this.wages = wages;
	}



	
}
