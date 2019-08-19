package com.bujidao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bujidao.entity.Product;
import com.bujidao.entity.ProductImg;

public interface ProductDao {
	/**
	 * 分页查询商品，可以模糊查询商品名，商品状态，商品id
	 */
	List<Product> queryProductList(@Param("productCondition") Product productCondition,
			@Param("rowIndex") int rowIndex,@Param("pageSize")int pageSize);
	
	/**
	 * 插入商品
	 */
	int insertProduct(Product product);
	
	int queryProductCount(@Param("productCondition") Product productCondition);

	int updateProduct(Product product);
	
	/**
	 * 通过productCategoryId查询该类别的商品，用于删除商品类别
	 */
	int queryCountProductByProductCategoryId(long productCategoryId);
	
	Product queryByProductId(long productId);
	
	/**
	 * 删除商品类别之前，将商品类别ID置为空
	 */
	int updateProductCategoryToNull(long productCategoryId);
}
