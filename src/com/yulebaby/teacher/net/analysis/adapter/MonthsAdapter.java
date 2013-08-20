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
import com.yulebaby.teacher.model.MonthsModel;
import com.yulebaby.teacher.model.ReLessonModel;
import com.yulebaby.teacher.model.YPhoto;

public class MonthsAdapter implements ModelAdapter<MonthsModel> {

	@Override
	public MonthsModel analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "MonthsAdapter analysis json = " + json);

		return null;
	}

	@Override
	public ArrayList<MonthsModel> analysisList(JSONObject json)
			throws JSONException {
		ArrayList<MonthsModel> mList = new ArrayList<MonthsModel>();

		JSONArray months = json.getJSONArray("months");
		// JSONArray names = months.names();
		String title;
		int cols = months.length();
		for (int i = 0; i < cols; i++) {
			MonthsModel bean = new MonthsModel();

			JSONObject dataset = months.getJSONObject(i);
			if (dataset.has("name")) {
				//System.out.println(" name:" + dataset.getString("name"));
				bean.setName(dataset.getString("name"));
			}
			if (dataset.has("month")) {
				bean.setMonth(dataset.getString("month"));
			}
			mList.add(bean);

		}

		return mList;
	}

}