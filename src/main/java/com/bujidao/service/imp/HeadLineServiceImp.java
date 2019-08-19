package com.bujidao.service.imp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bujidao.cache.JedisUtil;
import com.bujidao.dao.HeadLineDao;
import com.bujidao.entity.HeadLine;
import com.bujidao.exception.HeadLineOperationException;
import com.bujidao.service.HeadLineService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HeadLineServiceImp implements HeadLineService {
	@Autowired
	private HeadLineDao headLineDao;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private JedisUtil.Strings jedisStrings;

	private static Logger logger = LoggerFactory.getLogger(HeadLineServiceImp.class);

	@Override
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) {
		String key = HLLISTKEY;
		ObjectMapper mapper=new ObjectMapper();
		List<HeadLine> headLineList=null;
		//将可用的头条和不可用的头条分开
		if(headLineCondition!=null&&headLineCondition.getEnableStatus()!=null){
			key+="_"+headLineCondition.getEnableStatus();
		}
		//不存在
		if(!jedisKeys.exists(key)){
			headLineList=headLineDao.queryHeadLineList(headLineCondition);
			String jsonString=null;
			try {
				//将数据转换为json串
				jsonString=mapper.writeValueAsString(headLineList);
				//每三小时刷新一次缓存，数字单位为s
				jedisStrings.setEx(key, 60*60*3,jsonString);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}
		}else{
			//从redis中取出json串
			String jsonString=jedisStrings.get(key);
			//指定转换的类型
			JavaType javaType=mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
			//将json串转换为java类型
			try {
				headLineList=mapper.readValue(jsonString, javaType);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}
		}
		return headLineList;
	}

}
