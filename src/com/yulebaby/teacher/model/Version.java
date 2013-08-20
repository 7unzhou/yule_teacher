package com.yulebaby.teacher.model;

public class Version implements BaseModel {

	private String versionCode;
	private boolean force;
	private String url;
	private String version;
	
	
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public boolean isForce() {
		return force;
	}
	public void setForce(String force) {
		if("0".equals(force.trim())){
			this.force = false;
		}else{
			this.force = true;
		}
	}
	public void setForce(boolean force) {
		this.force = force;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
