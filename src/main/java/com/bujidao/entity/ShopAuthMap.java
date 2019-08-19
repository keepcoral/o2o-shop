package com.bujidao.entity;

import java.util.Date;

/**
 * 店铺授权
 * @author wyq
 *
 */
public class ShopAuthMap {
	//主键
	private Long shopAuthId;
	//职称名字
	private String title;
	//职称符号，可用于权限控制
	private Integer titleFlag;
	//授权状态 0无效1有效
	private Integer enableStatus;
	//创建时间
	private Date createTime;
	//最后一次更新时间
	private Date lastEditTime;
	//员工信息
	private PersonInfo employee;
	//店铺信息
	private Shop shop;
	
	public Long getShopAuthId() {
		return shopAuthId;
	}
	public void setShopAuthId(Long shopAuthId) {
		this.shopAuthId = shopAuthId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getTitleFlag() {
		return titleFlag;
	}
	public void setTitleFlag(Integer titleFlag) {
		this.titleFlag = titleFlag;
	}
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	public PersonInfo getEmployee() {
		return employee;
	}
	public void setEmployee(PersonInfo employee) {
		this.employee = employee;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
}
