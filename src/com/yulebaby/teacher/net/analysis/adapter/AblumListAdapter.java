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
import com.yulebaby.teacher.model.YPhoto;

public class AblumListAdapter implements ModelAdapter<AblumModel> {

	private static final String TYPE_RECENT = "recent";
	private static final String TYPE_LEAST = "least";
	private String page;

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		System.out.println("setpage:" + page);
		this.page = page;
	}

	private ArrayList<AblumModel> analysisAlbum(JSONArray dataset,
			boolean isRecent) {

		ArrayList<AblumModel> list = new ArrayList<AblumModel>();

		int length = dataset.length();
		//System.out.println("analysisAlbum lenght:" + length);
		try {

			for (int i = 0; i < length; i++) {
				JSONObject json = dataset.getJSONObject(i);
				// datas.addModel(mAdapter.analysis(obj));
				JSONArray names = json.names();
				int cols = names.length();
				AblumModel model = new AblumModel();
				if (isRecent) {
					model.setRecent(isRecent);
				}
				for (int j = 0; j < cols; j++) {
					String name = names.get(j).toString();
					String value = json.getString(name);
					if ("id".equals(name.trim().toLowerCase())) {
						System.out.println("add id:" + value);
						model.setId(value);
					} else if ("name".equals(name.trim().toLowerCase())) {
						model.setName(value);
					} else if ("nick".equals(name.trim().toLowerCase())) {
						// bean.setIndex(value);
						model.setNick(value);
					} else if ("img".equals(name.trim().toLowerCase())) {
						model.setImg(value);
					} else if ("total".equals(name.trim().toLowerCase())) {
						model.setTotal(value);
					} else if ("today".equals(name.trim().toLowerCase())) {
						model.setToday(json.getInt("today"));
					}
				}
				//System.out.println("add photos to photo list");
				list.add(model);
			}
		} catch (Exception e) {
			Log.d(App.TAG, "AblumList analysis : " + e);
		}
		return list;
	}

	@Override
	public AblumModel analysis(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<AblumModel> analysisList(JSONObject json)
			throws JSONException {
		Log.d(App.TAG, "AblumList analysis json = " + json);

		ArrayList<AblumModel> mList = new ArrayList<AblumModel>();
		if (json.has("page")) {

			setPage(json.getString("page"));
		}
		if (json.has(TYPE_RECENT)) {
			mList.addAll(analysisAlbum(json.getJSONArray(TYPE_RECENT), true));
		}
		if (json.has(TYPE_LEAST)) {
			mList.addAll(analysisAlbum(json.getJSONArray(TYPE_LEAST), false));
		}

		return mList;
	}
}