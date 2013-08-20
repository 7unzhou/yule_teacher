package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.Login;
import com.yulebaby.teacher.model.UserAuth;

import android.util.Log;

public class LoginAdapter implements ModelAdapter<Login> {

	@Override
	public Login analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "Login analysis json = " + json);
		Login bean = new Login();
		JSONArray names = json.names();
		int cols = names.length();
		for (int i = 0; i < cols; i++) {
			String name = names.get(i).toString();
			String value = json.getString(name);
			if ("id".equals(name.trim().toLowerCase())) {
				bean.setId(value);
			} else if ("name".equals(name.trim().toLowerCase())) {
				bean.setName(value);
			} else if ("key".equals(name.trim().toLowerCase())) {
				App.getInstance().getUserInfo().setCookie(value);
			} else if ("img".equals(name.trim().toLowerCase())) {
				bean.setImgUrl(value);
			} else if ("auth".equals(name.trim().toLowerCase())) {
				UserAuth auth = new UserAuth();
				JSONObject authJson = json.getJSONObject(name);
				if (authJson.has("cultureChapter")) {
					auth.setCultureChapter(authJson.getInt("cultureChapter"));
				}
				if (authJson.has("sopAuth")) {
					auth.setSopAuth(authJson.getInt("sopAuth"));
				}
				if (authJson.has("bilingualAuth")) {
					auth.setBilingualAuth(authJson.getInt("bilingualAuth"));
				}
				if (authJson.has("bilingualChapter")) {
					auth.setBilingualChapter(authJson
							.getInt("bilingualChapter"));
					// auth.setBilingualChapter(0);
				}
				if (authJson.has("cultureAuth")) {
					auth.setCultureAuth(authJson.getInt("cultureAuth"));
				}
				if (authJson.has("waterAuth")) {
					auth.setWaterAuth(authJson.getInt("waterAuth"));
				}
				if (authJson.has("waterChapter")) {
					auth.setWaterChapter(authJson.getInt("waterChapter"));
				}
				if (authJson.has("sopChapter")) {
					auth.setSopChapter(authJson.getInt("sopChapter"));
				}
				if (authJson.has("reserveAuth")) {
					auth.setReserveAuth(authJson.getInt("reserveAuth"));
				}
				if (authJson.has("consumeAuth")) {
					auth.setConsumeAuth(authJson.getInt("consumeAuth"));
				}
				if (authJson.has("photoAuth")) {
					auth.setPhotoAuth(authJson.getInt("photoAuth"));
				}
				if (authJson.has("attendanceAuth")) {
					auth.setAttendanceAuth(authJson.getInt("attendanceAuth"));
				}

				if (authJson.has("payrollAuth")) {
					auth.setPayrollAuth(authJson.getInt("payrollAuth"));
				}
				if (authJson.has("examAuth")) {
					auth.setExamAuth(authJson.getInt("examAuth"));
				}

				if (authJson.has("bilingualUpdate")) {
					System.out.println("authJson.has(bilingualUpdate)");
					
					if (authJson.getInt("bilingualUpdate") == 1) {
						auth.setBilingualUpdate(true);
					} else {
						auth.setBilingualUpdate(false);
					}
					// auth.setBilingualTime(authJson.getString("bilingualTime"));
				}
				if (authJson.has("cultureUpdate")) {
					System.out.println("authJson.has(cultureUpdate)");
					if (authJson.getInt("cultureUpdate") == 1) {
						auth.setCultureUpdate(true);
					} else {
						auth.setCultureUpdate(false);
					}
				}
				if (authJson.has("waterUpdate")) {
					System.out.println("authJson.has(waterUpdate)");
					
					if (authJson.getInt("waterUpdate") == 1) {
						auth.setWaterUpdate(true);
					} else {
						auth.setWaterUpdate(false);
					}
				}
				if (authJson.has("sopTime")) {
					auth.setSopTime(authJson.getString("sopTime"));
				}
				
				System.out.println("set auth:"+auth);
				System.out.println("set auth.isBilingualUpdate:"+auth.isBilingualUpdate());
				//System.out.println("set auth.isBilingualUpdate:"+auth.isBilingualUpdate());
				//System.out.println("set auth.isBilingualUpdate:"+auth.isBilingualUpdate());
				bean.setAuth(auth);
			}
		}
		return bean;
	}

	@Override
	public ArrayList<Login> analysisList(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

}
