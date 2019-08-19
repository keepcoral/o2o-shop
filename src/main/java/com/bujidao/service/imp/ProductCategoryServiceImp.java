package com.bujidao.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bujidao.dao.ProductCategoryDao;
import com.bujidao.dao.ProductDao;
import com.bujidao.dto.ProductCategoryExecution;
import com.bujidao.entity.ProductCategory;
import com.bujidao.enums.ProductCategoryStateEnum;
import com.bujidao.exception.ProductCategoryOperationException;
import com.bujidao.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImp implements ProductCategoryService {
	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Autowired
	private ProductDao productDao;

	@Override
	public List<ProductCategory> getProductCategoryList(Long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				int effectNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
				if (effectNum < 0) {
					throw new ProductCategoryOperationException("店铺创建失败");
				} else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS, productCategoryList);
				}
			} catch (Exception e) {
				throw new ProductCategoryOperationException("店铺创建失败:" + e.getMessage());
			}
		} else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		//解除tb_product与tb_product_category的关系
		try {
			//先查询是否有该商品类别的商品
			int productCount=productDao.queryCountProductByProductCategoryId(productCategoryId);
			if(productCount!=0){
				int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
				if (effectedNum <= 0) {
					throw new ProductCategoryOperationException("商品更新删除失败！");
				} 
			}
		} catch (Exception e) {
			throw new ProductCategoryOperationException("商品类别更新失败！delete errorMsg" + e.getMessage());
		}
		//删除该productCategory
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if (effectedNum <= 0) {
				throw new ProductCategoryOperationException("商品类别删除失败！");
			} else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		} catch (Exception e) {
			throw new ProductCategoryOperationException("商品类别删除失败！delete errorMsg" + e.getMessage());
		}
	}

}
