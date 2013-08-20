package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.AblumModel;
import com.yulebaby.teacher.model.Lesson;
import com.yulebaby.teacher.model.OrderCustom;

public class SearchAListAdapter implements ModelAdapter<AblumModel> {

	@Override
	public AblumModel analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "SearchAListAdapter analysis json = " + json);
		AblumModel bean = new AblumModel();
		JSONArray names = json.names();
		int cols = names.length();
		for (int i = 0; i < cols; i++) {
			String name = names.get(i).toString();
			String value = json.getString(name);
			if ("id".equals(name.trim().toLowerCase())) {
				bean.setId(value);
			} else if ("name".equals(name.trim().toLowerCase())) {
				bean.setName(value);
			} else if ("nick".equals(name.trim().toLowerCase())) {
				bean.setNick(value);
			} else if ("total".equals(name.trim().toLowerCase())) {
				bean.setTotal(value);
			} else if ("name".equals(name.trim().toLowerCase())) {
				bean.setName(value);
			} else if ("today".equals(name.trim().toLowerCase())) {
				bean.setToday(json.getInt(name));
			}
		}
		return bean;
	}

	@Override
	public ArrayList<AblumModel> analysisList(JSONObject json)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}
