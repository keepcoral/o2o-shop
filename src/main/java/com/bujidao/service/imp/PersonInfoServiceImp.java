package com.bujidao.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bujidao.dao.PersonInfoDao;
import com.bujidao.entity.PersonInfo;
import com.bujidao.service.PersonInfoService;

@Service
public class PersonInfoServiceImp implements PersonInfoService{

	@Autowired
	private PersonInfoDao personInfoDao;
	
	@Override
	public PersonInfo getPersonInfo(Long userId) {
		return personInfoDao.queryPersonInfoById(userId);
	}

}
