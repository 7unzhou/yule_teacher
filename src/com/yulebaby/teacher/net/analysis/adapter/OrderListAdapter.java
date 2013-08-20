package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.Lesson;
import com.yulebaby.teacher.model.OrderCustom;

public class OrderListAdapter implements ModelAdapter<OrderCustom> {

	@Override
	public OrderCustom analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "OrderListAdapter analysis json = " + json);
		OrderCustom bean = new OrderCustom();
		JSONArray names = json.names();
		int cols = names.length();
		for (int i = 0; i < cols; i++) {
			String name = names.get(i).toString();
			String value = json.getString(name);
			if ("id".equals(name.trim().toLowerCase())) {
				bean.setId(json.getInt(name));
			} else if ("name".equals(name.trim().toLowerCase())) {
				bean.setName(value);
			} else if ("nick".equals(name.trim().toLowerCase())) {
				bean.setNick(value);
			} else if ("img".equals(name.trim().toLowerCase())) {
				bean.setImgUrl(value);
			} else if ("babytype".equals(name.trim().toLowerCase())) {
				bean.setBabyType(value);
			} else if ("reservedate".equals(name.trim().toLowerCase())) {
				bean.setReserveDate(value);
			} else if ("reservetime".equals(name.trim().toLowerCase())) {
				bean.setReserveTime(value);
				System.out.println("reserveTime:"+value);
			} else if ("remaintime".equals(name.trim().toLowerCase())) {
				bean.setRemainTime(value);
				System.out.println("remainTime:"+value);
			} else if ("remarks".equals(name.trim().toLowerCase())) {
				bean.setRemarks(value);
			} else if ("monthage".equals(name.trim().toLowerCase())) {
				bean.setMonthAge(json.getInt(name));
			}
		}
		return bean;
	}

	@Override
	public ArrayList<OrderCustom> analysisList(JSONObject json)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}
