package com.bujidao.service;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bujidao.entity.PersonInfo;
import com.bujidao.entity.Shop;
import com.bujidao.entity.ShopAuthMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopAuthMapServiceTest {
	@Autowired
	private ShopAuthMapService shopAuthMapService;
	
	@Test
//	@Ignore
	public void testUpdate(){
		ShopAuthMap shopAuthMap=new ShopAuthMap();
		shopAuthMap.setLastEditTime(new Date());
		shopAuthMap.setEnableStatus(0);
		shopAuthMap.setTitleFlag(3);
		shopAuthMap.setShopAuthId(2L);
		shopAuthMapService.modifyShopAuthMap(shopAuthMap);
	}
	
}
