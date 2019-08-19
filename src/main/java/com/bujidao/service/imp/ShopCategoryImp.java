package com.bujidao.service.imp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bujidao.cache.JedisUtil;
import com.bujidao.dao.ShopCategoryDao;
import com.bujidao.entity.ShopCategory;
import com.bujidao.exception.ShopCategoryOperationException;
import com.bujidao.service.ShopCategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class ShopCategoryImp implements ShopCategoryService{
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	private Logger logger =LoggerFactory.getLogger(ShopCategoryImp.class);
	/**
	 * 根据查询条件获取shopCategory列表
	 */
	@Override
	public List<ShopCategory> getShopCategory(ShopCategory shopCategoryCondition) {
		String key=SCLISTKEY;
		List<ShopCategory> shopCategoryList=null;
		ObjectMapper mapper=new ObjectMapper();
		//拼接条件
		if(shopCategoryCondition==null){
			//条件为空，那么查询一级分类
			key+="_allfirstlevel";
		}else if(shopCategoryCondition!=null&&shopCategoryCondition.getParent()!=null
				&&shopCategoryCondition.getParent().getShopCategoryId()!=null){
			//若parentId不为空，返回所有该parentId的所有子类别
			key+="_parent"+shopCategoryCondition.getParent().getShopCategoryId();
		}else{
			//获取所有子类别，不管属于哪一个一级类别，都列出来
			key+="_allsecondlevel";
		}
		
		if(!jedisKeys.exists(key)){
			shopCategoryList=shopCategoryDao.queryShopCategory(shopCategoryCondition);
			String jsonString=null;
			try {
				jsonString = mapper.writeValueAsString(shopCategoryList);
				//每三小时刷新一次缓存，数字单位为s
				jedisStrings.setEx(key, 60*60*3,jsonString);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
		}else{
			String jsonString=jedisStrings.get(key);
			JavaType javaType=mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
			try {
				shopCategoryList=mapper.readValue(jsonString, javaType);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
		}
		return shopCategoryList;
	}
}
