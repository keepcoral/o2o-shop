package com.bujidao.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bujidao.dto.ImageHolder;
import com.bujidao.dto.ShopExecution;
import com.bujidao.entity.Area;
import com.bujidao.entity.PersonInfo;
import com.bujidao.entity.Shop;
import com.bujidao.entity.ShopCategory;
import com.bujidao.enums.ShopStateEnum;
import com.bujidao.exception.ShopOperationException;
import com.bujidao.service.ShopService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceTest{
	@Autowired
	private ShopService shopService;
	
	@Test
	@Ignore
	public void testAddShop() throws FileNotFoundException{
		Area area=new Area();
		PersonInfo info=new PersonInfo();
		ShopCategory category=new ShopCategory();
		area.setAreaId(1);
		info.setUserId(1L);
		category.setShopCategoryId(1L);
		Shop shop=new Shop();
		shop.setArea(area);
		shop.setShopCategory(category);
		shop.setOwner(info);
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setShopName("service测试商店4");
		shop.setShopDesc("这是测试商店");
		shop.setAdvice("测试");
		File shopImg=new File("D:/IDM缓存/Image/2.jpg");
		InputStream is=new FileInputStream(shopImg);
//		System.out.println(file.length()/1024);
		ImageHolder thumbnail=
				new ImageHolder(shopImg.getName(),is);
		ShopExecution se=shopService.addShop(shop, thumbnail);
	}
	
	@Test
	public void testModifyShop() throws ShopOperationException, FileNotFoundException{
		Shop shop=new Shop();
		shop.setShopId(7L);
		shop.setShopDesc("咖啡可能非常劣质！");
//		File shopImg=new File("D:/IDM缓存/Image/6.jpg");
//		InputStream is=new FileInputStream(shopImg);
//		ImageHolder thumbnail=
//				new ImageHolder(shopImg.getName(),is);
		ShopExecution shopExecution=shopService.modifyShop(shop,new ImageHolder(null, null));
		System.out.println(shopExecution.getStateInfo());
	}
	
	@Test
	@Ignore
	public void testGetShopList(){
		Shop shopCondition=new Shop();
		PersonInfo owner=new PersonInfo();
		owner.setUserId(1L);
		shopCondition.setOwner(owner);
		ShopCategory category=new ShopCategory();
		shopCondition.setShopCategory(category);
		ShopExecution se=shopService.getShopList(shopCondition, 2, 2);
		System.out.println(se.getCount());
		System.out.println(se.getStateInfo());
		for(Shop shop:se.getShopList()){
			System.out.println(shop.getShopName());
		}
	}
}
