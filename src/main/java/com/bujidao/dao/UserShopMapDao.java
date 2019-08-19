package com.bujidao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bujidao.entity.UserShopMap;

public interface UserShopMapDao {
	/**
	 * 根据查询条件返回用户店铺积分列表
	 */
	List<UserShopMap> queryUserShopMapList(@Param("userShopMapCondition") UserShopMap userShopMapCondition,
			@Param("rowIndex") int rowIndex,@Param("pageSize") int pageSize);
	
	/**
	 * 根据查询条件返回用户店铺积分列表记录总数
	 */
	int queryUserShopMapCount(@Param("userShopMapCondition") UserShopMap userShopMapCondition);

	/**
	 * 根据传入的userId和shopId查询积分信息
	 */
	UserShopMap queryUserShopMap(@Param("userId") long userId,@Param("shopId") long shopId);

	/**
	 * 添加一条信息
	 */
	int insertUserShopMap(UserShopMap userShopMap);
	
	/**
	 * 更新用户在某店铺的积分
	 */
	int updateUserShopMapPoint(UserShopMap userShopMap);
}
