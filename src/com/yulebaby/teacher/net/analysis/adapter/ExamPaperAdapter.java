package com.yulebaby.teacher.net.analysis.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yulebaby.teacher.adapter.AnswerAdapter;
import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.ExamAnswer;
import com.yulebaby.teacher.model.ExamPaperModel;
import com.yulebaby.teacher.model.QuestionModel;
import com.yulebaby.teacher.model.YPhoto;

public class ExamPaperAdapter implements ModelAdapter<ExamPaperModel> {

	@Override
	public ExamPaperModel analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "ExamPaperModel analysis json = " + json);
		ExamPaperModel bean = new ExamPaperModel();
		JSONArray names = json.names();
		int cols = names.length();
		if(json.has("id")){
			bean.setId(json.getString("id"));
		}
		if(json.has("status")){
			bean.setStatus(json.getString("status"));
		}
		if(json.has("score")){
			bean.setScore(json.getInt("score"));
		}
		if(json.has("createTime")){
			bean.setCreateTime(json.getString("createTime"));
		}
		if(json.has("completionTime")){
			bean.setCompletionTime(json.getString("completionTime"));
		}
		if(json.has("totalScore")){
			bean.setTotalScore(json.getInt("totalScore"));
		}
		if(json.has("userName")){
			bean.setUserName(json.getString("userName"));
		}
		if(json.has("questionNum")){
			bean.setQuestionNum(json.getInt("questionNum"));
		}
		if(json.has("list")){
			JSONArray dataset = new JSONArray();
			dataset = json.getJSONArray("list");
			ArrayList<QuestionModel> qlist = analyQuestList(dataset);
			bean.setQuestList(qlist);
		}
//		for (int i = 0; i < cols; i++) {
//			JSONArray dataset = new JSONArray();
//			String name = names.get(i).toString();
//			String value = json.getString(name);
//			if ("id".equals(name.trim().toLowerCase())) {
//				// System.out.println("AblumMember id:"+value);
//				bean.setId(value);
//			} else if ("status".equals(name.trim().toLowerCase())) {
//				bean.setStatus(value);
//			} else if ("score".equals(name.trim().toLowerCase())) {
//				bean.setScore(json.getInt(name));
//			} else if ("createtime".equals(name.trim().toLowerCase())) {
//				bean.setCreateTime(value);
//			} else if ("completiontime".equals(name.trim().toLowerCase())) {
//				bean.setCompletionTime(value);
//			} else if ("totalscore".equals(name.trim().toLowerCase())) {
//				bean.setTotalScore(json.getInt(name));
//			} else if ("username".equals(name.trim().toLowerCase())) {
//				bean.setUserName(value);
//			} else if ("questionnum".equals(name.trim().toLowerCase())) {
//				bean.setQuestionNum(json.getInt(name));
//			} else if ("list".equals(name.trim().toLowerCase())) {
//				// bean.setToday(json.getInt(name));
//
//				dataset = json.getJSONArray("list");
//				ArrayList<QuestionModel> qlist = analyQuestList(dataset);
//				bean.setQuestList(qlist);
//				// ArrayList<YPhoto> photos = new
//				// PhotoAdapter().analysis(dataset);
//				// bean.setPhotoList(photos);
//				// ystem.out.println("add list to menber:");
//			}
//
//		}
		return bean;
	}

	private ArrayList<QuestionModel> analyQuestList(JSONArray dataset)
			throws JSONException {
		// Log.d(App.TAG, "YPhoto analysis json = " + dataset);

		ArrayList<QuestionModel> model = new ArrayList<QuestionModel>();
		int length = dataset.length();
		for (int i = 0; i < length; i++) {
			JSONObject json = dataset.getJSONObject(i);
			// datas.addModel(mAdapter.analysis(obj));
			JSONArray names = json.names();
			int cols = names.length();
			QuestionModel bean = new QuestionModel();
			if(json.has("id")){
				bean.setId(json.getString("id"));
			}
			if(json.has("selected")){
				bean.setSelectedId(json.getString("selected"));
				//json.getString(name);
			}
			if(json.has("title")){
				bean.setStrQuestion(json.getString("title"));
			}
			if(json.has("score")){
				bean.setScore(json.getInt("score"));
			}
			if(json.has("answers")){
				ArrayList<ExamAnswer> answers = analyAnswerList(json.getJSONArray("answers"),bean);
				bean.setAnswers(answers);
			}
//			for (int j = 0; j < cols; j++) {
//				String name = names.get(j).toString();
//				String value = json.getString(name);
//				if ("id".equals(name.trim().toLowerCase())) {
//					System.out.println("add QuestionModel id:" + value);
//					bean.setId(value);
//				} else if ("selected".equals(name.trim().toLowerCase())) {
//					//System.out.println("add QuestionModel selected:" + value);
//				} else if ("title".equals(name.trim().toLowerCase())) {
//					bean.setStrQuestion(value);
//				} else if ("score".equals(name.trim().toLowerCase())) {
//					bean.setScore(json.getInt(name));
//				} else if ("answers".equals(name.trim().toLowerCase())) {
//					//dataset = ;
//					ArrayList<ExamAnswer> answers = analyAnswerList(json.getJSONArray("answers"),bean
//							);
//					bean.setAnswers(answers);
//				}
//				// System.out.println("add photos to photo list");
//
//			}
			model.add(bean);
		}
		return model;
	}

	private ArrayList<ExamAnswer> analyAnswerList(JSONArray dataset, QuestionModel questionModel)
			throws JSONException {
		ArrayList<ExamAnswer> model = new ArrayList<ExamAnswer>();
		int length = dataset.length();
		for (int i = 0; i < length; i++) {
			JSONObject json = dataset.getJSONObject(i);
			// datas.addModel(mAdapter.analysis(obj));
			JSONArray names = json.names();
			int cols = names.length();
			ExamAnswer bean = new ExamAnswer();
			for (int j = 0; j < cols; j++) {
				String name = names.get(j).toString();
				String value = json.getString(name);
				if ("id".equals(name.trim().toLowerCase())) {
					//System.out.println("add ExamAnswer id:" + value);
					bean.setId(value);
				} else if ("index".equals(name.trim().toLowerCase())) {
					//System.out.println("add ExamAnswer index:" + value);
				} else if ("flag".equals(name.trim().toLowerCase())) {
					
					if(1==json.getInt(name)){
						//System.out.println("add coAnswer id:");
						questionModel.setAnswerId(json.getString("id"));
					}
					//System.out.println("add ExamAnswer flag:" + value);
				} else if ("answer".equals(name.trim().toLowerCase())) {
					bean.setAnswerText(value);
				}
				// System.out.println("add photos to photo list");

			}
			model.add(bean);
		}
		return model;
	}

	@Override
	public ArrayList<ExamPaperModel> analysisList(JSONObject json)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}