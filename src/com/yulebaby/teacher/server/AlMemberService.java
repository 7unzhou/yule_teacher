package com.yulebaby.teacher.server;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.yulebaby.teacher.model.AblumMember;
import com.yulebaby.teacher.model.Commons;
import com.yulebaby.teacher.model.GuideDetail;
import com.yulebaby.teacher.model.Lesson;
import com.yulebaby.teacher.net.HttpHelper;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Url;
import com.yulebaby.teacher.net.analysis.DataAnalysis;
import com.yulebaby.teacher.net.analysis.ListDataAnalysis;
import com.yulebaby.teacher.net.analysis.adapter.AMemberAdapter;
import com.yulebaby.teacher.net.analysis.adapter.LessonAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class AlMemberService {

	public static ModelData<AblumMember> albumMember(Context context, String id) {
		ModelData<AblumMember> result = null;
		String url = Url.host_url + Url.AblumList.memberAblum;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		//System.out.println("type:" + type);
		try {
			result = HttpHelper.getInfo(context, url, params,
					new DataAnalysis<AblumMember>(new AMemberAdapter()));
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}

		return result;
	}
}
