package com.bujidao.service.imp;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bujidao.dao.PersonInfoDao;
import com.bujidao.dao.WechatAuthDao;
import com.bujidao.dto.WechatAuthExecution;
import com.bujidao.entity.PersonInfo;
import com.bujidao.entity.WechatAuth;
import com.bujidao.enums.WechatAuthStateEnum;
import com.bujidao.exception.WechatAuthOperationException;
import com.bujidao.service.WechatAuthService;

@Service
public class WechatAuthServiceImp implements WechatAuthService {
	private Logger logger=LoggerFactory.getLogger(WechatAuthServiceImp.class); 
	 
	@Autowired
	private WechatAuthDao wechatAuthDao;

	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public WechatAuth getWechatAuthById(String openId) {
		return wechatAuthDao.queryWechatAuthByOpenId(openId);
	}

	@Override
	@Transactional
	public WechatAuthExecution regist(WechatAuth wechatAuth) throws WechatAuthOperationException {
		if(wechatAuth==null||wechatAuth.getOpenId()==null){
			return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
		}
		try{
			wechatAuth.setCreateTime(new Date());
			//微信有PersonInfo对象，且userId为空，那么证明该微信账号为第一次登陆平台
			if(wechatAuth.getPersonInfo()!=null
						&&wechatAuth.getPersonInfo().getUserId()==null){
				try{
					wechatAuth.getPersonInfo().setCreateTime(new Date());
					wechatAuth.getPersonInfo().setLastEditTime(new Date());
					PersonInfo personInfo=wechatAuth.getPersonInfo();
//					System.out.println("创建用户的名字为"+personInfo.getName());
					int effectedNum=personInfoDao.insertPersonInfo(personInfo);
					wechatAuth.setPersonInfo(personInfo);
					if(effectedNum<=0){
						//添加用户失败
						throw new WechatAuthOperationException("添加用户失败！");
					}
				}catch (Exception e) {
					throw new WechatAuthOperationException("InsertPersonInfo error:"+e.getMessage());
				}
			}
			
			int effectedNum=wechatAuthDao.insertWechatAuth(wechatAuth);
			if(effectedNum<=0){
				//添加用户失败
				throw new WechatAuthOperationException("注册微信用户失败！");
			}else{
				return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS,wechatAuth);
			}
		}catch (Exception e) {
			throw new WechatAuthOperationException("WechatAutherrMsg:"+e.getMessage());
		}
	}

}
