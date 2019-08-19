package com.bujidao.dao;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bujidao.entity.PersonInfo;
import com.bujidao.entity.Product;
import com.bujidao.entity.Shop;
import com.bujidao.entity.UserProductMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserProductMapDaoTest {
	
	@Autowired
	private UserProductMapDao userProductMapDao;
	
	@Test
	public void testQueryList(){
		UserProductMap userProductMap=new UserProductMap();
		PersonInfo user=new PersonInfo();
		user.setUserId(1L);
		userProductMap.setUser(user);
		Product product=new Product();
		product.setProductName("拿铁");
		userProductMap.setProduct(product);
		System.out.println(
				userProductMapDao.queryUserProductMapList(userProductMap, 0, 100));
		System.out.println(userProductMapDao.queryUserProductMapCount(userProductMap));
	}
	
	@Test
	@Ignore
	public void testInsert(){
		UserProductMap userProductMap=new UserProductMap();
		Shop shop=new Shop();
		shop.setShopId(9L);
		userProductMap.setShop(shop);
		PersonInfo user=new PersonInfo();
		user.setUserId(1L);
		PersonInfo operator=new PersonInfo();
		operator.setUserId(3L);
		Product product=new Product();
		product.setProductId(15L);
		userProductMap.setUser(user);
		userProductMap.setOperator(operator);
		userProductMap.setShop(shop);
		userProductMap.setCreateTime(new Date());
		userProductMap.setProduct(product);
		userProductMapDao.insertUserProductMap(userProductMap);
	}
}
