package com.bujidao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bujidao.entity.UserProductMap;

public interface UserProductMapDao {
	/**
	 * 按照条件分页查询消费记录
	 * @param userProductMapCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<UserProductMap> queryUserProductMapList(@Param("userProductMapCondition") UserProductMap userProductMapCondition,
			@Param("rowIndex") int rowIndex,@Param("pageSize") int pageSize);
	
	/**
	 * 按照条件分页查询消费记录的条数
	 */
	int queryUserProductMapCount(@Param("userProductMapCondition") UserProductMap userProductMapCondition);

	/**
	 * 插入用户购买商品的记录
	 */
	int insertUserProductMap(UserProductMap userProductMap);

}
