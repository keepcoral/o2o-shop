package com.bujidao.dao;

import java.util.Date;

import org.apache.catalina.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bujidao.entity.PersonInfo;
import com.bujidao.entity.Shop;
import com.bujidao.entity.UserShopMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserShopMapTest {
	@Autowired
	private UserShopMapDao userShopMapDao;
	
	@Test
	@Ignore
	public void testQueryList(){
		UserShopMap userShopMapCondition=new UserShopMap();
		Shop shop=new Shop();
		shop.setShopId(3L);
		shop.setShopName("精品");
		PersonInfo user=new PersonInfo();
		user.setUserId(1L);
		userShopMapCondition.setUser(user);
		userShopMapCondition.setShop(shop);
		System.out.println(userShopMapDao.queryUserShopMapList(userShopMapCondition,0,1));
		System.out.println(userShopMapDao.queryUserShopMapCount(userShopMapCondition));
	}
	
	@Test
	public void testQueryById(){
		System.out.println(userShopMapDao.queryUserShopMap(1, 3));
	}
	
	@Test
	@Ignore
	public void testInsert(){
		Shop shop=new Shop();
		shop.setShopId(3L);
		PersonInfo user=new PersonInfo();
		user.setUserId(1L);
		UserShopMap userShopMap=new UserShopMap();
		userShopMap.setCreateTime(new Date());
		userShopMap.setShop(shop);
		userShopMap.setUser(user);
		userShopMap.setPoint(100);
		userShopMapDao.insertUserShopMap(userShopMap);
	}
	
	@Test
//	@Ignore
	public void testUpdate(){
		UserShopMap userShopMap=new UserShopMap();
		userShopMap.setPoint(8888);
		PersonInfo user=new PersonInfo();
		user.setUserId(1L);
		Shop shop=new Shop();
		shop.setShopId(3L);
		userShopMap.setUser(user);
		userShopMap.setShop(shop);
		userShopMapDao.updateUserShopMapPoint(userShopMap);
	}
}
