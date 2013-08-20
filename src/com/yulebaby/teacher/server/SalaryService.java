package com.yulebaby.teacher.server;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.text.TextUtils;

import com.yulebaby.teacher.model.DaySalary;
import com.yulebaby.teacher.model.ExamModel;
import com.yulebaby.teacher.model.MonthsModel;
import com.yulebaby.teacher.model.SalaryDetailModel;
import com.yulebaby.teacher.model.SalaryMonthModel;
import com.yulebaby.teacher.net.HttpHelper;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Url;
import com.yulebaby.teacher.net.analysis.Data4ListAnalysis;
import com.yulebaby.teacher.net.analysis.DataAnalysis;
import com.yulebaby.teacher.net.analysis.ListDataAnalysis;
import com.yulebaby.teacher.net.analysis.adapter.ExamListAdapter;
import com.yulebaby.teacher.net.analysis.adapter.MonthsAdapter;
import com.yulebaby.teacher.net.analysis.adapter.SalaryCommissionAdapter;
import com.yulebaby.teacher.net.analysis.adapter.SalaryDetailAdapter;
import com.yulebaby.teacher.net.analysis.adapter.SalaryMonthAdapter;

public class SalaryService {

	public static ModelData<SalaryMonthModel> getMonthSalary(Context context,
			String month) {
		ModelData<SalaryMonthModel> result = null;
		String url = Url.host_url + Url.Salary.payroll;
		// String url = "http://114.113.158.6:38880/s/payroll";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		if (!TextUtils.isEmpty(month)) {
			params.add(new BasicNameValuePair("month", month));
		}
		try {
			result = HttpHelper
					.getInfo(context, url, params,
							new DataAnalysis<SalaryMonthModel>(
									new SalaryMonthAdapter()));
			// System.out.println("getMonthSalary result:"+result.getModels().size());

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public static ModelData<DaySalary> commission(Context context, String month) {
		ModelData<DaySalary> result = null;
		String url = Url.host_url + Url.Salary.commission;
		// String url = "http://114.113.158.6:38880/s/payroll";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		// if (!TextUtils.isEmpty(month)) {
		params.add(new BasicNameValuePair("month", month));
		// }
		try {
			result = HttpHelper.getInfo(context, url, params,
					new ListDataAnalysis<DaySalary>(
							new SalaryCommissionAdapter()));
			// System.out.println("getMonthSalary result:"+result.getModels().size());

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public static ModelData<DaySalary> dayDetail(Context context, String day) {
		ModelData<DaySalary> result = null;
		String url = Url.host_url + Url.Salary.dayDestail;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("day", day));
		try {
			result = HttpHelper.getInfo(context, url, params,
					new ListDataAnalysis<DaySalary>(
							new SalaryCommissionAdapter()));

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public static ModelData<SalaryDetailModel> salaryDetail(Context context,
			String month, String type) {
		ModelData<SalaryDetailModel> result = null;
		String url = Url.host_url + Url.Salary.typeDetails;
		System.out.println("Load url:" + url);
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("month", month));
		params.add(new BasicNameValuePair("type", type));

		try {
			result = HttpHelper.getInfo(context, url, params,
					new ListDataAnalysis<SalaryDetailModel>(
							new SalaryDetailAdapter()));

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
	
	
	public static ModelData<MonthsModel> getMonths(Context context) {
		ModelData<MonthsModel> result = null;
		String url = Url.host_url + Url.Salary.months;
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
