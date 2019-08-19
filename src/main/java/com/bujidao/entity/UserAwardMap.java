package com.bujidao.entity;

import java.util.Date;


/**
 * 顾客已领取奖品映射
 * @author wyq
 *
 */
public class UserAwardMap {
	private Long userAwardId;
	private Date createTime;
	//使用状态0未兑换1已兑换
	private Integer usedStatus;
	//兑换商品消耗的积分
	private Integer point;
	//顾客信息
	private PersonInfo user;
	//奖品信息
	private Award award;
	//商店信息
	private Shop shop;
	//操作人信息实体类，颁发该奖品的操作员
	private PersonInfo operator;
	
	public Long getUserAwardId() {
		return userAwardId;
	}
	public void setUserAwardId(Long userAwardId) {
		this.userAwardId = userAwardId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getUsedStatus() {
		return usedStatus;
	}
	public void setUsedStatus(Integer usedStatus) {
		this.usedStatus = usedStatus;
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
	public Award getAward() {
		return award;
	}
	public void setAward(Award award) {
		this.award = award;
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
	
	

}
