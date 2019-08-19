package com.bujidao.service;

import com.bujidao.dto.WechatAuthExecution;
import com.bujidao.entity.WechatAuth;
import com.bujidao.exception.WechatAuthOperationException;

public interface WechatAuthService {
	/**
	 * 通过openId查询微信账号
	 */
	WechatAuth getWechatAuthById(String openId);
	
	/**
	 * 注册本平台微信账号
	 */
	WechatAuthExecution regist(WechatAuth wechatAuth) 
			throws WechatAuthOperationException;
}
