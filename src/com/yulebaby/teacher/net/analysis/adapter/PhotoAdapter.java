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

public class PhotoAdapter {

	public ArrayList<YPhoto> analysis(JSONArray dataset) throws JSONException {
		//Log.d(App.TAG, "YPhoto analysis json = " + dataset);

		ArrayList<YPhoto> photos = new ArrayList<YPhoto>();
		int length = dataset.length();
		for (int i = 0; i < length; i++) {
			JSONObject json = dataset.getJSONObject(i);
			// datas.addModel(mAdapter.analysis(obj));
			JSONArray names = json.names();
			int cols = names.length();
			YPhoto bean = new YPhoto();
			for (int j = 0; j < cols; j++) {
				String name = names.get(j).toString();
				String value = json.getString(name);
				if ("id".equals(name.trim().toLowerCase())) {
					System.out.println("add photos id:" + value);
					bean.setId(value);
				} else if ("time".equals(name.trim().toLowerCase())) {
					bean.setTime(value);
				} else if ("monthage".equals(name.trim().toLowerCase())) {
					// bean.setIndex(value);
					bean.setMonthAge(value);
				} else if ("big".equals(name.trim().toLowerCase())) {
					bean.setBigUrl(value);
				} else if ("small".equals(name.trim().toLowerCase())) {
					bean.setSmallUrl(value);
				}
				//System.out.println("add photos to photo list");

			}
			photos.add(bean);
		}
		return photos;
	}
}