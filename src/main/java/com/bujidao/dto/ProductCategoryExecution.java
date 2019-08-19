package com.bujidao.dto;

import java.util.List;

import com.bujidao.entity.ProductCategory;
import com.bujidao.enums.ProductCategoryStateEnum;

public class ProductCategoryExecution {
	private int state;
	private String stateInfo;
	private List<ProductCategory> productCategoryList;
	public ProductCategoryExecution() {
		super();
	}
	/**
	 * 操作失败使用的构造器
	 * @param productCategoryStateEnum
	 */
	public ProductCategoryExecution
		(ProductCategoryStateEnum productCategoryStateEnum) {
		this.state = productCategoryStateEnum.getState();
		this.stateInfo = productCategoryStateEnum.getStateInfo();
	}
	public ProductCategoryExecution(ProductCategoryStateEnum productCategoryStateEnum
					,List<ProductCategory> productCategoryList) {
		this.productCategoryList=productCategoryList;
		this.state = productCategoryStateEnum.getState();
		this.stateInfo = productCategoryStateEnum.getStateInfo();
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStateInfo() {
		return stateInfo;
	}
	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
	public List<ProductCategory> getProductCategoryList() {
		return productCategoryList;
	}
	public void setProductCategoryList(List<ProductCategory> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}
	
	
}
