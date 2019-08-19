package com.bujidao.dao;

import com.bujidao.entity.PersonInfo;

public interface PersonInfoDao {
	PersonInfo queryPersonInfoById(long userId);
	
	int insertPersonInfo(PersonInfo personInfo);
}	
