package com.yulebaby.teacher.server;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.Context;

import com.yulebaby.teacher.model.Version;
import com.yulebaby.teacher.net.HttpHelper;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Url;
import com.yulebaby.teacher.net.analysis.TextDataAnalysis;
import com.yulebaby.teacher.net.analysis.adapter.VersionAdapter;


public class UpdateService {

	public static ModelData<Version> checkVersion(Context context){
		ModelData<Version> result = null;
		String url = Url.host_url + Url.Update.version;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			result = HttpHelper.getInfo(context, url, params, new TextDataAnalysis<Version>(new VersionAdapter()));
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} 
		return result;
	}
}
