package com.hy.ly.entity;

import java.util.Date;

public class LoginLog {

	private int logId;
	private int userId;
	private String ip;
	private Date loginTime;

	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	

	public LoginLog() {
		super();
	}

	public int getLogId() {
		return logId;
	}

	public void setLoginId(int logId) {
		this.logId = logId;
	}

	@Override
	public String toString() {
		return "LoginLog [loginId=" + logId + ", userId=" + userId + ", ip=" + ip + ", loginTime=" + loginTime + "]";
	}

	public LoginLog(int logId, int userId, String ip, Date loginTime) {
		super();
		this.logId = logId;
		this.userId = userId;
		this.ip = ip;
		this.loginTime = loginTime;
	}

	

}
