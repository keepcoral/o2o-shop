package com.bujidao.dao;

import java.util.List;

import com.bujidao.entity.ProductImg;

public interface ProductImgDao {
	/**
	 * 批量添加图片
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);
	
	/**
	 * 通过id删除图片
	 */
	int deleteProductImgByProductId(long productId);
	
	List<ProductImg> queryProductImgList(long productId);
}
