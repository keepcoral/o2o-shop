package com.bujidao.service.imp;

import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bujidao.dao.ProductSellDailyDao;
import com.bujidao.entity.ProductSellDaily;
import com.bujidao.service.ProductSellDailyService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ProductSellDailyServiceImp implements ProductSellDailyService {
    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Override
    @Transactional
    public void dailyCalculate() {
        System.out.println("测试定时任务");
        //先统计有消费的商品
        productSellDailyDao.insertProductSellDaily();
        //再统计没有消费的商品
        productSellDailyDao.insertDefaultProductSellDaily();
    }

    /**
     * 根据查询条件返回商品日销量的统计列表
     */
    @Override
    public List<ProductSellDaily> getProductSellDailyList(ProductSellDaily productSellDailyCondition, Date beginTime,
                                                          Date endTime) {
        return productSellDailyDao.queryProductSellDailyList(productSellDailyCondition
                , beginTime, endTime);
    }


}
