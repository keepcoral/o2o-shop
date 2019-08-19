package com.bujidao.service.imp;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bujidao.dao.ShopAuthMapDao;
import com.bujidao.dao.ShopDao;
import com.bujidao.dto.ImageHolder;
import com.bujidao.dto.ShopExecution;
import com.bujidao.entity.Shop;
import com.bujidao.entity.ShopAuthMap;
import com.bujidao.enums.ShopStateEnum;
import com.bujidao.exception.ShopOperationException;
import com.bujidao.service.ShopService;
import com.bujidao.util.ImageUtil;
import com.bujidao.util.PageCalculator;
import com.bujidao.util.PathUtil;

@Service
public class ShopServiceImp implements ShopService {
    //	private static Logger logger=LoggerFactory.getLogger(ShopServiceImp.class);
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
			shop.setEnableStatus(0);//审核中为0
//            shop.setEnableStatus(1);//直接通过，最后要改回来

            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //查询商店parent_category_id
            if (shop.getShopCategory() != null) {

            }
            //添加店铺信息
            int effectNum = shopDao.insertShop(shop);
            if (effectNum <= 0) {
                throw new ShopOperationException("店铺创建失败！");
            } else {
                //先添加商铺，如果没有发生异常，在使用update方法添加图片，这样以防添加失败之后图片仍然保留
                if (thumbnail.getImage() != null) {
                    //存储图片
                    try {
                        addShopImg(shop, thumbnail);
                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg error:" + e.getMessage());
                    }
                    effectNum = shopDao.updateShop(shop);
                    if (effectNum <= 0) {
                        throw new ShopOperationException("更新图片地址失败！");
                    }
                }
            }
            ShopAuthMap shopAuthMap = new ShopAuthMap();
            shopAuthMap.setCreateTime(new Date());
            shopAuthMap.setLastEditTime(new Date());
            shopAuthMap.setEnableStatus(1);
            shopAuthMap.setEmployee(shop.getOwner());
            shopAuthMap.setTitle("店家");
            shopAuthMap.setTitleFlag(0);
            shopAuthMap.setShop(shop);
            try {
                effectNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
                if (effectNum <= 0) {
                    throw new ShopOperationException("添加店家授权失败");
                }
            } catch (Exception e) {
                throw new ShopOperationException("InsertAuth Failed:" + e.getMessage());
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error:" + e.getMessage());
            //当且仅当是抛出RuntimeException或者为RuntimeException的子类时，事务才能回滚
            //如果只是Exception的话是不会回滚的！
        }
        //返回一个CHECK也就是待审核的，且返回店铺信息
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    private void addShopImg(Shop shop, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
        shop.setShopImg(shopImgAddr);
    }

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail)
            throws ShopOperationException {
        try {
            if (shop == null || shop.getShopId() == null) {
                return new ShopExecution(ShopStateEnum.NULL_SHOP);
            }
            //判断是否有图片要更新
            if (thumbnail != null && thumbnail.getImage() != null && thumbnail.getImageName() != null && !thumbnail.getImageName().equals("")) {
                //这只是临时的变量，用于删除图片的变量
                Shop tempShop = shopDao.queryByShopId(shop.getShopId());
//				删除图片
                if (tempShop.getShopImg() != null) {
                    ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                }
                //在本地存下图片并更新
                addShopImg(shop, thumbnail);
            }
            shop.setLastEditTime(new Date());
            int effectedNum = shopDao.updateShop(shop);
            if (effectedNum <= 0) {
                return new ShopExecution(ShopStateEnum.INNER_ERROR);
            } else {
                shop = shopDao.queryByShopId(shop.getShopId());
                return new ShopExecution(ShopStateEnum.SUCCESS, shop);
            }
        } catch (Exception e) {
            System.out.println("抛出异常操作失败");
            throw new ShopOperationException("修改店铺失败：" + e.getMessage());
        }

    }

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = null;
        if (shopList != null) {
            se = new ShopExecution(ShopStateEnum.SUCCESS, shopList);
            se.setCount(count);
        } else {
            se = new ShopExecution(ShopStateEnum.INNER_ERROR);
        }
        return se;
    }


}
