package com.bujidao.service;

public interface  CacheService {
	/**
	 * 根据key前缀删除所有以该key为前缀的key-value键值对
	 * @param keyPrefix
	 */
	void removeFromCache(String keyPrefix);
}
