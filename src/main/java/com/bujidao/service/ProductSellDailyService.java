package com.bujidao.service;

import java.util.Date;
import java.util.List;

import com.bujidao.entity.ProductSellDaily;

public interface ProductSellDailyService {
	void dailyCalculate();
	
	List<ProductSellDaily> getProductSellDailyList(ProductSellDaily productSellDailyCondition,
			Date beginTime,Date endTime);
}
