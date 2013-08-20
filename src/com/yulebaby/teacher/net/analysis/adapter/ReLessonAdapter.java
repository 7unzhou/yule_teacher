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
import com.yulebaby.teacher.model.AblumModel;
import com.yulebaby.teacher.model.AttendModel;
import com.yulebaby.teacher.model.GuideDetail;
import com.yulebaby.teacher.model.Lesson;
import com.yulebaby.teacher.model.ReLessonModel;
import com.yulebaby.teacher.model.YPhoto;

public class ReLessonAdapter implements ModelAdapter<ReLessonModel> {

	private static final String TYPE_PREV = "prev";
	private static final String TYPE_LAST = "last";

	private ArrayList<ReLessonModel> analysisLesson(JSONArray dataset,
			boolean isPre) {

		ArrayList<ReLessonModel> list = new ArrayList<ReLessonModel>();

		int length = dataset.length();
		// System.out.println("analysisAlbum lenght:" + length);
		try {

			for (int i = 0; i < length; i++) {
				JSONObject json = dataset.getJSONObject(i);
				// datas.addModel(mAdapter.analysis(obj));
				JSONArray names = json.names();
				int cols = names.length();
				ReLessonModel model = new ReLessonModel();
				if (isPre) {
					model.setPre(isPre);
				}
				for (int j = 0; j < cols; j++) {
					String name = names.get(j).toString();
					String value = json.getString(name);
					if ("id".equals(name.trim().toLowerCase())) {
						 //System.out.println("add id:" + value);
						model.setId(json.getInt(name));
					} else if ("type".equals(name.trim().toLowerCase())) {
						// model.setName(value);
						if (value.equals("0")) {
							//model.setType("国学");
							model.setType(0);
							model.setTypeName("国学");
							
						}
						if (value.equals("1")) {
							model.setType(1);
							model.setTypeName("双语");
						}
						if (value.equals("2")) {
							model.setType(2);
							model.setTypeName("水育");
						}

					} else if ("title".equals(name.trim().toLowerCase())) {
						// model.setImg(value);
						model.setTitle(value);
					}
				}
				// System.out.println("add photos to photo list");
				list.add(model);
			}
		} catch (Exception e) {
			Log.d(App.TAG, "AblumList analysis : " + e);
		}
		return list;
	}

	@Override
	public ReLessonModel analysis(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ReLessonModel> analysisList(JSONObject json)
			throws JSONException {
		Log.d(App.TAG, "AblumList analysis json = " + json);

		ArrayList<ReLessonModel> mList = new ArrayList<ReLessonModel>();

		if (json.has(TYPE_PREV)) {
			mList.addAll(analysisLesson(json.getJSONArray(TYPE_PREV), true));
		}
		if (json.has(TYPE_LAST)) {
			mList.addAll(analysisLesson(json.getJSONArray(TYPE_LAST), false));
		}

		return mList;
	}
}