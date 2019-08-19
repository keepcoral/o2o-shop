package com.bujidao.service;

import java.util.List;

import com.bujidao.dto.ImageHolder;
import com.bujidao.dto.ProductExecution;
import com.bujidao.entity.Product;
import com.bujidao.exception.ProductOperationException;

public interface ProductService {
	/**
	 * 添加商品信息和图片处理
	 * @param thumbnail 缩略图
	 * @param productImgList 详情图片
	 */
	ProductExecution addProduct(Product product,
			ImageHolder thumbnail,List<ImageHolder> productImgHolderList)
				throws ProductOperationException;
	
	Product getProductById(long productId);
	
	ProductExecution modifyProduct(Product product,
			ImageHolder thumbnail,List<ImageHolder> productImgHolderList)
					throws ProductOperationException;

	ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);
}
