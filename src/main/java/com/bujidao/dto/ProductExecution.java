package com.bujidao.dto;

import java.util.List;

import com.bujidao.entity.Product;
import com.bujidao.enums.ProductStateEnum;

public class ProductExecution {
	private int state;
	//状态标识
	private String stateInfo;
	
	private int count;
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	//增删改使用
	private Product product;
	//查询使用
	private List<Product> productList;
	
	public ProductExecution() {

	}
	
	public ProductExecution(ProductStateEnum stateEnum) {
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
	}
	
	public ProductExecution(ProductStateEnum stateEnum,Product product) {
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.product=product;
	}
	
	public ProductExecution(ProductStateEnum stateEnum,List<Product> productList,int count) {
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.productList=productList;
		this.count=count;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	
	
}
