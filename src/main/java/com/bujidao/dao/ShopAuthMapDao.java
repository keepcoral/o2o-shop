package com.bujidao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bujidao.entity.ShopAuthMap;


public interface ShopAuthMapDao {
	/**
	 * 分页列出授权信息
	 */
	List<ShopAuthMap> queryShopAuthMapListByShopId(@Param("shopId") Long shopId,
			@Param("rowIndex") int rowIndex,@Param("pageSize")int pageSize);
	/**
	 * 授权信息的总数
	 */
	int queryShopAuthMapCountByShopId(@Param("shopId") Long shopId);
	
	/**
	 * 更新授权信息
	 */
	int updateShopAuthMap(ShopAuthMap shopAuthMap);
	
	/**
	 * 对员工移权
	 */
	int deleteShopAuthMap(long shopAuthId);
	
	/**
	 * 新增员工
	 */
	int insertShopAuthMap(ShopAuthMap shopAuthMap);
	
	/**
	 * 通过id查询员工授权信息
	 */
	ShopAuthMap queryShopAuthMapById(long shopAuthId);
	
}
