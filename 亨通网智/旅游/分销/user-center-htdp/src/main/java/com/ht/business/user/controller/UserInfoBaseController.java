package com.ht.business.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ht.business.user.dto.UserInfoDTO;
import com.ht.business.user.service.UserInfoService;
import com.ht.common.response.Result;

import io.swagger.annotations.Api;

/**
 * @author qiujunda
 * @date 2019年1月3日
 * @description
 */
@RestController
@CrossOrigin
@RequestMapping("/user/base")
@Api(value = "UserInfoBaseController", description = "用户基本接口UserInfoBaseController")
public class UserInfoBaseController {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserInfoService userInfoService;
	
	@PostMapping("/add")
	public String addUser(@RequestBody UserInfoDTO dto) {
		System.out.println(dto.getId()+"-"+dto.getUserName()+"-"+dto.getPassWord());
		return Result.successObj(userInfoService.add(dto), new String[]{"passWord"});
	}
	@PostMapping("/update")
	public String updateUser(@RequestBody UserInfoDTO dto) {
		return Result.successObj(userInfoService.update(dto), new String[]{"passWord"});
	}
	@PostMapping("/delete")
	public String deleteUsers(@RequestBody String[] accounts) {
		userInfoService.deleteByIds(accounts);
		return Result.success();
	}
	@PostMapping("/updatePasswd")
	public String updatePasswd(String account,String oldPasswd,String newPasswd) {
		userInfoService.updatePasswd(account, oldPasswd, newPasswd);
		return Result.success();
	}
	@PostMapping("/queryPage")
	public String queryPageUsers(UserInfoDTO dto) {
		return Result.success();
	}
}
