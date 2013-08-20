package com.yulebaby.teacher.server;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.text.TextUtils;

import com.yulebaby.teacher.model.ContentViewModel;
import com.yulebaby.teacher.model.InteractionModel;
import com.yulebaby.teacher.model.OrderCustom;
import com.yulebaby.teacher.net.HttpHelper;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Url;
import com.yulebaby.teacher.net.analysis.DataAnalysis;
import com.yulebaby.teacher.net.analysis.ListDataAnalysis;
import com.yulebaby.teacher.net.analysis.adapter.ContentAdapter;
import com.yulebaby.teacher.net.analysis.adapter.InteraAdapter;
import com.yulebaby.teacher.net.analysis.adapter.OrderListAdapter;

public class InteractionService {
	public static ModelData<InteractionModel> consume(Context context,
			String id, String type) {
		ModelData<InteractionModel> result = null;
		String url = Url.host_url + Url.Interaction.interaction;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		if (!TextUtils.isEmpty(id)) {
			params.add(new BasicNameValuePair("date", id));
			
		}
		if (!TextUtils.isEmpty(type)) {
			params.add(new BasicNameValuePair("type", type));
			System.out.println("type =" + type);
		}
		try {
			result = HttpHelper
					.getInfo(context, url, params,
							new ListDataAnalysis<InteractionModel>(
									new InteraAdapter()));
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
	
	
	
	public static ModelData<ContentViewModel> commentView(Context context,
			String id) {
		ModelData<ContentViewModel> result = null;
		String url = Url.host_url + Url.Interaction.commentView;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
			
		try {
			result = HttpHelper
					.getInfo(context, url, params,
							new DataAnalysis<ContentViewModel>(
									new ContentAdapter()));
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static ModelData submitComment(Context context, String id,String smsContent, String height, String weight){
		ModelData result = null;
		String url = Url.host_url + Url.Comment.comment;
		System.out.println("url:" + url);
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("smsContent", smsContent));
		params.add(new BasicNameValuePair("height", height));
		params.add(new BasicNameValuePair("weight", weight));
		params.add(new BasicNameValuePair("id", id));
		try {
			result = HttpHelper.setInfo(context, url, params);
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} 
		return result;
	}
}
