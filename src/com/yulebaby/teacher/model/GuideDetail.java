package com.yulebaby.teacher.model;

import android.text.Spanned;

public class GuideDetail extends Guide {

	
	private static final long serialVersionUID = 6360470601452500871L;
	
	private String mContent;
	private transient Spanned mSpanned;

	public String getContent() {
		return mContent;
	}

	public void setContent(String mContent) {
		this.mContent = mContent;
	}

	public Spanned getSpanned() {
		return mSpanned;
	}

	public void setSpanned(Spanned mSpanned) {
		this.mSpanned = mSpanned;
	}
	
}
