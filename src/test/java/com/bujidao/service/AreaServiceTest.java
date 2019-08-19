package com.bujidao.service;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bujidao.entity.Area;
import com.bujidao.service.AreaService;



@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest{
	@Autowired
	private AreaService areaService;
	@Autowired
	private CacheService cacheService;
	
	@Test
	public void testQueryArea(){
		System.out.println(areaService);
		List<Area> areaList=areaService.queryArea();
		cacheService.removeFromCache(areaService.AREALIST);
//		for(Area area:areaList){
////			System.out.println(area.getAreaName());
//		}
	}
}
