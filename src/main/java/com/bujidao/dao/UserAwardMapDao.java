package com.bujidao.dao;

import java.util.List;


import org.apache.ibatis.annotations.Param;

import com.bujidao.entity.UserAwardMap;

public interface UserAwardMapDao {
	/**
	 * 按照查询条件，分页查询用户兑换奖品信息
	 */
	List<UserAwardMap> queryUserAwardMapList(@Param("userAwardMapCondition") UserAwardMap userAwardMapCondition,
			@Param("rowIndex") int rowIndex,@Param("pageSize") int pageSize);
	
	/**
	 * 按照查询条件，分页查询用户兑换奖品信息的条数
	 * @param userAwardMapCondition
	 */
	int queryUserAwardMapCount(@Param("userAwardMapCondition") UserAwardMap userAwardMapCondition);
	
	/**
	 * 按照id查询用户兑换奖品信息
	 */
	UserAwardMap queryUserMapById(long userAwardMapId);
	
	/**
	 * 添加一条奖品兑换信息
	 */
	int insertUserAwardMap(UserAwardMap userAwardMap);
	
	/**
	 * 更新奖品兑换信息，更新奖品领取状态
	 */
	int updateUserAwardMap(UserAwardMap userAwardMap);
	
}
