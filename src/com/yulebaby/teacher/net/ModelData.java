package com.yulebaby.teacher.net;

import java.util.ArrayList;

import com.yulebaby.teacher.model.BaseModel;

public class ModelData<T extends BaseModel> {

	private int mResultCode;
	private String mMessage = "";
	private ArrayList<T> mDatas = new ArrayList<T>();
	private T mT;
	private Object mTag;
	private String mTotal;

	public ModelData() {
	}

	public Object getTag() {
		return mTag;
	}

	public void setTag(Object mTag) {
		this.mTag = mTag;
	}

	public String getTotal() {
		return mTotal;
	}

	public void setTotal(String mTotal) {
		this.mTotal = mTotal;
	}

	public int getResultCode() {
		return mResultCode;
	}

	public void setResultCode(int mResultCode) {
		this.mResultCode = mResultCode;
	}

	public String getMessage() {
		return mMessage;
	}

	public void setMessage(String mMessage) {
		this.mMessage = mMessage;
	}

	public void addModel(T t) {
		mDatas.add(t);
	}

	public ArrayList<T> getModels() {
		return mDatas;
	}

	public void setInfo(T t) {
		mT = t;
	}

	public T getInfo() {
		return mT;
	}

	public void addAll(ArrayList<T> mData) {
		this.mDatas = mData;
	}
}
