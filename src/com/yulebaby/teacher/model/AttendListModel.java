package com.yulebaby.teacher.model;

import java.util.ArrayList;

public class AttendListModel implements BaseModel {
	private String title;
	private int totalWages;
	public int getTotalWages() {
		return totalWages;
	}
	public void addTotalWages(int totalWages) {
		this.totalWages = this.totalWages + totalWages;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ArrayList<AttendModel> getList() {
		return list;
	}
	public void setList(ArrayList<AttendModel> list) {
		this.list = list;
	}
	private ArrayList<AttendModel> list;
}
