package com.ht.business.user.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ht.business.user.dao.UserInfoDAO;
import com.ht.business.user.dto.UserInfoDTO;
import com.ht.business.user.entity.UserInfo;
import com.ht.business.user.service.UserInfoService;
import com.ht.common.dynamicquery.DynamicQuery;
import com.ht.common.exception.HtRunningException;
import com.ht.common.page.Page;
import com.ht.common.util.dto.DtoUtil;
import com.ht.util.coder.MD5Util;

/**
 * @author qiujunda
 * @date 2019年1月2日
 * @description
 */
@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService{
	@Autowired
	private DtoUtil dtoUtil;
	@Autowired
	private UserInfoDAO userInfoDAO;
	@Autowired
	private DynamicQuery dynamicQuery;
	
	@Override
	public UserInfo getById(String id) {
		// TODO Auto-generated method stub
		UserInfo userInfo=userInfoDAO.findOne(id);
		if(userInfo==null||userInfo.getDeleted()==1) {
			throw new HtRunningException("用户不存在");
		}
		return userInfo;
	}
	@Override
	public void deleteById(String id)  {
		// TODO Auto-generated method stub
		UserInfo userInfo=userInfoDAO.findOne(id);
		if(userInfo==null||userInfo.getDeleted()==1) {
			throw new HtRunningException(id+"用户不存在");
		}
		userInfo.setDeleted(1);
		userInfoDAO.saveAndFlush(userInfo);
	}
	@Override
	public void deleteByIds(String[] ids) {
		// TODO Auto-generated method stub
		if(ids==null||ids.length<1) {
			throw new HtRunningException("待删除用户不能为空");
		}
		for(int i=0;i<ids.length;i++) {
			String account=ids[i];
			System.out.println(account);
			if(StringUtils.isNotBlank(account)) {
				deleteById(account);
			}
		}
		
	}
	@Override
	public UserInfo add(UserInfoDTO dto)  {
		UserInfo userInfo=userInfoDAO.findOne(dto.getId());
		if(userInfo!=null) {
			throw new HtRunningException("用户已经存在");
		}
		userInfo=new UserInfo();
//		System.out.println(userInfo.getId()+"-"+userInfo.getUserName()+"-"+userInfo.getPassWord());
		dtoUtil.copy(dto, userInfo, null, DtoUtil.ADD);
		userInfo.setPassWord(MD5Util.MD5(userInfo.getPassWord()));
		userInfo.setUpdateTime(new Date());
		System.out.println(userInfo.getId()+"-"+userInfo.getUserName()+"-"+userInfo.getPassWord());
		userInfo=userInfoDAO.saveAndFlush(userInfo);
		return userInfo;
	}
	@Override
	public UserInfo update(UserInfoDTO dto)  {
		UserInfo userInfo=userInfoDAO.findOne(dto.getId());
		if(userInfo==null) {
			throw new HtRunningException("用户不存在");
		}
		System.out.println(userInfo.getId()+"-"+userInfo.getUserName()+"-"+userInfo.getPassWord());
		dtoUtil.copy(dto, userInfo, null, DtoUtil.UPDATE);
		userInfo.setPassWord(MD5Util.MD5(userInfo.getPassWord()));
		userInfo.setUpdateTime(new Date());
		userInfo=userInfoDAO.saveAndFlush(userInfo);
		return userInfo;
	}
	@Override
	public Page<UserInfo> queryPage(Page<UserInfo> page, UserInfoDTO dto)  {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<UserInfo> query(UserInfoDTO dto)  {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Long queryCount(UserInfoDTO dto)  {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void updatePasswd(String account, String oldPasswd, String newPasswd) {
		// TODO Auto-generated method stub
		UserInfo userInfo=userInfoDAO.findOne(account);
		if(userInfo==null||userInfo.getDeleted()==1) {
			throw new HtRunningException("用户不存在");
		}
		if(oldPasswd.equals(newPasswd)) {
			throw new HtRunningException("新密码和老密码不能一样");
		}
		oldPasswd=MD5Util.MD5(oldPasswd);
		if(!oldPasswd.equals(userInfo.getPassWord())) {
			throw new HtRunningException("原密码错误");
		}
		newPasswd=MD5Util.MD5(newPasswd);
		userInfo.setPassWord(newPasswd);
		userInfo.setUpdateTime(new Date());
		userInfoDAO.saveAndFlush(userInfo);
	}
	
	
	
}
