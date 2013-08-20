package com.yulebaby.teacher.model;

public class Login implements BaseModel {

	private String id;
	private String name;
	private String remainTimes;
	private String imgUrl;
	private UserAuth auth;

	public boolean isEmpty() {
		if (name == null || null == imgUrl || null == auth
				|| remainTimes == null)
			return true;
		return false;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public UserAuth getAuth() {
		return auth;
	}

	public void setAuth(UserAuth auth) {
		this.auth = auth;
	}

	public boolean checkRemainIsNum() {
		boolean is = false;
		try {
			Integer.valueOf(remainTimes);
			is = true;
		} catch (Exception e) {
			is = false;
		}
		return is;
	}

	public String getRemainTimes() {
		return remainTimes;
	}

	public void setRemainTimes(String remainTimes) {
		this.remainTimes = remainTimes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
