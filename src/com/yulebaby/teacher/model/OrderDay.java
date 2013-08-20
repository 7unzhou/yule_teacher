package com.yulebaby.teacher.model;


public class OrderDay implements BaseModel {

	private String mDate;
	private String mWeek;
	private String mMonth;
	private String mDay;

	public String getDate() {
		return mDate;
	}

	public void setDate(String mDate) {
		this.mDate = mDate;
		this.mMonth = mDate.substring(5, 7);
		this.mDay = mDate.substring(8, 10);
	}

	public String getWeek() {
		return mWeek;
	}

	public void setWeek(String mWeek) {
		this.mWeek = mWeek;
	}

	public String getMonth() {
		return mMonth;
	}

	public void setMonth(String mMonth) {
		this.mMonth = mMonth;
	}

	public String getDay() {
		return mDay;
	}

	public void setDay(String mDay) {
		this.mDay = mDay;
	}
	
}
