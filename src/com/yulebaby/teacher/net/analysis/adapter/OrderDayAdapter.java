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
import com.yulebaby.teacher.model.OrderDay;
import com.yulebaby.teacher.model.ReLessonModel;
import com.yulebaby.teacher.model.YPhoto;

public class OrderDayAdapter implements ModelAdapter<OrderDay> {

	@Override
	public OrderDay analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "OrderDayAdapter analysis json = " + json);

		return null;
	}

	@Override
	public ArrayList<OrderDay> analysisList(JSONObject json)
			throws JSONException {
		ArrayList<OrderDay> mList = new ArrayList<OrderDay>();

		JSONArray months = json.getJSONArray("days");
		// JSONArray names = months.names();
		String title;
		int cols = months.length();
		for (int i = 0; i < cols; i++) {
			OrderDay bean = new OrderDay();

			JSONObject dataset = months.getJSONObject(i);
			if (dataset.has("weekZn")) {
				bean.setWeek(dataset.getString("weekZn"));
			}
			if (dataset.has("day")) {
				bean.setDate(dataset.getString("day"));
			}
			mList.add(bean);

		}

		return mList;
	}

}