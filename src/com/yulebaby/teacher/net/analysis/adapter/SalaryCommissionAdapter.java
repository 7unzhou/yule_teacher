package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.DaySalary;
import com.yulebaby.teacher.model.GuideDetail;
import com.yulebaby.teacher.model.Lesson;


public class SalaryCommissionAdapter implements ModelAdapter<DaySalary> {

	@Override
	public DaySalary analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "LessonAdapter analysis json = " + json);
		DaySalary bean = new DaySalary();
		JSONArray names = json.names();
		int cols = names.length();
		for (int i = 0; i < cols; i++) {
			String name = names.get(i).toString();
			String value = json.getString(name);
			if("name".equals(name.trim().toLowerCase())){
				bean.setDay(value);
			}else if("num".equals(name.trim().toLowerCase())){
				bean.setTotal(json.getInt(name));
			}
		}
		return bean;
	}

	@Override
	public ArrayList<DaySalary> analysisList(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}