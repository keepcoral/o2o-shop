package com.bujidao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bujidao.entity.Area;
import com.bujidao.entity.ProductCategory;

public interface ProductCategoryDao {
	/**
	 * 通过店铺id查询店铺商品类别
	 */
	List<ProductCategory> queryProductCategoryList(Long shopId);
	/**
	 * 批量新增商品类别	
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	
	int deleteProductCategory(@Param("productCategoryId") long productCategoryId,
			@Param("shopId")long shopId);
}
