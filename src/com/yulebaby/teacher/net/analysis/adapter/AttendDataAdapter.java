package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.util.Log;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.AblumMember;
import com.yulebaby.teacher.model.AttendListModel;
import com.yulebaby.teacher.model.AttendModel;
import com.yulebaby.teacher.model.GuideDetail;
import com.yulebaby.teacher.model.Lesson;
import com.yulebaby.teacher.model.YPhoto;

public class AttendDataAdapter implements ModelAdapter<AttendListModel> {

	@Override
	public AttendListModel analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "AttendListModel analysis json = " + json);
		AttendListModel bean = new AttendListModel();
		JSONArray names = json.names();
		String title;
		int cols = names.length();
		for (int i = 0; i < cols; i++) {

			JSONArray dataset = new JSONArray();
			String name = names.get(i).toString();
			String value = json.getString(name);
			if ("month".equals(name.trim().toLowerCase())) {
				// System.out.println("AblumMember id:"+value);
				// bean.setTitle(value);
				bean.setTitle(value);
			} else if ("list".equals(name.trim().toLowerCase())) {
				// bean.setToday(json.getInt(name));

				dataset = json.getJSONArray("list");

				ArrayList<AttendModel> dList = new AttendAdapter()
						.analysis(dataset,bean);
				bean.setList(dList);
				System.out.println("add list to month:");
			}

		}
		return bean;
	}

	@Override
	public ArrayList<AttendListModel> analysisList(JSONObject json)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

}