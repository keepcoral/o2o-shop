package com.bujidao.entity;

import java.util.Date;

/**
 * 顾客商铺积分映射
 * @author wyq
 *
 */
public class UserShopMap {
	//主键
	private Long userShopId;
	//创建日期
	private Date createTime;
	//顾客在该店铺的积分
	private Integer point;
	//顾客信息
	private PersonInfo user;
	//商店信息
	private Shop shop;
	
	public Long getUserShopId() {
		return userShopId;
	}
	public void setUserShopId(Long userShopId) {
		this.userShopId = userShopId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public PersonInfo getUser() {
		return user;
	}
	public void setUser(PersonInfo user) {
		this.user = user;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
	
}
