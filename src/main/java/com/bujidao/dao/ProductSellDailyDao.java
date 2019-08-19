package com.bujidao.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bujidao.entity.ProductSellDaily;

public interface ProductSellDailyDao {
	/**
	 * 根据查询条件返回商品日销量的统计列表
	 */
	List<ProductSellDaily> queryProductSellDailyList(
			@Param("productSellDailyCondition") ProductSellDaily productSellDailyCondition,
			@Param("beginTime") Date beginTime,@Param("endTime") Date endTime);

	/**
	 * 统计所有商品的日销售量
	 */
	int insertProductSellDaily();
	
	/**
	 * 当天没有销量商品信息，补全信息，将他们销量重置为0
	 */
	int insertDefaultProductSellDaily();
}
