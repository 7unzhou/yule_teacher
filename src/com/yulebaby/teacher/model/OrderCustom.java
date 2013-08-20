package com.yulebaby.teacher.model;

import java.io.Serializable;

public class OrderCustom implements BaseModel , Serializable{

	private static final long serialVersionUID = -3596117087913799920L;
	public int getId() {
		return mId;
	}

	public void setId(int mId) {
		this.mId = mId;
	}

	public String getNick() {
		return mNick;
	}

	public void setNick(String mNick) {
		this.mNick = mNick;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getBabyType() {
		return mBabyType;
	}

	public void setBabyType(String mBabyType) {
		this.mBabyType = mBabyType;
	}

	public int getMonthAge() {
		return mMonthAge;
	}

	public void setMonthAge(int mMonthAge) {
		this.mMonthAge = mMonthAge;
	}

	public String getReserveDate() {
		return mReserveDate;
	}

	public void setReserveDate(String mReserveDate) {
		this.mReserveDate = mReserveDate;
	}

	public String getReserveTime() {
		return mReserveTime;
	}

	public void setReserveTime(String mReserveTime) {
		this.mReserveTime = mReserveTime;
	}

	public String getRemainTime() {
		return mRemainTime;
	}

	public void setRemainTime(String mRemainTime) {
		this.mRemainTime = mRemainTime;
	}

	public String getRemarks() {
		return mRemarks;
	}

	public void setRemarks(String mRemarks) {
		this.mRemarks = mRemarks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	int mId;
	String mNick;
	String imgUrl;
	String mBabyType;
	int mMonthAge;
	String mReserveDate;
	String mReserveTime;
	String mRemainTime;
	String mRemarks;
	String name;

}
