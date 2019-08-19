package com.bujidao.dto;

import java.util.List;

import com.bujidao.entity.ShopAuthMap;
import com.bujidao.enums.ShopAuthMapStateEnum;

public class ShopAuthMapExecution {
	private int state;
	//状态标识
	private String stateInfo;
	
	private int count;
	
	private ShopAuthMap shopAuthMap;
	
	private List<ShopAuthMap> shopAuthMapList;

	public ShopAuthMapExecution() {
		super();
	}
	
	//操作失败的构造器
	public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
	}
	
	//操作成功的构造器  单个
	public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum,ShopAuthMap shopAuthMap){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.shopAuthMap=shopAuthMap;
	}
	
	//操作成功的构造器 多个
	public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum,List<ShopAuthMap> shopAuthMapList){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.shopAuthMapList=shopAuthMapList;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ShopAuthMap getShopAuthMap() {
		return shopAuthMap;
	}

	public void setShopAuthMap(ShopAuthMap shopAuthMap) {
		this.shopAuthMap = shopAuthMap;
	}

	public List<ShopAuthMap> getShopAuthMapList() {
		return shopAuthMapList;
	}

	public void setShopAuthMapList(List<ShopAuthMap> shopAuthMapList) {
		this.shopAuthMapList = shopAuthMapList;
	}
	
	
}
