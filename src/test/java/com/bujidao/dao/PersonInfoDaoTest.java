package com.bujidao.dao;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bujidao.entity.PersonInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonInfoDaoTest {
	@Autowired
	private PersonInfoDao personInfoDao;
	
	@Test
	public void testQueyById(){
		System.out.println(personInfoDao.queryPersonInfoById(1L));
	}
	
	@Test
	public void testInsert(){
		PersonInfo info=new PersonInfo();
		info.setName("布吉岛");
		info.setCreateTime(new Date());
		info.setEmail("123456@qq.com");
		info.setLastEditTime(new Date());
		info.setEnableStatus(1);
		System.out.println("id为"+info.getUserId());
		personInfoDao.insertPersonInfo(info);
		System.out.println("id为"+info.getUserId());
	}
}
