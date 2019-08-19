package com.bujidao.service.imp;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bujidao.dao.ShopAuthMapDao;
import com.bujidao.dto.ShopAuthMapExecution;
import com.bujidao.entity.ShopAuthMap;
import com.bujidao.enums.ShopAuthMapStateEnum;
import com.bujidao.exception.ShopAuthMapOperationException;
import com.bujidao.service.ShopAuthMapService;
import com.bujidao.util.PageCalculator;

@Service
public class ShopAuthMapServiceImp implements ShopAuthMapService {

	@Autowired
	private ShopAuthMapDao shopAuthMapDao;

	@Override
	public ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize) {
		ShopAuthMapExecution se = null;
		if (shopId != null && pageIndex != null && pageSize != null) {
			int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
			List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(shopId, rowIndex, pageSize);
			int count = shopAuthMapDao.queryShopAuthMapCountByShopId(shopId);
			se = new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMapList);
			se.setCount(count);
		} else {
			se = new ShopAuthMapExecution(ShopAuthMapStateEnum.INNER_ERROR);
		}
		return se;
	}

	@Override
	public ShopAuthMap getShopAuthMapById(Long shopAuthId) {
		return shopAuthMapDao.queryShopAuthMapById(shopAuthId);
	}

	@Override
	@Transactional
	public ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
		if (shopAuthMap != null && shopAuthMap.getShop() != null && shopAuthMap.getShop().getShopId() != null
				&& shopAuthMap.getEmployee() != null && shopAuthMap.getEmployee().getUserId() != null) {
				shopAuthMap.setCreateTime(new Date());
				shopAuthMap.setLastEditTime(new Date());
				shopAuthMap.setEnableStatus(1);
				shopAuthMap.setTitleFlag(1);
				try{
					int effectedNum=shopAuthMapDao.insertShopAuthMap(shopAuthMap);
					if(effectedNum<=0){
						throw new ShopAuthMapOperationException("添加授权失败！");
					}else{
						return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS,shopAuthMap);
					}
				}catch (Exception e) {
					throw new ShopAuthMapOperationException("添加授权失败！"+e.getMessage());
				}
		}else{
			return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOPAUTH_ID);
		}
	}

	@Override
	@Transactional
	public ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
		if(shopAuthMap!=null && shopAuthMap.getShopAuthId()!=null){
			try{
				int effectedNum=shopAuthMapDao.updateShopAuthMap(shopAuthMap);
				if(effectedNum<=0){
					return new ShopAuthMapExecution(ShopAuthMapStateEnum.INNER_ERROR);
				}else{
					shopAuthMap=shopAuthMapDao.queryShopAuthMapById(shopAuthMap.getShopAuthId());
					return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS,shopAuthMap);
				}
			}catch (Exception e) {
				throw new ShopAuthMapOperationException("修改授权失败"+e.getMessage());
			}
		}else{
			return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOPAUTH_INFO);
		}
	}

}
