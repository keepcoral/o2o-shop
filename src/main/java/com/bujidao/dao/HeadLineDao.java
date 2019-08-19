package com.bujidao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bujidao.entity.HeadLine;

public interface HeadLineDao {
	/**
	 * 根据查询条件查询头条
	 */
	 List<HeadLine> queryHeadLineList(@Param("headLineCondition") HeadLine headLineCondition);
}
