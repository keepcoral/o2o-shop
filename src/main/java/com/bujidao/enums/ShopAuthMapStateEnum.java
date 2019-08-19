package com.bujidao.enums;


public enum ShopAuthMapStateEnum {
	SUCCESS(1,"操作成功"),INNER_ERROR(-1001,"操作失败"),
	NULL_SHOPAUTH_ID(-1002,"shopAuthId为空"),NULL_SHOPAUTH_INFO(-1003,"传入空的信息");
	private int state;
	private String stateInfo;
	
	private ShopAuthMapStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}
	
	public static ShopAuthMapStateEnum stateOf(int state){
		for(ShopAuthMapStateEnum stateEnum:values()){
			if(stateEnum.getState()==state){
				return stateEnum;
			}
		}
		return null;
	}
}
