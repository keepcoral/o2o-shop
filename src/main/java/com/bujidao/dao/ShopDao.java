package com.bujidao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.junit.runners.Parameterized.Parameters;

import com.bujidao.entity.Shop;

public interface ShopDao {
	/**
	 * 分页查询店铺，可输入条件：店铺名，店铺状态，店铺类别
	 * 区域id，owner
	 * 这里参数有多个所以要用@Param这个标签，为每个参数提供唯一标识
	 */
	List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,
			@Param("rowIndex") int rowIndex,@Param("pageSize")int pageSize);
	/**
	 * 返回shopList总数
	 */
	int queryShopCount(@Param("shopCondition")Shop shopCondition);
	/**
	 * 增加店铺
	 */
	int insertShop(Shop shop);
	/**
	 * 更新店铺
	 */
	int updateShop(Shop shop);
	/**
	 * 通过id查询shop
	 */
	Shop queryByShopId(long shopId);
}
