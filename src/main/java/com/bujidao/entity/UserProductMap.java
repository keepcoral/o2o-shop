package com.bujidao.entity;

import java.util.Date;

/**
 * 顾客消费的商品映射
 * @author wyq
 *
 */
public class UserProductMap {
	//主键Id
	private Long userProductId;
	//创建日期
	private Date createTime;
	//消费所获的积分
	private Integer point;
	//商品信息
	private Product product;
	//顾客信息
	private PersonInfo user;
	//商店信息
	private Shop shop;
	//操作员信息
	private PersonInfo operator;
	
	
	public Long getUserProductId() {
		return userProductId;
	}
	public void setUserProductId(Long userProductId) {
		this.userProductId = userProductId;
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
	public PersonInfo getOperator() {
		return operator;
	}
	public void setOperator(PersonInfo operator) {
		this.operator = operator;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}
