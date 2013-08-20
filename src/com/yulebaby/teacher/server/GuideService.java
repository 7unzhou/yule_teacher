package com.yulebaby.teacher.server;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.yulebaby.teacher.model.GuideDetail;
import com.yulebaby.teacher.model.SopLesson;
import com.yulebaby.teacher.net.HttpHelper;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Url;
import com.yulebaby.teacher.net.analysis.Data4ListAnalysis;
import com.yulebaby.teacher.net.analysis.ListDataAnalysis;
import com.yulebaby.teacher.net.analysis.adapter.SOPLessonAdapter;

import android.content.Context;
import android.text.TextUtils;


public class GuideService {

	public static ModelData<GuideDetail> guide(Context context){
		ModelData<GuideDetail> result = null;
//		String url = Url.host_url + Url.Guide.detail;
//		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//		try {
//			result = HttpHelper.getInfo(context, url, params, new ListDataAnalysis<GuideDetail>(new GuideAdapter()));
//		} catch (Exception e) {
//			e.printStackTrace();
//			result = null;
//		} 
		return result;
	}
	
	
	public static ModelData<SopLesson> sopLesson(Context context,String type,String id){
		ModelData<SopLesson> result = null;
		String url = Url.host_url + Url.SOP.sopLesson;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		if(!TextUtils.isEmpty(type)){
			params.add(new BasicNameValuePair("type", type));
			System.out.println("loadtype:"+type);
		}
		if(!TextUtils.isEmpty(id)){
			params.add(new BasicNameValuePair("id", id));
			System.out.println("id:"+id);
		}
		try {
			result = HttpHelper.getInfo(context, url, params, new Data4ListAnalysis(new SOPLessonAdapter()));
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} 
		return result;
	}
}
