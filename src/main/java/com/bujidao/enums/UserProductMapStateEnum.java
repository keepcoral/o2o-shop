package com.bujidao.enums;

public enum UserProductMapStateEnum {
	SUCCESS(1,"操作成功");
	private int state;
	private String stateInfo;
	
	UserProductMapStateEnum(int state,String stateInfo){
		this.state=state;
		this.stateInfo=stateInfo;
	}
	
	public static UserProductMapStateEnum stateOf(int state){
		for(UserProductMapStateEnum stateEnum:values()){
			if(stateEnum.getState()==state){
				return stateEnum;
			}
		}
		return null;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	
	

}
