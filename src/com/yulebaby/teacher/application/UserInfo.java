package com.yulebaby.teacher.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class UserInfo {

	private String mJSessionId;
	private String mLoginName;
	private String mPassword;
	private String mImei;

	public String getCookie() {
		return mJSessionId;
	}

	public void setCookie(String jSessionId) {
		this.mJSessionId = jSessionId;
	}

	public String getLoginName() {
		return mLoginName;
	}

	public void setLoginName(String loginName) {
		mLoginName = loginName;
	}
	
	public void save(final Context context) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				SharedPreferences sp = context.getSharedPreferences(App.Name, 0);
				Editor editor = sp.edit();
				editor.putString("cookie", mJSessionId);
				editor.putString("name", mLoginName);
				editor.putString("password", mPassword);
				editor.commit();
			}
		}).start();
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		this.mPassword = password;
	}

	public String getImei() {
		return mImei;
	}

	public void setImei(String imei) {
		this.mImei = imei;
	}
	
}
