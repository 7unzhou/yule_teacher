package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.GuideDetail;


public class GuideAdapter implements ModelAdapter<GuideDetail> {

	@Override
	public GuideDetail analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "GuideDetail analysis json = " + json);
		GuideDetail bean = new GuideDetail();
		JSONArray names = json.names();
		int cols = names.length();
		for (int i = 0; i < cols; i++) {
			String name = names.get(i).toString();
			String value = json.getString(name);
			if("id".equals(name.trim().toLowerCase())){
				bean.setId(value);
			}else if("title".equals(name.trim().toLowerCase())){
				bean.setTitle(value);
			}else if("index".equals(name.trim().toLowerCase())){
				bean.setIndex(value);
			}else if("content".equals(name.trim().toLowerCase())){
				bean.setContent(value);
			}
		}
		return bean;
	}

	@Override
	public ArrayList<GuideDetail> analysisList(JSONObject json)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}