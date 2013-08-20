package com.yulebaby.teacher.server;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.yulebaby.teacher.model.AblumModel;
import com.yulebaby.teacher.model.Commons;
import com.yulebaby.teacher.model.GuideDetail;
import com.yulebaby.teacher.model.Lesson;
import com.yulebaby.teacher.model.Login;
import com.yulebaby.teacher.model.ReLessonModel;
import com.yulebaby.teacher.net.HttpHelper;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Url;
import com.yulebaby.teacher.net.analysis.Data4ListAnalysis;
import com.yulebaby.teacher.net.analysis.ListDataAnalysis;
import com.yulebaby.teacher.net.analysis.adapter.AblumListAdapter;
import com.yulebaby.teacher.net.analysis.adapter.LessonAdapter;
import com.yulebaby.teacher.net.analysis.adapter.ReLessonAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class LessonService {

	public static ModelData<Lesson> guide(Context context, Login loginUser,
			String type) {
		System.out.println("LessonService guide load type:" + type);
		System.out.println("LessonService guide load loginUser:" + loginUser.getAuth());
		
		ModelData<Lesson> result = null;
		// String strLessonPre = "";
		if (!checkUpdate(loginUser, type)) {
			System.out.println("no update type:" + type);
			SharedPreferences sp = context.getSharedPreferences(Commons.TEACHER_PREFERE, Context.MODE_PRIVATE);

			String strLessonPre = sp.getString("lesson" + type, "");

			// new ListDataAnalysis<Lesson>(new LessonAdapter())
			System.out.println("strLessonPre from preferences------>"
					+ strLessonPre);
			try {
				JSONObject json = new JSONObject(strLessonPre);
				result = new ListDataAnalysis<Lesson>(new LessonAdapter())
						.analyzeJson(context, json);
			} catch (JSONException e) {

				e.printStackTrace();
			}
		} else {

			System.out.println(" update type:" + type);
			String url = Url.host_url + Url.Lesson.lesson;
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("type", type));
			System.out.println("type:" + type);
			try {
				result = HttpHelper.getInfo(context, url, params,
						new ListDataAnalysis<Lesson>(new LessonAdapter()));
			} catch (Exception e) {
				e.printStackTrace();
				result = null;
			}

		}

		return result;
	}

	private static boolean checkUpdate(Login loginUser, String type) {
		
		System.out.println("loginUser.getAuth().isBilingualUpdate()");
		if(loginUser.getAuth().isBilingualUpdate()){
		}
		
		if (type == Commons.TYPE_CHINESE
				&& loginUser.getAuth().isBilingualUpdate()) {
			return true;
		}
		if (type == Commons.TYPE_ENGLISH
				&& loginUser.getAuth().isCultureUpdate()) {
			return true;
		}
		if (type == Commons.TYPE_WATER && loginUser.getAuth().isWaterUpdate()) {
			return true;
		}
		return false;
	}

	public static ModelData<Lesson> checkUpdate(Context context, String type,
			String time) {
		ModelData<Lesson> result = null;
		// 取出上次更新的时间，如果没有，则已当前系统的时间为更新时间
		// 问题:如果当时设备更改了时间，就会不准确

		SharedPreferences sp = context.getSharedPreferences(
				Commons.TEACHER_PREFERE, Context.MODE_PRIVATE);
		String updataTime = sp.getString("update_time" + type, time);
		String url = Url.host_url + Url.Lesson.lessonUpdate;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		// if (!TextUtils.isEmpty(time)) {
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("time", updataTime));
		// }
		try {
			result = HttpHelper.getInfo(context, url, params,
					new ListDataAnalysis<Lesson>(new LessonAdapter()));

			// 更新后把更新的时间保存到本地
			sp.edit().putString("update_time" + type, time).commit();
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public static ModelData<ReLessonModel> lessList(Context context,
			String memberId) {
		ModelData<ReLessonModel> result = null;
		String url = Url.host_url + Url.Lesson.reserveLesson;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		if (!TextUtils.isEmpty(memberId)) {
			params.add(new BasicNameValuePair("memberId", memberId));
		}
		try {
			result = HttpHelper
					.getInfo(context, url, params,
							new Data4ListAnalysis<ReLessonModel>(
									new ReLessonAdapter()));
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}
