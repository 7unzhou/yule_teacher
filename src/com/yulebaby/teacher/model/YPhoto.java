package com.yulebaby.teacher.model;

public class YPhoto implements BaseModel {
	public String getId() {
		return mId;
	}

	public void setId(String mId) {
		this.mId = mId;
	}

	public String getTime() {
		return mTime;
	}

	public void setTime(String mTime) {
		this.mTime = mTime;
	}

	public String getMonthAge() {
		return monthAge;
	}

	public void setMonthAge(String monthAge) {
		this.monthAge = monthAge;
	}

	public String getBigUrl() {
		return bigUrl;
	}

	public void setBigUrl(String bigUrl) {
		this.bigUrl = bigUrl;
	}

	public String getSmallUrl() {
		return smallUrl;
	}

	public void setSmallUrl(String smallUrl) {
		this.smallUrl = smallUrl;
	}

	private String mId;
	private String mTime;
	private String monthAge;
	private String bigUrl;
	private String smallUrl;
}
