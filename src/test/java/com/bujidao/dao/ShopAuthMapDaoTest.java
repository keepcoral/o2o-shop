package com.bujidao.dao;

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
public class ShopAuthMapDaoTest {
	@Autowired
	private ShopAuthMapDao shopAuthMapDao;
	
	@Test
	public void testQueryList(){
		System.out.println(shopAuthMapDao.queryShopAuthMapListByShopId(3L, 0, 100));
		System.out.println(shopAuthMapDao.queryShopAuthMapCountByShopId(3L));
	}
	
	@Test
	public void testQueryById(){
		System.out.println(shopAuthMapDao.queryShopAuthMapById(1L));
	}
	
	@Test
//	@Ignore
	public void testInsert(){
		ShopAuthMap shopAuthMap=new ShopAuthMap();
		shopAuthMap.setCreateTime(new Date());
		shopAuthMap.setLastEditTime(new Date());
		shopAuthMap.setEnableStatus(1);
		shopAuthMap.setTitle("店家");
		shopAuthMap.setTitleFlag(0);
		Shop shop=new Shop();
		shop.setShopId(3L);
		PersonInfo employee=new PersonInfo();
		employee.setUserId(2L);
		shopAuthMap.setEmployee(employee);
		shopAuthMap.setShop(shop);
		shopAuthMapDao.insertShopAuthMap(shopAuthMap);
	}
	
	@Test
	@Ignore
	public void testUpdate(){
		ShopAuthMap shopAuthMap=new ShopAuthMap();
		shopAuthMap.setLastEditTime(new Date());
		shopAuthMap.setEnableStatus(0);
		shopAuthMap.setTitleFlag(3);
		shopAuthMap.setShopAuthId(2L);
		shopAuthMapDao.updateShopAuthMap(shopAuthMap);
	}
	
	@Test
	@Ignore
	public void testDelete(){
		shopAuthMapDao.deleteShopAuthMap(1L);
	}
}
