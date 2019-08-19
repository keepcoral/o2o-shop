package com.bujidao.enums;

public enum ProductStateEnum {
	SUCCESS(1,"操作成功"),INNER_ERROR(-1001,"操作失败"),EMPTY(-1002,"添加数少于1");
	private int state;
	private String stateInfo;
	private ProductStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
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
	
	
}
