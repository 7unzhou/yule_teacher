package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yulebaby.teacher.adapter.AnswerAdapter;
import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.DaySalary;
import com.yulebaby.teacher.model.ExamAnswer;
import com.yulebaby.teacher.model.ExamPaperModel;
import com.yulebaby.teacher.model.QuestionModel;
import com.yulebaby.teacher.model.SalaryMonthModel;
import com.yulebaby.teacher.model.YPhoto;

public class SalaryMonthAdapter implements ModelAdapter<SalaryMonthModel> {

	@Override
	public SalaryMonthModel analysis(JSONObject json) throws JSONException {
		// Log.d(App.TAG, "SalaryMonthAdapter analysis json = " + json);
		SalaryMonthModel bean = new SalaryMonthModel();
		JSONArray names = json.names();
		int cols = names.length();
		if (json.has("name")) {
			// bean.setId(json.getString("name"));
			bean.setUserName(json.getString("name"));
		}
		if (json.has("level")) {
			bean.setLevel(json.getString("level"));
		}
		if (json.has("month")) {
			bean.setMonth(json.getString("month"));
		}
		if (json.has("total")) {
			bean.setTotal(json.getInt("total"));
		}
		if (json.has("basicWage")) {
			bean.setBasicWage(json.getInt("basicWage"));
		}
		if (json.has("insured")) {
			bean.setInsured(json.getInt("insured"));
		}
		if (json.has("commission")) {
			bean.setCommission(json.getInt("commission"));
		}
		if (json.has("add")) {
			bean.setAdd(json.getInt("add"));
		}
		if (json.has("sub")) {
			bean.setSub(json.getInt("sub"));
		}
		if (json.has("other")) {
			bean.setOther(json.getInt("other"));
		}
		if (json.has("details")) {
			JSONObject dataset = json.getJSONObject("details");
			analyDetails(dataset, bean);
			// bean.setDayList(qlist);
		}
		//System.out.println(" return bean:" + bean.getMonth());
		return bean;
	}

	private ArrayList<DaySalary> analyDetails(JSONObject dataset,
			SalaryMonthModel bean) throws JSONException {
		// Log.d(App.TAG, "analyDetails analysis json = " + dataset);

		ArrayList<DaySalary> model = new ArrayList<DaySalary>();

		JSONArray names = dataset.names();
		int length = names.length();
		for (int i = 0; i < length; i++) {
			String name = names.get(i).toString();
			String value = dataset.getString(name);

			if ("daymax".equals(name.trim().toLowerCase())) {
				bean.setDayMax(dataset.getInt(name));
			}

			if ("name".equals(name.trim().toLowerCase())) {
				// bean.setDayMax(json.getInt("dayMax"));
				// String name = json.getString("name");
				// System.out.println("deatails name : "+value );
			}

			if ("list".equals(name.trim().toLowerCase())) {
				ArrayList<DaySalary> list = analyDaySalaryList(dataset
						.getJSONArray("list"));
				// System.out.println("detail list lenght:" +list.size());
				bean.setDayList(list);
			}

			// model.add(bean);
		}
		return model;
	}

	private ArrayList<DaySalary> analyDaySalaryList(JSONArray dataset)
			throws JSONException {
		ArrayList<DaySalary> model = new ArrayList<DaySalary>();
		int length = dataset.length();
		for (int i = 0; i < length; i++) {
			JSONObject json = dataset.getJSONObject(i);
			// datas.addModel(mAdapter.analysis(obj));
			JSONArray names = json.names();
			int cols = names.length();
			DaySalary bean = new DaySalary();
			for (int j = 0; j < cols; j++) {
				String name = names.get(j).toString();
				String value = json.getString(name);
				if ("day".equals(name.trim().toLowerCase())) {
					// System.out.println("add ExamAnswer id:" + value);
					bean.setDay(value);
				} else if ("total".equals(name.trim().toLowerCase())) {
					// System.out.println("add ExamAnswer index:" + value);
					bean.setTotal(json.getInt(name));
				}
			}
			model.add(bean);
		}
		return model;
	}

	@Override
	public ArrayList<SalaryMonthModel> analysisList(JSONObject json)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}