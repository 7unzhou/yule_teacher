package com.yulebaby.teacher.model;

import java.io.Serializable;
import java.util.ArrayList;

public class AblumMember implements BaseModel, Serializable {
	private static final long serialVersionUID = 5415242324858152360L;

	public String getId() {
		return mId;
	}

	public void setId(String mId) {
		this.mId = mId;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getNick() {
		return mNick;
	}

	public void setNick(String mNick) {
		this.mNick = mNick;
	}

	public String getImgUrl() {
		return mImgUrl;
	}

	public void setImgUrl(String mImgUrl) {
		this.mImgUrl = mImgUrl;
	}

	public String getTotal() {
		return mTotal;
	}

	public void setTotal(String mTotal) {
		this.mTotal = mTotal;
	}

	public String getToday() {
		return mToday;
	}

	public void setToday(String mToday) {
		this.mToday = mToday;
	}

	public ArrayList<YPhoto> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(ArrayList<YPhoto> photoList) {
		this.photoList = photoList;
	}

	private String mId;
	private String mName;
	private String mNick;
	private String mImgUrl;
	private String mTotal;
	private String mToday;
	private ArrayList<YPhoto> photoList;
}
