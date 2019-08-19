package com.bujidao.dto;

import java.util.HashSet;


/**
 * 对应echart的aAxis项，也就x轴
 * @author wyq
 *
 */
public class EchartXAxis {
	private String type="category";
	//为了去重
	private HashSet<String> data;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public HashSet<String> getData() {
		return data;
	}
	public void setData(HashSet<String> data) {
		this.data = data;
	}
	
	
}
