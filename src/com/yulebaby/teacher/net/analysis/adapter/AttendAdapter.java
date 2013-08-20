package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;

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

public class AttendAdapter {

	public ArrayList<AttendModel> analysis(JSONArray dataset, AttendListModel listModel)
			throws JSONException {
		// Log.d(App.TAG, "YPhoto analysis json = " + dataset);

		ArrayList<AttendModel> mList = new ArrayList<AttendModel>();
		int length = dataset.length();
		for (int i = 0; i < length; i++) {
			JSONObject json = dataset.getJSONObject(i);
			// datas.addModel(mAdapter.analysis(obj));
			JSONArray names = json.names();
			int cols = names.length();
			AttendModel bean = new AttendModel();
			for (int j = 0; j < cols; j++) {
				String name = names.get(j).toString();
				String value = json.getString(name);
				if ("id".equals(name.trim().toLowerCase())) {
					// System.out.println("add photos id:" + value);
					bean.setId(value);
				} else if ("name".equals(name.trim().toLowerCase())) {
					bean.setName(value);
				} else if ("img".equals(name.trim().toLowerCase())) {
					// bean.setIndex(value);
					bean.setImg(value);
				} else if ("workdate".equals(name.trim().toLowerCase())) {
					bean.setWorkDate(value);
				} else if ("reason".equals(name.trim().toLowerCase())) {
					bean.setReson(value);
				} else if ("wages".equals(name.trim().toLowerCase())) {
					bean.setWages(value);
					listModel.addTotalWages(json.getInt(name));
				} else if ("comment".equals(name.trim().toLowerCase())) {
					bean.setComment(value);
				}
				// System.out.println("add photos to photo list");

			}
			mList.add(bean);
		}
		return mList;
	}
}