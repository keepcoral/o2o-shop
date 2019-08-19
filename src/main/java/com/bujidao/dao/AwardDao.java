package com.bujidao.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bujidao.entity.Award;

public interface AwardDao {
	/**
	 * 根据查询条件分页查询Award
	 */
	List<Award> queryAwardList(@Param("awardCondtion") Award awardCondtion,
			@Param("rowIndex") int rowIndex,@Param("pageSize")int pageSize);
	
	/**
	 * 根据查询条件分页查询Award的数量
	 */
	int queryAwardCount(@Param("awardCondtion") Award awardCondtion);
	
	/**
	 * 根据id查询奖品
	 */
	Award queryAwardByAwardId(Long awardId);
	
	/**
	 * 插入奖品
	 */
	int insertAward(Award award);
	/**
	 * 更新奖品
	 */
	int updateAward(Award award);
	
	/**
	 * 删除奖品信息，每个店家都只能删除自己店铺的奖品不能删除别人的奖品
	 */
	int deleteAward(@Param("awardId") long awardId,@Param("shopId") long shopId);
}