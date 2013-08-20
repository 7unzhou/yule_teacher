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
import com.yulebaby.teacher.model.ExamReturnModel;
import com.yulebaby.teacher.model.QuestionModel;
import com.yulebaby.teacher.model.YPhoto;

public class ExamReturnAdapter implements ModelAdapter<ExamReturnModel> {

	@Override
	public ExamReturnModel analysis(JSONObject json) throws JSONException {
		Log.d(App.TAG, "ExamReturnAdapter analysis json = " + json);
		ExamReturnModel bean = new ExamReturnModel();
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
		if(json.has("correctNum")){
			bean.setCorrectNum(json.getInt("correctNum"));
			//bean.setCreateTime(json.getString("createTime"));
		}
		if(json.has("errorNum")){
			bean.setErrorNum(json.getInt("errorNum"));
			
			//bean.setCompletionTime(json.getString("completionTime"));
		}
		if(json.has("totalScore")){
			bean.setTotalScore(json.getInt("totalScore"));
			//bean.setTotalScore(json.getInt("totalScore"));
		}
		

		return bean;
	}





	@Override
	public ArrayList<ExamReturnModel> analysisList(JSONObject json)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}