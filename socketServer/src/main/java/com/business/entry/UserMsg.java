
package com.business.entry;

import com.result.base.entry.Base.BaseSocketMessage;

import java.io.Serializable;


/**
 * ClassName:User 
 * Function: TODO ADD FUNCTION. 
 * Date:     2017年9月4日 上午11:38:23 
 * @author   HXY	 
 */
public class UserMsg extends BaseSocketMessage implements Serializable{

	private static final long serialVersionUID = 7917731633090079679L;

	private String userId;

	private String userName;

	private boolean sex;

	private String openId;

	private String createTime;

	private String phoneNum;	//手机号码

	private String userImg; 	//用户头像

	private String introduct;   //个人说明

	public UserMsg() {
		super();
	}

	public UserMsg(String clientUri, String serverUri, String userId, String userName, boolean sex, String openId, String createTime, String phoneNum, String userImg, String introduct) {
		super(clientUri, serverUri);
		this.userId = userId;
		this.userName = userName;
		this.sex = sex;
		this.openId = openId;
		this.createTime = createTime;
		this.phoneNum = phoneNum;
		this.userImg = userImg;
		this.introduct = introduct;
	}

	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getIntroduct() {
		return introduct;
	}

	public void setIntroduct(String introduct) {
		this.introduct = introduct;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", sex=" + sex + ", openId=" + openId
				+ ", createTime=" + createTime + ", phoneNum=" + phoneNum + ", userImg=" + userImg + ", introduct="
				+ introduct + "]";
	}
	
}

