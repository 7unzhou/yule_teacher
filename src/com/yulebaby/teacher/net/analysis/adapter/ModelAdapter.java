package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.yulebaby.teacher.model.BaseModel;


public interface ModelAdapter<T extends BaseModel> {

	public T analysis(JSONObject json) throws JSONException;
	public ArrayList<T> analysisList(JSONObject json) throws JSONException;
}
