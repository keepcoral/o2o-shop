package com.bujidao.dao;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bujidao.entity.Award;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardDaoTest {
	
	@Autowired
	private AwardDao awardDao;
	
	@Test
//	@Ignore
	public void testQueryAwardList(){
		Award award=new Award();
		award.setAwardName("奶茶");
		System.out.println(awardDao.queryAwardList(award, 0, 1));
		System.out.println("数量为"+awardDao.queryAwardCount(award));
	}
	
	@Test
	@Ignore
	public void testInsertAward(){
		Award award=new Award();
		award.setAwardName("中级奶茶");
		award.setAwardDesc("一般般的奶茶");
		award.setPoint(10);
		award.setPriority(20);
		award.setEnableStatus(0);
		award.setCreateTime(new Date());
		award.setLastEditTime(new Date());
		System.out.println("当前id是"+award.getAwardId());
		awardDao.insertAward(award);
		System.out.println("现在id是"+award.getAwardId());
	}
	
	@Test
	@Ignore
	public void testQueryById(){
		System.out.println(awardDao.queryAwardByAwardId(1L));
	}
	
	@Test
	@Ignore
	public void testDeleteAward(){
		System.out.println(awardDao.deleteAward(4L, 3L));
	}
	
	@Test
	@Ignore
	public void testUpdateAward(){
		Award award=new Award();
		award.setAwardId(1L);
		award.setLastEditTime(new Date());
		award.setShopId(3L);
		awardDao.updateAward(award);
	}
}
