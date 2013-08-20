package com.yulebaby.teacher.model;

import java.io.Serializable;

public class Guide implements BaseModel, Serializable {

	private static final long serialVersionUID = -8366646650895232573L;
	
	private String mId;
	private String mIndex;
	private String mTitle;

	public String getId() {
		return mId;
	}

	public void setId(String mId) {
		this.mId = mId;
	}

	public String getIndex() {
		return mIndex;
	}

	public void setIndex(String mIndex) {
		this.mIndex = mIndex;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

}
