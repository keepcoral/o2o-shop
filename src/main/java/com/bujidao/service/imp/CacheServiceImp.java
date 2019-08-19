package com.bujidao.service.imp;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bujidao.cache.JedisUtil;
import com.bujidao.service.CacheService;

@Service
public class CacheServiceImp implements CacheService{

	@Autowired
	private JedisUtil.Keys jedisKeys;
	
	@Override
	public void removeFromCache(String keyPrefix) {
		Set<String> keySet=jedisKeys.keys(keyPrefix+"*");//找出所有key
		for(String key:keySet){
			jedisKeys.del(key);
		}
	}

}
