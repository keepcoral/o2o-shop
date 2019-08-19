package com.bujidao.service;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bujidao.dto.WechatAuthExecution;
import com.bujidao.entity.PersonInfo;
import com.bujidao.entity.WechatAuth;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthServiceTest {
	@Autowired
	private WechatAuthService wechatAuthService;
	
	@Test
	@Ignore
	public void testQuery(){
		wechatAuthService.getWechatAuthById("1");
	}
	
	@Test
	public void testRegist(){
		WechatAuth auth=new WechatAuth();
		PersonInfo info=new PersonInfo();
		info.setName("微信插入测试用户");
		info.setEmail("111@163.com");
		info.setEnableStatus(1);
		info.setUserType(1);
		auth.setOpenId("test-test");
		auth.setCreateTime(new Date());
		auth.setPersonInfo(info);
		WechatAuthExecution we=wechatAuthService.regist(auth);
		System.out.println(we.getStateInfo());
	}
}
