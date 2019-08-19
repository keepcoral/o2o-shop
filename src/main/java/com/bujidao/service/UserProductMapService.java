package com.bujidao.service;

import java.util.List;

import com.bujidao.dto.UserProductMapExecution;
import com.bujidao.entity.UserProductMap;

public interface UserProductMapService {
	UserProductMapExecution getUserProductMapList( UserProductMap userProductMapCondition,
		 Integer pageIndex, Integer pageSize);
	
//	int addUserProductMap(UserProductMap userProductMap);
	
}
