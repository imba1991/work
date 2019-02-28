package com.ht.business.user.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * @author qiujunda
 * @date 2019年1月2日
 * @description
 */
@Entity
@Table(name = "user_info", uniqueConstraints = {})
public class UserInfo {
	@Id
	@Column(unique = true, nullable = false, length=32,columnDefinition="varchar(32) comment '主键'")
	public String id;//账号
	
	@Column( length = 1,columnDefinition="int(1) comment '1正常2锁住了'")
	public Integer status=1;//1正常2锁住了
	
	@Column( length = 1,columnDefinition="int(1) comment '0未删除1已删除'")
	public Integer deleted=0;
	
	@Column( length = 32,columnDefinition="varchar(32) comment '姓名'")
	public String userName;//姓名
	
	@Column( length = 64,columnDefinition="varchar(128) comment '密码'")
	public String passWord;//密码
	
	@Column( length = 32,columnDefinition="varchar(32) comment '性别'")
	public String userSex;//姓名
	
	@Column( length = 16,columnDefinition="varchar(16) comment '生日'")
	public String birthday;//出生日期yyyy-mm-dd
	
	@Column( length = 16,columnDefinition="varchar(16) comment '家庭电话'")
	public String homePhone;//家庭电话
	
	@Column( length = 32,columnDefinition="varchar(32) comment '工作电话'")
	public String workPhone;//工作电话
	
	@Column( length = 64,columnDefinition="varchar(64) comment '邮箱'")
	public String userEmail;//邮箱
	
	@Column( length = 64,columnDefinition="varchar(64) comment '家庭住址'")
	public String homeAddress;//家庭住址
	
	@Column( length = 32,columnDefinition="varchar(32) comment '用户昵称'")
	public String userNickName;//用户昵称
	
	@Column( length = 2000,columnDefinition="varchar(2000) comment '头像url地址'")
	public String userHeaderUrl;//用户头像url地址
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column( length = 7,columnDefinition="timestamp null default null comment '新增时间'")
	public Date addTime=new Date();//创建时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column( length = 7,columnDefinition="timestamp null default null comment '最近修改时间'")
	public Date updateTime;//最新修改时间



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public String getUserHeaderUrl() {
		return userHeaderUrl;
	}

	public void setUserHeaderUrl(String userHeaderUrl) {
		this.userHeaderUrl = userHeaderUrl;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	
}

