package com.ht.business.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ht.business.user.entity.UserInfo;


/**
 * @author qiujunda
 * @date 2019年1月2日
 * @description
 */
public interface UserInfoDAO extends JpaRepository<UserInfo, String> {

}
