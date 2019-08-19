package com.bujidao.dao;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bujidao.entity.PersonInfo;
import com.bujidao.entity.WechatAuth;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthDaoTest {
	@Autowired
	private WechatAuthDao wechatAuthDao;
	
	@Test
	@Ignore
	public void testQueryByOpenId(){
		System.out.println();
		wechatAuthDao.queryWechatAuthByOpenId("abcccccdddee");
	}
	
	@Test
	public void testInsert(){
		WechatAuth auth=new WechatAuth();
		PersonInfo info=new PersonInfo();
		info.setUserId(1L);
		auth.setPersonInfo(info);
		auth.setOpenId("aaa");
		wechatAuthDao.insertWechatAuth(auth);
	}
}
