package com.bujidao.service;

import java.util.List;

import com.bujidao.dto.ProductCategoryExecution;
import com.bujidao.entity.ProductCategory;
import com.bujidao.exception.ProductCategoryOperationException;

public interface ProductCategoryService {
	
	List<ProductCategory> getProductCategoryList(Long shopId);
	
	ProductCategoryExecution batchAddProductCategory
		(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;
	
	ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId)
		throws ProductCategoryOperationException;
}
