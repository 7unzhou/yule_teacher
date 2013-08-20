package com.yulebaby.teacher.model;

public class DaySalary  implements BaseModel  {
	
	private String day;
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	private int total;


	
}
