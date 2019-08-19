package com.bujidao.dto;

import java.util.List;

import com.bujidao.entity.Shop;
import com.bujidao.entity.UserProductMap;
import com.bujidao.enums.ShopStateEnum;
import com.bujidao.enums.UserProductMapStateEnum;

/**
 * 
 *	商店的操作后的状态
 */
public class UserProductMapExecution {
	//结果状态
	private int state;
	
	//状态标识
	private String stateInfo;
	
	private UserProductMap userProductMap;
	
	private int count;
	
	private List<UserProductMap> userProductMapList;
	
	public UserProductMapExecution(){
		
	}
	
	public UserProductMapExecution(UserProductMapStateEnum stateEnum){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
	}
	
	public UserProductMapExecution(UserProductMapStateEnum stateEnum,UserProductMap userProductMap){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.userProductMap=userProductMap;
	}
	
	public UserProductMapExecution(UserProductMapStateEnum stateEnum,List<UserProductMap> userProductMapList){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.userProductMapList=userProductMapList;
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

	public UserProductMap getUserProductMap() {
		return userProductMap;
	}

	public void setUserProductMap(UserProductMap userProductMap) {
		this.userProductMap = userProductMap;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<UserProductMap> getUserProductMapList() {
		return userProductMapList;
	}

	public void setUserProductMapList(List<UserProductMap> userProductMapList) {
		this.userProductMapList = userProductMapList;
	}
	
	
}
