package com.yulebaby.teacher.server;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.yulebaby.teacher.model.AttendListModel;
import com.yulebaby.teacher.model.AttendModel;
import com.yulebaby.teacher.model.Commons;
import com.yulebaby.teacher.model.GuideDetail;
import com.yulebaby.teacher.model.Lesson;
import com.yulebaby.teacher.model.MonthsModel;
import com.yulebaby.teacher.net.HttpHelper;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Url;
import com.yulebaby.teacher.net.analysis.Data4ListAnalysis;
import com.yulebaby.teacher.net.analysis.DataAnalysis;
import com.yulebaby.teacher.net.analysis.ListDataAnalysis;
import com.yulebaby.teacher.net.analysis.adapter.AttendDataAdapter;
import com.yulebaby.teacher.net.analysis.adapter.LessonAdapter;
import com.yulebaby.teacher.net.analysis.adapter.MonthsAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class AttendService {

	public static ModelData<AttendListModel> list(Context context, String month) {
		ModelData<AttendListModel> result = null;
		String url = Url.host_url + Url.Attendance.attendance;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		if (!TextUtils.isEmpty(month)) {
			params.add(new BasicNameValuePair("month", month));
		}
		try {
			result = HttpHelper.getInfo(context, url, params,
					new DataAnalysis<AttendListModel>(new AttendDataAdapter()));
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}

		return result;
	}
	
	
	
	public static ModelData<MonthsModel> getMonths(Context context) {
		ModelData<MonthsModel> result = null;
		String url = Url.host_url + Url.Attendance.months;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			result = HttpHelper.getInfo(context, url, params,
					new Data4ListAnalysis(new MonthsAdapter()));
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}

		return result;
	}
}
