package com.bujidao.dao;

import java.util.Calendar;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bujidao.entity.Product;
import com.bujidao.entity.ProductSellDaily;
import com.bujidao.entity.Shop;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductSellDailyTest {
	@Autowired
	private ProductSellDailyDao productSellDailyDao;
	
	
	@Test
	public void testQueryList(){
		Product product=new Product();
//		product.setProductName("抹茶");
		Shop shop=new Shop();
		shop.setShopId(3L);
		ProductSellDaily dailyCondition=new ProductSellDaily();
		dailyCondition.setShop(shop);
		Calendar calendar=Calendar.getInstance();
		//获取昨天日期
		calendar.add(Calendar.DATE, -1);
		Date endTime=calendar.getTime();
		//获取7天前日期
		calendar.add(Calendar.DATE, -6);
		Date beginTime=calendar.getTime();
		System.out.println(productSellDailyDao.queryProductSellDailyList(dailyCondition,beginTime, endTime));
	}
	
	@Test
	@Ignore
	public void testInsert(){
		productSellDailyDao.insertProductSellDaily();
	}
	
	@Test
	public void testInsertDefault(){
		productSellDailyDao.insertDefaultProductSellDaily();
	}
}
