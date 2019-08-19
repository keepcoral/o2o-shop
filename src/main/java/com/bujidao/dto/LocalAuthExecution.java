package com.bujidao.dto;

import java.util.List;

import com.bujidao.entity.LocalAuth;
import com.bujidao.enums.LocalAuthStateEnum;

public class LocalAuthExecution {
	private int state;
	
	private String stateInfo;
	
	private int count;
	private LocalAuth localAuth;
	
	private List<LocalAuth> localAuthList;

	public LocalAuthExecution() {
		super();
	}
	
	/**
	 * 操作失败使用的构造器
	 */
	public LocalAuthExecution(LocalAuthStateEnum stateEnum){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
	}
	public LocalAuthExecution(LocalAuthStateEnum stateEnum,List<LocalAuth> localAuthList){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.localAuthList=localAuthList;
	}
	public LocalAuthExecution(LocalAuthStateEnum stateEnum,LocalAuth localAuth){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.localAuth=localAuth;
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

	public List<LocalAuth> getLocalAuthList() {
		return localAuthList;
	}

	public void setLocalAuthList(List<LocalAuth> localAuthList) {
		this.localAuthList = localAuthList;
	}

	public LocalAuth getLocalAuth() {
		return localAuth;
	}

	public void setLocalAuth(LocalAuth localAuth) {
		this.localAuth = localAuth;
	}

}
