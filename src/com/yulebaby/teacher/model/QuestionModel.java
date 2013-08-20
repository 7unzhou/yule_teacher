package com.yulebaby.teacher.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.yulebaby.teacher.adapter.AnswerAdapter;

public class QuestionModel implements Serializable{
	private ArrayList<ExamAnswer> answers;
	public ArrayList<ExamAnswer> getAnswers() {
		return answers;
	}
	public void setAnswers(ArrayList<ExamAnswer> answers) {
		this.answers = answers;
	}
	public String getStrQuestion() {
		return strQuestion;
	}
	public void setStrQuestion(String strQuestion) {
		this.strQuestion = strQuestion;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	private String strQuestion;
	private String id;
	private String answerId;
	private String selectedId;
	public String getSelectedId() {
		return selectedId;
	}
	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}
	public String getAnswerId() {
		return answerId;
	}
	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}
	//private String answerId;
	private int score;
	// public QuestionModel(String strQ, ArrayList<String> aList) {
	// this.answers = aList;
	// this.strQuestion = strQ;
	// }
}
