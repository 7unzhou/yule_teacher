package com.yulebaby.teacher.model;

import android.graphics.Bitmap;

public class InteractionModel implements BaseModel {
	String id;
	String name;
	String nick;
	String img;
	String babyType;
	String month;
	String consumeDate;
	String consumeTime;
	String height;
	String weight;
	boolean isAppraisal;
	String remarks;
	String statifaction;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	String title;

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

	public String getBabyType() {
		return babyType;
	}

	public void setBabyType(String babyType) {
		this.babyType = babyType;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getConsumeDate() {
		return consumeDate;
	}

	public void setConsumeDate(String consumeDate) {
		this.consumeDate = consumeDate;
	}

	public String getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public boolean isAppraisal() {
		return isAppraisal;
	}

	public void setAppraisal(boolean isAppraisal) {
		this.isAppraisal = isAppraisal;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatifaction() {
		return statifaction;
	}

	public void setStatifaction(String statifaction) {
		this.statifaction = statifaction;
	}

	// public Bitmap iHeader;

}
