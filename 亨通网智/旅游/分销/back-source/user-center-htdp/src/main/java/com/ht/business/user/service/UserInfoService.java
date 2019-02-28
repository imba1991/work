package com.ht.business.user.service;

import com.ht.business.user.dto.UserInfoDTO;
import com.ht.business.user.entity.UserInfo;
import com.ht.common.service.BaseService;

/**
 * @author qiujunda
 * @date 2019年1月2日
 * @description
 */
public interface UserInfoService extends BaseService<UserInfoDTO, UserInfo, String>{

	
	/**
	 * @description 修改用户密码，根据老密码和新密码一起判断是否能修改
	 */
	public void updatePasswd(String account,String oldPasswd,String newPasswd);
	
	
}
