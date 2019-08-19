package com.bujidao.dto;

import java.util.List;

import com.bujidao.entity.Shop;
import com.bujidao.enums.ShopStateEnum;

/**
 * 
 *	商店的操作后的状态
 */
public class ShopExecution {
	//结果状态
	private int state;
	
	//状态标识
	private String stateInfo;
	
	private Shop shop;
	
	private int count;
	
	private List<Shop> shopList;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * 店铺操作失败使用的构造器
	 */
	public ShopExecution(ShopStateEnum stateEnum){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
	}
	/**
	 * 店铺操作成功败使用的构造器
	 */
	public ShopExecution(ShopStateEnum stateEnum,List<Shop> shopList){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.shopList=shopList;
	}
	
	/**
	 * 店铺操作成功使用的构造器
	 */
	public ShopExecution(ShopStateEnum stateEnum,Shop shop){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.shop=shop;
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
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	public List<Shop> getShopList() {
		return shopList;
	}
	public void setShopList(List<Shop> shopList) {
		this.shopList = shopList;
	}
	
	
}
