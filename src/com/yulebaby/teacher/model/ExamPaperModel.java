package com.yulebaby.teacher.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ExamPaperModel implements BaseModel, Serializable {
	private static final long serialVersionUID = 5415246824858152360L;
	/**
	 * 试题列表包括题目
	 * */
	private ArrayList<QuestionModel> questList;

	public ArrayList<QuestionModel> getQuestList() {
		return questList;
	}
	public void setQuestList(ArrayList<QuestionModel> questList) {
		this.questList = questList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCompletionTime() {
		return completionTime;
	}
	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime;
	}
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getQuestionNum() {
		return questionNum;
	}
	public void setQuestionNum(int questionNum) {
		this.questionNum = questionNum;
	}

	private String id;
	/** 完成状态 0:未完成 1：已完成 */
	private String status;
	private int score;
	private String createTime;
	private String completionTime;
	private int totalScore;
	private String userName;
	private int questionNum;
}
