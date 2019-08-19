package com.bujidao.service.imp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bujidao.cache.JedisUtil;
import com.bujidao.dao.AreaDao;
import com.bujidao.entity.Area;
import com.bujidao.exception.AreaOperationException;
import com.bujidao.service.AreaService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AreaServiceImp implements AreaService {
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private JedisUtil.Strings jedisStrings;

	private static Logger logger = LoggerFactory.getLogger(AreaServiceImp.class);

	@Override
	public List<Area> queryArea() {
		String key = AREALIST;
		List<Area> areaList = null;
		ObjectMapper mapper = new ObjectMapper();
		// 缓存中不存在，查询数据库
		if (!jedisKeys.exists(key)) {
			areaList = areaDao.queryArea();
			String jsonString = null;
			try {
				// 将areaList转换为String
				jsonString = mapper.writeValueAsString(areaList);
				//每三小时刷新一次缓存，数字单位为s
				jedisStrings.setEx(key, 60*60*3,jsonString);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			}
		} else {
			// 如果存在，则直接从redis中取出该json串转换为java对象
			String jsonString = jedisStrings.get(key);
			//找到要转换的类型
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
			try {
				//将json串转换为areaList
				areaList = mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			}
		}
		return areaList;
	}

}
