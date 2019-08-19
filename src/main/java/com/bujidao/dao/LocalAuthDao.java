package com.bujidao.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.bujidao.entity.LocalAuth;

public interface LocalAuthDao {
	/**
	 * 通过账号和密码查询相应信息，登陆用
	 */
	LocalAuth queryLocalByUserNameAndPwd
		(@Param("username") String username,@Param("password") String password);
	
	/**
	 * 通过userId查询相应的localauth
	 */
	LocalAuth queryLocalByUserId(@Param("userId") long userId);
	
	/**
	 * 添加账号
	 */
	int insertLocalAuth(LocalAuth localAuth);
	
	/**
	 * 通过userId,username,password更改密码
	 */
	int updateLocalAuth(@Param("userId")Long userId,@Param("username")String username,
			@Param("password")String password,@Param("newPassword")String newPassword,
			@Param("lastEditTime")Date lastEditTime);
}
