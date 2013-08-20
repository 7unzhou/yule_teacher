package com.yulebaby.teacher.server;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.text.TextUtils;

import com.yulebaby.teacher.model.OrderCustom;
import com.yulebaby.teacher.model.OrderDay;
import com.yulebaby.teacher.net.HttpHelper;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Url;
import com.yulebaby.teacher.net.analysis.Data4ListAnalysis;
import com.yulebaby.teacher.net.analysis.ListDataAnalysis;
import com.yulebaby.teacher.net.analysis.adapter.OrderDayAdapter;
import com.yulebaby.teacher.net.analysis.adapter.OrderListAdapter;


public class OrderCustomService {
	public static ModelData<OrderCustom> orderList(Context context, String date){
		ModelData<OrderCustom> result = null;
		String url = Url.host_url + Url.Order.orderlist;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		if(!TextUtils.isEmpty(date)){
			System.out.println("orderlist data:"+date);
			params.add(new BasicNameValuePair("date", date));
		}
		try {
			result = HttpHelper.getInfo(context, url, params, new ListDataAnalysis<OrderCustom>(new OrderListAdapter()));
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} 
		return result;
	}
	
	
	public static ModelData<OrderDay> reserveDays(Context context){
		ModelData<OrderDay> result = null;
		String url = Url.host_url + Url.Order.days;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		try {
			result = HttpHelper.getInfo(context, url, params, new Data4ListAnalysis(new OrderDayAdapter()));
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} 
		return result;
	}
}
