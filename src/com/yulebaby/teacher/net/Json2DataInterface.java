package com.yulebaby.teacher.net;

import org.json.JSONObject;

import android.content.Context;

import com.yulebaby.teacher.model.BaseModel;
import com.yulebaby.teacher.net.analysis.adapter.ModelAdapter;


public abstract class Json2DataInterface<T extends BaseModel> {

	protected ModelAdapter<T> mAdapter;
	
	public Json2DataInterface() {
		mAdapter = null;
	}

	public Json2DataInterface(ModelAdapter<T> adapter) {
		mAdapter = adapter;
	}
	
	public abstract ModelData<T> analyzeJson(Context context, JSONObject json);
}
