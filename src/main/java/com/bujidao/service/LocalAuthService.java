package com.bujidao.service;


import com.bujidao.dto.LocalAuthExecution;
import com.bujidao.entity.LocalAuth;
import com.bujidao.exception.LocalAuthOperationException;

public interface LocalAuthService {
	/**
	 * 根据用户名和密码查询账号信息
	 */
	LocalAuth getLocalAuthByUsernameAndPwd(String username,String password);

	/**
	 * 根据userId查询账号信息
	 */
	LocalAuth getLocalAuthByUserId(long userId);

	/**
	 * 绑定微信生成平台账号
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth) 
			throws LocalAuthOperationException;

	/**
	 * 修改密码
	 */
	LocalAuthExecution modifyLocalAuthPassword(Long userId,
			String username,String password,String newPassword) 
					throws LocalAuthOperationException;
	


}
