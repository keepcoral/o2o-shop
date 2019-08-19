package com.bujidao.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bujidao.entity.UserProductMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserProductMapServiceTest {
	
	@Autowired
	private UserProductMapService userProductMapService;
	
	@Test
	public void testQueryList(){
		System.out.println(userProductMapService.getUserProductMapList(new UserProductMap(), 0, 100));
		
	}
}
