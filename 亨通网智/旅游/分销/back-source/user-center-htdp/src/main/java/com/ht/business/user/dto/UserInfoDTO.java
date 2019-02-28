package com.ht.business.user.dto;


import com.ht.common.annotations.dto.AddColumn;
import com.ht.common.annotations.dto.EntityTableName;
import com.ht.common.annotations.dto.QueryColumn;
import com.ht.common.annotations.dto.QueryTable;
import com.ht.common.annotations.dto.QueryType;
import com.ht.common.annotations.dto.UpdateColumn;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author qiujunda
 * @date 2019年1月2日
 * @description
 */
@EntityTableName(entityName="com.ht.business.user.entity.UserInfo")
@QueryTable(tableName="user_info")
public class UserInfoDTO {

	@AddColumn
	@QueryColumn(QueryType.LIKE)
	@ApiModelProperty(value = "账号")
	public String id;//账号
	
	@QueryColumn(QueryType.EQ)
	public Integer status;//1正常2锁住了
	
	@AddColumn
	@UpdateColumn
	@QueryColumn(value=QueryType.LIKE,column="")
	@ApiModelProperty(value = "姓名")
	public String userName;//姓名
	
	@AddColumn
	@ApiModelProperty(value = "密码")
	public String passWord;//密码
	
	@AddColumn
	@UpdateColumn
	@QueryColumn(QueryType.EQ)
	@ApiModelProperty(value = "性别")
	public String userSex;//姓名
	
	@AddColumn
	@UpdateColumn
	@QueryColumn(QueryType.LIKE)
	@ApiModelProperty(value = "出生日期yyyy-mm-dd")
	public String birthday;//出生日期yyyy-mm-dd
	
	@AddColumn
	@UpdateColumn
	@QueryColumn(QueryType.LIKE)
	@ApiModelProperty(value = "家庭电话")
	public String homePhone;//家庭电话
	
	@AddColumn
	@UpdateColumn
	@QueryColumn(QueryType.LIKE)
	@ApiModelProperty(value = "工作电话")
	public String workPhone;//工作电话
	
	@AddColumn
	@UpdateColumn
	@QueryColumn(QueryType.LIKE)
	@ApiModelProperty(value = "邮箱")
	public String userEmail;//邮箱
	
	@AddColumn
	@UpdateColumn
	@QueryColumn(QueryType.LIKE)
	@ApiModelProperty(value = "家庭住址")
	public String homeAddress;//家庭住址
	
	@AddColumn
	@UpdateColumn
	@QueryColumn(QueryType.LIKE)
	@ApiModelProperty(value = "用户昵称")
	public String userNickName;//用户昵称
	
	@AddColumn
	@UpdateColumn
	@ApiModelProperty(value = "用户头像url地址")
	public String userHeaderUrl;//用户头像url地址

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
	
	
	
}
