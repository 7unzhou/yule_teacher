package com.yulebaby.teacher.model;

public class ExamModel  implements BaseModel  {
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	public String getCompletionTime() {
		return completionTime;
	}
	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime;
	}
	public boolean isExaming() {
		return status;
	}
	public void isExaming(boolean status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	private String id;
	private String createTime;
	private int totalScore;
	private String completionTime;
	private boolean status;
	private String name;
	private int score;
	private String type;
	
	
	
	/*public String date;
	public String title;
	public String state;
	public String score;
	public boolean isExaming;

	public ExamModel(String title, String date, String state, String score) {
		this.date = date;
		this.title = title;
		this.score = score;
		this.state = state;
		if (state.equals("未作答")) {
			this.isExaming = true;
		} else {
			this.isExaming = false;
		}
	}*/
}
