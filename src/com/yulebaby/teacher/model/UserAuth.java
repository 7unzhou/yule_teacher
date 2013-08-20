package com.yulebaby.teacher.model;

public class UserAuth {
	private int bilingualAuth;
	private int bilingualChapter;
	private int cultureAuth;
	private int cultureChapter;
	private int waterAuth;
	private int waterChapter;
	private int sopAuth;
	private int sopChapter;
	private int reserveAuth;
	private int consumeAuth;
	private int photoAuth;
	private int attendanceAuth;
	private int payrollAuth;
	private int examAuth;
	private String sopTime;
	private boolean isBilingualUpdate;
	private boolean isCultureUpdate;
	private boolean isWaterUpdate;

	public boolean isBilingualUpdate() {
		return isBilingualUpdate;
	}
	public void setBilingualUpdate(boolean isBilingualUpdate) {
		this.isBilingualUpdate = isBilingualUpdate;
	}
	
	

	public boolean isCultureUpdate() {
		return isCultureUpdate;
	}
	public void setCultureUpdate(boolean isCultureUpdate) {
		this.isCultureUpdate = isCultureUpdate;
	}
	public boolean isWaterUpdate() {
		return isWaterUpdate;
	}
	public void setWaterUpdate(boolean isWaterUpdate) {
		this.isWaterUpdate = isWaterUpdate;
	}

	
	public int getBilingualAuth() {
		return bilingualAuth;
	}
	public void setBilingualAuth(int bilingualAuth) {
		this.bilingualAuth = bilingualAuth;
	}
	public int getBilingualChapter() {
		return bilingualChapter;
	}
	public void setBilingualChapter(int bilingualChapter) {
		this.bilingualChapter = bilingualChapter;
	}
	public int getCultureAuth() {
		return cultureAuth;
	}
	public void setCultureAuth(int cultureAuth) {
		this.cultureAuth = cultureAuth;
	}
	public int getCultureChapter() {
		return cultureChapter;
	}
	public void setCultureChapter(int cultureChapter) {
		this.cultureChapter = cultureChapter;
	}
	public int getWaterAuth() {
		return waterAuth;
	}
	public void setWaterAuth(int waterAuth) {
		this.waterAuth = waterAuth;
	}
	public int getWaterChapter() {
		return waterChapter;
	}
	public void setWaterChapter(int waterChapter) {
		this.waterChapter = waterChapter;
	}
	public int getSopAuth() {
		return sopAuth;
	}
	public void setSopAuth(int sopAuth) {
		this.sopAuth = sopAuth;
	}
	public int getSopChapter() {
		return sopChapter;
	}
	public void setSopChapter(int sopChapter) {
		this.sopChapter = sopChapter;
	}
	public int getReserveAuth() {
		return reserveAuth;
	}
	public void setReserveAuth(int reserveAuth) {
		this.reserveAuth = reserveAuth;
	}
	public int getConsumeAuth() {
		return consumeAuth;
	}
	public void setConsumeAuth(int consumeAuth) {
		this.consumeAuth = consumeAuth;
	}
	public int getPhotoAuth() {
		return photoAuth;
	}
	public void setPhotoAuth(int photoAuth) {
		this.photoAuth = photoAuth;
	}
	public int getAttendanceAuth() {
		return attendanceAuth;
	}
	public void setAttendanceAuth(int attendanceAuth) {
		this.attendanceAuth = attendanceAuth;
	}
	public int getPayrollAuth() {
		return payrollAuth;
	}
	public void setPayrollAuth(int payrollAuth) {
		this.payrollAuth = payrollAuth;
	}
	public int getExamAuth() {
		return examAuth;
	}
	public void setExamAuth(int examAuth) {
		this.examAuth = examAuth;
	}
	public String getSopTime() {
		return sopTime;
	}
	public void setSopTime(String sopTime) {
		this.sopTime = sopTime;
	}


}
