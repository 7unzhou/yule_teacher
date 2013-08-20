package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.Version;


public class VersionAdapter implements ModelAdapter<Version> {

	@Override
	public Version analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "Version analysis json = " + json);
		Version bean = new Version();
		JSONArray names = json.names();
		int cols = names.length();
		for (int i = 0; i < cols; i++) {
			String name = names.get(i).toString();
			String value = json.getString(name);
			if("versioncode".equals(name.trim().toLowerCase())){
				bean.setVersionCode(value);
			}else if("force".equals(name.trim().toLowerCase())){
				bean.setForce(value);
			}else if("url".equals(name.trim().toLowerCase())){
				bean.setUrl(value);
			}else if("version".equals(name.trim().toLowerCase())){
				bean.setVersion(value);
			}
		}
		return bean;
	}

	@Override
	public ArrayList<Version> analysisList(JSONObject json)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

}
