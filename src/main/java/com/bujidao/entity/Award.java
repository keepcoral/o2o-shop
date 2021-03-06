package com.bujidao.entity;

import java.util.Date;

/**
 * 奖品实体类
 * @author wyq
 *
 */
public class Award {
	private Long awardId;
	private String awardName;
	private String awardDesc;
	private String awardImg;
	//奖品积分，需要多少积分去兑换
	private Integer point;
	private Integer priority;
	private Date createTime;
	private Date expireTime;
	private Date lastEditTime;
	//可用状态：0不可用，1可用
	private Integer enableStatus;
	//只需要获取shopId即可不需要复合对象
	private Long shopId;
	public Long getAwardId() {
		return awardId;
	}
	public void setAwardId(Long awardId) {
		this.awardId = awardId;
	}
	public String getAwardName() {
		return awardName;
	}
	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}
	public String getAwardDesc() {
		return awardDesc;
	}
	public void setAwardDesc(String awardDesc) {
		this.awardDesc = awardDesc;
	}
	public String getAwardImg() {
		return awardImg;
	}
	public void setAwardImg(String awardImg) {
		this.awardImg = awardImg;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
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
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	
	
	
	
	
	
}
