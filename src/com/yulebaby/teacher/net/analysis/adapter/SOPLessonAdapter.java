package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.GuideDetail;
import com.yulebaby.teacher.model.MonthsModel;
import com.yulebaby.teacher.model.SopLesson;

public class SOPLessonAdapter implements ModelAdapter<SopLesson> {

	@Override
	public SopLesson analysis(JSONObject json) throws JSONException {
		// Log.d(App.TAG, "SopLesson analysis json = " + json);
		// SopLesson bean = new SopLesson();
		// JSONArray names = json.names();
		// int cols = names.length();
		// for (int i = 0; i < cols; i++) {
		// String name = names.get(i).toString();
		// String value = json.getString(name);
		// if("id".equals(name.trim().toLowerCase())){
		// // bean.setId(value);
		// }else if("title".equals(name.trim().toLowerCase())){
		// //bean.setTitle(value);
		// }else if("index".equals(name.trim().toLowerCase())){
		// // bean.setIndex(value);
		// }else if("content".equals(name.trim().toLowerCase())){
		// // bean.setContent(value);
		// }
		// }
		return null;
	}

	@Override
	public ArrayList<SopLesson> analysisList(JSONObject json)
			throws JSONException {
		Log.d(App.TAG, "SopLesson analysis json = " + json);
		ArrayList<SopLesson> mList = new ArrayList<SopLesson>();
		JSONArray listArray = json.getJSONArray("list");
		// JSONObject dataObject = (JSONObject) listArray.get(0);
		// Log.d(App.TAG, "SopLesson analysis json = " + dataObject);
		// JSONArray sopSections = dataObject.getJSONArray("sopSections");
		int cols = listArray.length();
		for (int i = 0; i < cols; i++) {
			// JSONObject dataset = sopSections.getJSONObject(0);
			// if (dataset.has("sops")) {
			// System.out.println(" name:" + dataset.getString("name"));
			// JSONArray sopList = dataset.getJSONArray("sops");
			// System.out.println("analysis sopLIst:"+sopList);
			// int sopLenght = sopList.length();
			// for (int j = 0; j < sopLenght; j++) {
			JSONObject sopLessonJson = listArray.getJSONObject(i);
			System.out.println("sopLessonJson:" + sopLessonJson);
			SopLesson sopLesson = new SopLesson();
			if (sopLessonJson.has("id")) {
				sopLesson.setId(sopLessonJson.getString("id"));
			}
			if (sopLessonJson.has("title")) {
				System.out.println("sopLessonJson title:"
						+ sopLessonJson.getString("title"));
				sopLesson.setTitle(sopLessonJson.getString("title"));
			}
			if (sopLessonJson.has("url")) {
				sopLesson.setUrl(sopLessonJson.getString("url"));
			}
			mList.add(sopLesson);
			// System.out.println(" add to list by id:"+sopLessonJson.getString("id"));

			// }
		}
		// }
		// JSONArray listArray.get(0);
		// TODO Auto-generated method stub
		System.out.println("sopList lenght in adapter :" + mList.size());
		return mList;
	}
}