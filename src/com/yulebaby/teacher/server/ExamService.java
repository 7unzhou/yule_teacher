package com.yulebaby.teacher.server;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.text.TextUtils;

import com.yulebaby.teacher.model.ExamModel;
import com.yulebaby.teacher.model.ExamPaperModel;
import com.yulebaby.teacher.model.ExamReturnModel;
import com.yulebaby.teacher.model.OrderCustom;
import com.yulebaby.teacher.net.HttpHelper;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Url;
import com.yulebaby.teacher.net.analysis.DataAnalysis;
import com.yulebaby.teacher.net.analysis.ListDataAnalysis;
import com.yulebaby.teacher.net.analysis.adapter.ExamListAdapter;
import com.yulebaby.teacher.net.analysis.adapter.ExamPaperAdapter;
import com.yulebaby.teacher.net.analysis.adapter.ExamReturnAdapter;
import com.yulebaby.teacher.net.analysis.adapter.OrderListAdapter;

public class ExamService {
	public static ModelData<ExamModel> examList(Context context, String id,
			String type) {
		ModelData<ExamModel> result = null;
		String url = Url.host_url + Url.Exam.examList;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		if (!TextUtils.isEmpty(id)) {
			params.add(new BasicNameValuePair("id", id));
		}
		if (!TextUtils.isEmpty(type)) {
			params.add(new BasicNameValuePair("type", type));
		}
		try {
			result = HttpHelper.getInfo(context, url, params,
					new ListDataAnalysis<ExamModel>(new ExamListAdapter()));

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public static ModelData<ExamPaperModel> viewExam(Context context, String id) {
		ModelData<ExamPaperModel> result = null;
		String url = Url.host_url + Url.Exam.viewExamPaper;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		if (!TextUtils.isEmpty(id)) {
			params.add(new BasicNameValuePair("id", id));
		}
		try {
			result = HttpHelper.getInfo(context, url, params,
					new DataAnalysis<ExamPaperModel>(new ExamPaperAdapter()));

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public static ModelData<ExamReturnModel> submitExam(Context context,
			String id, String strAnswer) {
		ModelData<ExamReturnModel> result = null;
		String url = Url.host_url + Url.Exam.submitExam;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		// if (!TextUtils.isEmpty(id)) {
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("answers", strAnswer));
		// }
		try {
			result = HttpHelper.getInfo(context, url, params,
					new DataAnalysis<ExamReturnModel>(new ExamReturnAdapter()));

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

}
