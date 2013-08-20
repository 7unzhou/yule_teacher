package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.ContentViewModel;
import com.yulebaby.teacher.model.InteractionModel;
import com.yulebaby.teacher.model.Lesson;
import com.yulebaby.teacher.model.OrderCustom;

public class ContentAdapter implements ModelAdapter<ContentViewModel> {

	@Override
	public ContentViewModel analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "ContentAdapter analysis json = " + json);
		ContentViewModel bean = new ContentViewModel();
		JSONArray names = json.names();
		int cols = names.length();
		for (int i = 0; i < cols; i++) {
			String name = names.get(i).toString();
			String value = json.getString(name);
			if ("height".equals(name.trim().toLowerCase())) {
				bean.setHeight(value);
			}
			if ("weight".equals(name.trim().toLowerCase())) {
				bean.setWeight(value);
			}
			if ("smscontent".equals(name.trim().toLowerCase())) {
				bean.setSmsContent(value);
			}
		}
		return bean;
	}

	@Override
	public ArrayList<ContentViewModel> analysisList(JSONObject json)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}
