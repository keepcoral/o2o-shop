package com.bujidao.service;

import com.bujidao.dto.ShopAuthMapExecution;
import com.bujidao.entity.ShopAuthMap;
import com.bujidao.exception.ShopAuthMapOperationException;

public interface ShopAuthMapService {
	
	/**
	 * 分页查询授权信息
	 */
	ShopAuthMapExecution listShopAuthMapByShopId(Long shopId,
			Integer pageIndex,Integer pageSize);

	/**
	 * 根据shopAuthId返回授权信息
	 */
	ShopAuthMap getShopAuthMapById(Long shopAuthId);
	
	/**
	 * 添加授权信息
	 */
	ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) 
			throws ShopAuthMapOperationException;

	/**
	 * 更新授权信息包括职位状态
	 */
	ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) 
			throws ShopAuthMapOperationException;
}
