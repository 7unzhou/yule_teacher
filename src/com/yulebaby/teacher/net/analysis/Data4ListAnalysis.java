package com.yulebaby.teacher.net.analysis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.BaseModel;
import com.yulebaby.teacher.net.Json2DataInterface;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.analysis.adapter.ModelAdapter;

public class Data4ListAnalysis<T extends BaseModel> extends
		Json2DataInterface<T> {

	public Data4ListAnalysis(ModelAdapter<T> adapter) {
		super(adapter);
	}

	@Override
	public ModelData<T> analyzeJson(Context context, JSONObject json) {
		Log.d(App.TAG, "ListDataAnalysis adapter = " + mAdapter);
		ModelData<T> datas = new ModelData<T>();
		int result;
		JSONObject data = null;
		JSONArray dataset = new JSONArray();
		try {
			result = Integer.valueOf(json.getString("result"));
			datas.setResultCode(result);
			try {
				String message = json.getString("message");
				datas.setMessage(message);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			data = json.getJSONObject("data");
			if (null != data) {
				datas.addAll(mAdapter.analysisList(data));
				//datas.addModel(mAdapter.analysis(data));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return datas;
	}

}
