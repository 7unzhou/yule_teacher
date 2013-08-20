package com.yulebaby.teacher.net.analysis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.yulebaby.teacher.model.BaseModel;
import com.yulebaby.teacher.net.Json2DataInterface;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.analysis.adapter.ModelAdapter;


public class TextDataAnalysis<T extends BaseModel> extends Json2DataInterface<T> {

	public TextDataAnalysis(ModelAdapter<T> adapter) {
		super(adapter);
	}
	
	public TextDataAnalysis(){
		super();
	}
	
	@Override
	public ModelData<T> analyzeJson(Context context, JSONObject json) {
		ModelData<T> data = new ModelData<T>();
		JSONArray names = json.names();
		try {
			int length = names.length();
			for (int i = 0; i < length; i++) {
				String name = names.get(i).toString();
				String value = json.getString(name);
				if (("result").equals(name.trim().toLowerCase())) {
					data.setResultCode(Integer.valueOf(value));
				} else if (("message").equals(name.trim().toLowerCase())) {
					data.setMessage(value);
				} else if (("data").equals(name.trim().toLowerCase())) {
					if(mAdapter != null)
						data.setInfo(mAdapter.analysis(new JSONObject(value)));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

}
