package com.bujidao.service;

import java.util.List;

import com.bujidao.entity.ShopCategory;

public interface ShopCategoryService {
	String SCLISTKEY="shopcategorylist";
	List<ShopCategory> getShopCategory(ShopCategory shopCategoryCondition) ;		
}
