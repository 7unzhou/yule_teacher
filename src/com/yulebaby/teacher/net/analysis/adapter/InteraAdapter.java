package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.InteractionModel;
import com.yulebaby.teacher.model.Lesson;
import com.yulebaby.teacher.model.OrderCustom;

public class InteraAdapter implements ModelAdapter<InteractionModel> {

	@Override
	public InteractionModel analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "InteraAdapter analysis json = " + json);
		ArrayList<InteractionModel> list = new ArrayList<InteractionModel>();
		InteractionModel bean = new InteractionModel();
		JSONArray names = json.names();
		int cols = names.length();
		for (int i = 0; i < cols; i++) {
			String name = names.get(i).toString();
			String value = json.getString(name);
			if ("title".equals(name.trim().toLowerCase())) {
				// bean.setId(json.getInt(name));
				// System.out.println("get title:"+value);
				bean.setTitle(value);
			} else if ("list".equals(name.trim().toLowerCase())) {
				// bean.setName(value);
				System.out.println("get list:" + value);

				JSONArray dataset = json.getJSONArray("list");
				int length = dataset.length();
//				if (dataset.length() < 1) {
//					System.out.println("list is null");
//					return bean;
//				}
				for (int j = 0; j < length; j++) {
					JSONObject obj = dataset.getJSONObject(j);
					if (obj.has("id")) {
						// System.out.println(" id = " + obj.getString("id"));
						bean.setId(obj.getString("id"));
					}
					if (obj.has("name")) {
						// System.out.println(" name = " +
						// obj.getString("name"));
						bean.setName(obj.getString("name"));
					}
					if (obj.has("img")) {
						// System.out.println(" img = " + obj.getString("img"));
						bean.setImg(obj.getString("img"));
					}
					if (obj.has("babaType")) {
						// System.out.println(" img = " + obj.getString("img"));
						bean.setBabyType(obj.getString("babaType"));
					}
					if (obj.has("monthAge")) {
						// System.out.println(" img = " + obj.getString("img"));
						bean.setMonth(obj.getString("monthAge"));
					}
					if (obj.has("consumeDate")) {
						// System.out.println(" img = " + obj.getString("img"));
						bean.setConsumeDate(obj.getString("consumeDate"));
					}
					if (obj.has("consumeTime")) {
						// System.out.println(" img = " + obj.getString("img"));
						bean.setConsumeTime(obj.getString("consumeTime"));
					}
					if (obj.has("height")) {
						// System.out.println(" img = " + obj.getString("img"));
						bean.setHeight(obj.getString("height"));
					}
					if (obj.has("weight")) {
						// System.out.println(" img = " + obj.getString("img"));
						bean.setWeight(obj.getString("weight"));
					}
					if (obj.has("sms")) {
						// System.out.println(" img = " + obj.getString("img"));
						bean.setAppraisal(obj.getBoolean("sms"));
					}
					if (obj.has("remarks")) {
						// System.out.println(" img = " + obj.getString("img"));
						bean.setRemarks(obj.getString("remarks"));
					}
					if (obj.has("satisfaction")) {
						// System.out.println(" img = " + obj.getString("img"));
						bean.setStatifaction(obj.getString("satisfaction"));
					}
				}
			}

		}
		return bean;
	}

	@Override
	public ArrayList<InteractionModel> analysisList(JSONObject json)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}
