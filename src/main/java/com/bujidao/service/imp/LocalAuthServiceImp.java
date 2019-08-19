package com.bujidao.service.imp;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bujidao.dao.LocalAuthDao;
import com.bujidao.dto.LocalAuthExecution;
import com.bujidao.entity.LocalAuth;
import com.bujidao.enums.LocalAuthStateEnum;
import com.bujidao.exception.LocalAuthOperationException;
import com.bujidao.service.LocalAuthService;
import com.bujidao.util.MD5;

@Service
public class LocalAuthServiceImp implements LocalAuthService {
	@Autowired
	private LocalAuthDao localAuthDao;

	@Override
	public LocalAuth getLocalAuthByUsernameAndPwd(String username, String password) {
		return localAuthDao.queryLocalByUserNameAndPwd(username, MD5.getMd5(password));
	}

	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		return localAuthDao.queryLocalByUserId(userId);
	}

	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuthPassword(Long userId, String username,
			String password, String newPassword) 
					throws LocalAuthOperationException {
		if(userId==null||username==null||
				password==null||newPassword==null||password.equals(newPassword)){
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		try{
			int effectNum=localAuthDao.updateLocalAuth(userId, username, 
					MD5.getMd5(password), MD5.getMd5(newPassword), new Date());
			if(effectNum<=0){
				throw new LocalAuthOperationException("修改密码失败！");
			}else{
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
			}
		}catch (Exception e) {
			throw new LocalAuthOperationException("异常:"+e.getMessage());
		}
	}

	@Override
	@Transactional
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) 
			throws LocalAuthOperationException {
		//空值判断
		if (localAuth == null || localAuth.getPassword() == null || localAuth.getUsername() == null
				|| localAuth.getPersonInfo() == null || localAuth.getPersonInfo().getUserId() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		//判断是否已经绑定过
		LocalAuth tempLocalAuth=localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
		if(tempLocalAuth!=null){
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		}
		try{
			//没绑定过账号，创建一个平台账号绑定
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			int effectNum=localAuthDao.insertLocalAuth(localAuth);
			if(effectNum<=0){
				throw new LocalAuthOperationException("绑定账号失败！");
			}else{
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,localAuth);
			}
		}catch (Exception e) {
			throw new LocalAuthOperationException("绑定失败："+e.getMessage());
		}
	}

}
