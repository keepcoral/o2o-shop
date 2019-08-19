package com.bujidao.service;


import com.bujidao.dto.ImageHolder;
import com.bujidao.dto.ShopExecution;
import com.bujidao.entity.Shop;
import com.bujidao.exception.ShopOperationException;


public interface ShopService {
	/**
	 * 根据shopCondition分页返回店铺列表
	 */
	ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
	
	/**
	 * 添加商铺
	 * 先添加商铺，如果没有发生异常，在使用update方法添加图片，这样以防添加失败之后图片仍然保留
	 */
	ShopExecution addShop(Shop shop,
			ImageHolder thumbnail) throws ShopOperationException;
	/**
	 * 通过id获取商店
	 */
	Shop getByShopId(long shopId);
	/**
	 * 更新店铺信息包括对图片处理
	 */
	ShopExecution modifyShop(Shop shop,ImageHolder thumbnail) throws ShopOperationException;
}
