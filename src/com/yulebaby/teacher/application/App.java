package com.yulebaby.teacher.application;

import com.yulebaby.teacher.R;
import com.yulebaby.teacher.model.Login;
import com.yulebaby.teacher.model.UserAuth;
import com.yulebaby.teacher.net.Url;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.util.Log;


public class App extends Application {
	
	private static App instance;

	public static final String Name = "com.yulebaby.teacher";
	public static final String TAG = "YLBaby_Teacher";
	//private String[] mSatisfaction;
	//public static final int LIST_PAGE_SUM = 10;
	public static String mNetWorkIssue;
	//public static float mGridTextSize = 16f;
	//public static float mGridLineWidth = 3f;
	private UserInfo mUserInfo = new UserInfo();
	private Login mLogin = new Login();
	
	public static App getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		instance = this;
		SharedPreferences share = this.getSharedPreferences(Name, 0);
		String cookie = share.getString("cookie", "");
		String name = share.getString("name", "");
		String pwd = share.getString("password", "");
		Log.d(TAG, "cookie = " + cookie);
		Log.d(TAG, "name = " + name);
		Log.d(TAG, "pwd = " + pwd);
		mUserInfo.setCookie(cookie);
		mUserInfo.setLoginName(name);
		mUserInfo.setPassword(pwd);
		TelephonyManager tm = (TelephonyManager)(this.getSystemService(Context.TELEPHONY_SERVICE));
		String imei = tm.getDeviceId();
		mUserInfo.setImei(imei);
		Url.initHostUrl(this);
		//mSatisfaction = getResources().getStringArray(com.yulebaby.m.R.array.satisfaction);
		mNetWorkIssue = getResources().getString(R.string.no_network_connection_toast);
		//mGridTextSize = getResources().getDimension(R.dimen.line_text);
		//mGridLineWidth = getResources().getDimension(R.dimen.line_strokewidth);
		
		//CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
       // crashHandler.init(getApplicationContext());
	}
	
	public UserInfo getUserInfo(){
		return mUserInfo;
	}
	
	public void setUserInfo(UserInfo user){
		this.mUserInfo = user;
	}
	
	public void setLogin(Login login){
		Editor editor = getApplicationContext().getSharedPreferences(App.Name, 0).edit();
		editor.putString("login_id", login.getId());
		//editor.putString("login_babyType", login.getBabyType());
		//editor.putString("login_babyTypeZn", login.getBabyTypeZn());
		//editor.putString("login_age", login.getAge());
		editor.putString("login_name", login.getName());
		editor.putString("login_userurl", login.getImgUrl());
		//editor.putString("login_phone", login.getPhone());
		//editor.putString("login_point", login.getPoint());
		//editor.putString("login_year", login.getYear());
		//editor.putString("login_month", login.getMonth());
		//editor.putString("login_day", login.getDay());
		//editor.putInt("login_messageCount", login.getMessageCount());
		editor.putBoolean("isBilingualUpdate", login.getAuth().isBilingualUpdate());
		editor.putBoolean("isCultureUpdate", login.getAuth().isCultureUpdate());
		editor.putBoolean("isWaterUpdate", login.getAuth().isWaterUpdate());
		
		editor.putString("login_remainTimes", login.getRemainTimes());
		editor.commit();
		this.mLogin = login;
	}
	
	public Login getLogin(){
		if(mLogin == null || mLogin.isEmpty()){
			mLogin = new Login();
			SharedPreferences sp = getApplicationContext().getSharedPreferences(App.Name, 0);
			mLogin.setId(sp.getString("login_id", null));
			mLogin.setImgUrl(sp.getString("login_userurl",null));
			//mLogin.setBabyType(sp.getString("login_babyType", null));
			//mLogin.setBabyTypeZn(sp.getString("login_babyTypeZn", null));
			//mLogin.setAge(sp.getString("login_age", null));
			mLogin.setName(sp.getString("login_name", null));
			//mLogin.setPhone(sp.getString("login_phone", null));
			//mLogin.setPoint(sp.getString("login_point", null));
			//mLogin.setYear(sp.getString("login_year", null));
			//mLogin.setMonth(sp.getString("login_month", null));
			//mLogin.setDay(sp.getString("login_day", null));
			//mLogin.setMessageCount(sp.getInt("login_messageCount", 0));
			UserAuth auth = new UserAuth();
			auth.setCultureUpdate(sp.getBoolean("isCultureUpdate", false));
			auth.setBilingualUpdate(sp.getBoolean("isBilingualUpdate", false));
			auth.setWaterUpdate(sp.getBoolean("isWaterUpdate", false));
			mLogin.setAuth(auth);
			mLogin.setRemainTimes(sp.getString("login_remainTimes", null));
		}
		return mLogin;
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
}
