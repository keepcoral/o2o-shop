package com.bujidao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bujidao.entity.ShopCategory;

public interface ShopCategoryDao {
	List<ShopCategory> queryShopCategory
		(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);

	ShopCategory queryShopCategoryById(Long shopCategoryId);
}
