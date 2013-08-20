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
import com.yulebaby.teacher.model.SalaryDetailModel;


public class SalaryDetailAdapter implements ModelAdapter<SalaryDetailModel> {

	@Override
	public SalaryDetailModel analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "SalaryDetailAdapter analysis json = " + json);
		SalaryDetailModel bean = new SalaryDetailModel();
		JSONArray names = json.names();
		int cols = names.length();
		for (int i = 0; i < cols; i++) {
			String name = names.get(i).toString();
			String value = json.getString(name);
			if("name".equals(name.trim().toLowerCase())){
				//bean.setDay(value);
				bean.setName(name);
			}else if("num".equals(name.trim().toLowerCase())){
				bean.setNum(json.getInt(name));
			}else if("workdate".equals(name.trim().toLowerCase())){
				bean.setWorkDate(value);
			}
			else if("reason".equals(name.trim().toLowerCase())){
				bean.setReason(value);
			}
			else if("comment".equals(name.trim().toLowerCase())){
				bean.setComment(value);
			}
			else if("wages".equals(name.trim().toLowerCase())){
				bean.setWages(json.getInt(name));
			}
			
		}
		return bean;
	}

	@Override
	public ArrayList<SalaryDetailModel> analysisList(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}