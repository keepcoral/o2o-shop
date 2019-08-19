package com.bujidao.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bujidao.dao.UserProductMapDao;
import com.bujidao.dto.UserProductMapExecution;
import com.bujidao.entity.UserProductMap;
import com.bujidao.service.UserProductMapService;
import com.bujidao.util.PageCalculator;

@Service
public class UserProductMapServiceImp implements UserProductMapService {

	@Autowired
	private UserProductMapDao userProductMapDao;

	@Override
	public UserProductMapExecution getUserProductMapList(UserProductMap userProductMapCondition, Integer pageIndex,
			Integer pageSize) {
		if (userProductMapCondition != null && pageIndex != null && pageSize != null) {
			int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
			List<UserProductMap> userProductMapList=userProductMapDao.queryUserProductMapList
						(userProductMapCondition, rowIndex, pageSize);
			int count=userProductMapDao.queryUserProductMapCount(userProductMapCondition);
			UserProductMapExecution ue=new UserProductMapExecution();
			ue.setUserProductMapList(userProductMapList);
			ue.setCount(count);
			return ue;
		} else {
			return null;
		}
	}

}
