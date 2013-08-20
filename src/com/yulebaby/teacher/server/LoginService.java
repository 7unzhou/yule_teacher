package com.yulebaby.teacher.server;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.text.TextUtils;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.BaseModel;
import com.yulebaby.teacher.model.Login;
import com.yulebaby.teacher.net.HttpHelper;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Url;
import com.yulebaby.teacher.net.analysis.TextDataAnalysis;
import com.yulebaby.teacher.net.analysis.adapter.LoginAdapter;



public class LoginService {

	
	/**
	 * @param name : 用户名
	 * @param psw :密码
	 * @param bilingualUpdateTime:国学最后更新时间
	 * @param cultureUpdateTime:双语最后更新时间
	 * @param waterUpdateTime:水育最后更新时间
	 * */
	public static ModelData<Login> login(Context context, String name, String pwd,String bilingualUpdateTime,String cultureUpdateTime,String waterUpdateTime){
		ModelData<Login> result = null;
		String url = Url.host_url + Url.LoginOut.login;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", name));
		params.add(new BasicNameValuePair("password", pwd));
		params.add(new BasicNameValuePair("deviceType", "1"));
		params.add(new BasicNameValuePair("deviceNumber", App.getInstance().getUserInfo().getImei()));
		if(!TextUtils.isEmpty(bilingualUpdateTime)){
			params.add(new BasicNameValuePair("bilingualUpdateTime", bilingualUpdateTime));
		}
		if(!TextUtils.isEmpty(cultureUpdateTime)){
			params.add(new BasicNameValuePair("cultureUpdateTime", cultureUpdateTime));
		}
		if(!TextUtils.isEmpty(waterUpdateTime)){
			params.add(new BasicNameValuePair("waterUpdateTime", waterUpdateTime));
		}
		try {
			result = HttpHelper.login(context, url, params, new TextDataAnalysis<Login>(new LoginAdapter()));
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} 
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ModelData<BaseModel> logout(Context context){
		ModelData<BaseModel> result = null;
		String url = Url.host_url + Url.LoginOut.logout;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			result = HttpHelper.getInfo(context, url, params, new TextDataAnalysis());
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} 
		return result;
	}

}
