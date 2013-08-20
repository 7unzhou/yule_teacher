package com.yulebaby.teacher.model;

import java.util.ArrayList;

public class SalaryMonthModel implements BaseModel {

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getBasicWage() {
		return basicWage;
	}
	public void setBasicWage(int basicWage) {
		this.basicWage = basicWage;
	}
	public int getInsured() {
		return insured;
	}
	public void setInsured(int insured) {
		this.insured = insured;
	}
	public int getCommission() {
		return commission;
	}
	public void setCommission(int commission) {
		this.commission = commission;
	}
	public int getAdd() {
		return add;
	}
	public void setAdd(int add) {
		this.add = add;
	}
	public int getSub() {
		return sub;
	}
	public void setSub(int sub) {
		this.sub = sub;
	}
	public int getOther() {
		return other;
	}
	public void setOther(int other) {
		this.other = other;
	}
	public int getDayMax() {
		return dayMax;
	}
	public void setDayMax(int dayMax) {
		this.dayMax = dayMax;
	}
	public ArrayList<DaySalary> getDayList() {
		return dayList;
	}
	public void setDayList(ArrayList<DaySalary> dayList) {
		this.dayList = dayList;
	}
	private String userName;
	private String level;
	private String month;
	/** 最终实际发放工资 */
	private int total;
	private int basicWage;
	private int insured;

	/** 提成汇总 */
	private int commission;
	/** 奖励汇总 */
	private int add;
	/** 考勤汇总 */
	private int sub;
	/** 其他汇总 */
	private int other;
	private int dayMax;
	private ArrayList<DaySalary> dayList;

}
