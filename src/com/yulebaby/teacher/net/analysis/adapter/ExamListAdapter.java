package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.AblumModel;
import com.yulebaby.teacher.model.ExamModel;
import com.yulebaby.teacher.model.Lesson;
import com.yulebaby.teacher.model.OrderCustom;

public class ExamListAdapter implements ModelAdapter<ExamModel> {

	@Override
	public ExamModel analysis(JSONObject json) throws JSONException {
		//Log.d(App.TAG, "ExamListAdapter analysis json = " + json);
		ExamModel bean = new ExamModel();
		JSONArray names = json.names();
		int cols = names.length();
		for (int i = 0; i < cols; i++) {
			String name = names.get(i).toString();
			String value = json.getString(name);
			if ("id".equals(name.trim().toLowerCase())) {
				bean.setId(value);
			} else if ("createtime".equals(name.trim().toLowerCase())) {
				bean.setCreateTime(value);
			} else if ("totalscore".equals(name.trim().toLowerCase())) {
				bean.setTotalScore(json.getInt(name));
			} else if ("status".equals(name.trim().toLowerCase())) {
				if (value.equals("1")) {
					bean.isExaming(true);
				} else {
					bean.isExaming(false);
				}
			} else if ("name".equals(name.trim().toLowerCase())) {
				bean.setName(value);
			} else if ("score".equals(name.trim().toLowerCase())) {
				bean.setScore(json.getInt(name));
			} else if ("type".equals(name.trim().toLowerCase())) {
				bean.setType(value);
			}
		}
		return bean;
	}

	@Override
	public ArrayList<ExamModel> analysisList(JSONObject json)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}
