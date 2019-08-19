package com.bujidao.dao;

import java.util.Date;
import java.util.List;

import org.apache.catalina.User;
import org.assertj.core.data.Percentage;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bujidao.entity.Award;
import com.bujidao.entity.PersonInfo;
import com.bujidao.entity.Shop;
import com.bujidao.entity.UserAwardMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAwardMapDaoTest {
	@Autowired
	private UserAwardMapDao userAwardMapDao;
	
	@Test
	@Ignore
	public void testQueryList(){
		UserAwardMap userAwardMap=new UserAwardMap();
//		Shop shop=new Shop();
//		shop.setShopId(3L);
//		userAwardMap.setShop(shop);
//		Award award=new Award();
//		award.setAwardName("低级");
//		userAwardMap.setAward(award);
//		PersonInfo user=new PersonInfo();
//		user.setUserId(1L);
//		PersonInfo operator=new PersonInfo();
//		operator.setName("2");
//		userAwardMap.setUser(user);
//		userAwardMap.setOperator(operator);
//		userAwardMap.setUsedStatus(1);
		List<UserAwardMap> list=
				userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 10);
		UserAwardMap awardMap=list.get(0);
		System.out.println("顾客是"+awardMap.getUser().getName());
		System.out.println("操作员是"+awardMap.getOperator().getName());
		System.out.println(userAwardMapDao.queryUserAwardMapCount(userAwardMap));
	}
	
	@Test
	@Ignore
	public void testQueryById(){
		System.out.println(userAwardMapDao.queryUserMapById(2L));
	}
	
	@Test
	@Ignore
	public void testInsertMap(){
		UserAwardMap userAwardMap=new UserAwardMap();
		Shop shop=new Shop();
		shop.setShopId(3L);
		Award award=new Award();
		award.setAwardId(3L);
		PersonInfo user=new PersonInfo();
		user.setUserId(2L);
		userAwardMap.setUsedStatus(1);
		userAwardMap.setCreateTime(new Date());
		userAwardMap.setPoint(99);
		userAwardMap.setAward(award);
		userAwardMap.setShop(shop);
		userAwardMap.setUser(user);
		userAwardMap.setUsedStatus(1);
		userAwardMapDao.insertUserAwardMap(userAwardMap);
	}
	
	@Test
	public void testUpdata(){
		UserAwardMap userAwardMap=new UserAwardMap();
		userAwardMap.setUserAwardId(2L);
		PersonInfo user=new PersonInfo();
		user.setUserId(3L);
		userAwardMap.setUsedStatus(0);
		userAwardMap.setUser(user);
		userAwardMap.setPoint(9999);
		userAwardMap.setCreateTime(new Date());
		userAwardMapDao.updateUserAwardMap(userAwardMap);
	}
}
