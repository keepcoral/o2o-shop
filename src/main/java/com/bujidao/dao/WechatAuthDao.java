package com.bujidao.dao;

import com.bujidao.entity.WechatAuth;
import org.springframework.stereotype.Repository;

public interface WechatAuthDao {
	/**
	 * 通过openId查询本平台的微信账号
	 */
	WechatAuth queryWechatAuthByOpenId(String openId);
	
	/**
	 * 添加对应本平台的微信账号
	 */
	int insertWechatAuth(WechatAuth wechatAuth);
}
