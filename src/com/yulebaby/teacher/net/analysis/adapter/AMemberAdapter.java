package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.util.Log;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.AblumMember;
import com.yulebaby.teacher.model.GuideDetail;
import com.yulebaby.teacher.model.Lesson;
import com.yulebaby.teacher.model.YPhoto;

public class AMemberAdapter implements ModelAdapter<AblumMember> {

	@Override
	public AblumMember analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "AblumMember analysis json = " + json);
		AblumMember bean = new AblumMember();
		JSONArray names = json.names();
		int cols = names.length();
		for (int i = 0; i < cols; i++) {
			JSONArray dataset = new JSONArray();
			String name = names.get(i).toString();
			String value = json.getString(name);
			if ("id".equals(name.trim().toLowerCase())) {
				// System.out.println("AblumMember id:"+value);
				bean.setId(value);
			} else if ("name".equals(name.trim().toLowerCase())) {
				bean.setName(value);
			} else if ("nick".equals(name.trim().toLowerCase())) {
				// bean.setIndex(value);
				bean.setNick(value);
			} else if ("img".equals(name.trim().toLowerCase())) {
				bean.setImgUrl(value);
			} else if ("total".equals(name.trim().toLowerCase())) {
				bean.setTotal(value);
			} else if ("today".equals(name.trim().toLowerCase())) {
				bean.setToday(value);
			} else if ("list".equals(name.trim().toLowerCase())) {
				// bean.setToday(json.getInt(name));

				dataset = json.getJSONArray("list");

				ArrayList<YPhoto> photos = new PhotoAdapter().analysis(dataset);
				bean.setPhotoList(photos);
				// ystem.out.println("add list to menber:");
			}

		}
		return bean;
	}

	@Override
	public ArrayList<AblumMember> analysisList(JSONObject json)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}