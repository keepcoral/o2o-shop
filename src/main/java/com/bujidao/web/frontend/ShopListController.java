package com.bujidao.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bujidao.dto.ShopExecution;
import com.bujidao.entity.Area;
import com.bujidao.entity.Shop;
import com.bujidao.entity.ShopCategory;
import com.bujidao.service.AreaService;
import com.bujidao.service.ShopCategoryService;
import com.bujidao.service.ShopService;
import com.bujidao.util.HttpServletRequestUtil;



@Controller
@RequestMapping("/frontend")
public class ShopListController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	/**
	 * 获取店铺列表和区域信息
	 */
	@RequestMapping(value="/listshopspageinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object>listShopsPageInfo(HttpServletRequest request){
		Map<String, Object>modelMap = new HashMap<String, Object>();
		long parentId=HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList=null;
		if(parentId != -1){
			//如果parentId存在，就取出一级ShopCategory下的二级ShopCategory
			try{
				ShopCategory shopCategoryCondition = new ShopCategory();
				ShopCategory parent = new ShopCategory();
				parent.setShopCategoryId(parentId);
				shopCategoryCondition.setParent(parent);
				shopCategoryList = shopCategoryService.getShopCategory(shopCategoryCondition);
			}catch (Exception e) {
				modelMap.put("suceess", false);
				modelMap.put("获取商店列表失败(一级类别)errMsg：", e.getMessage());
				return modelMap;
			}
		} else {
			try {
				//如果parentId不存在，就取出所有一级ShopCategory
				shopCategoryList = shopCategoryService.getShopCategory(null);
			} catch (Exception e) {
				modelMap.put("suceess", false);
				modelMap.put("获取商店列表失败(二级类别)errMsg：", e.getMessage());
				return modelMap;
			}
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		List<Area> areaList = null;
		try {
			areaList = areaService.queryArea();
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("suceess", false);
			modelMap.put("获取区域信息失败", e.getMessage());
			return modelMap;
		}
		return modelMap;
	}
	
	/**
	 * 获取商铺列表
	 */
	@RequestMapping(value="/listshops",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object>listShops(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		if (pageIndex > -1 && pageSize > -1){
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			int areaId = HttpServletRequestUtil.getInt(request, "areaId");
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			//得到组合了4个条件的ShopCondition
			Shop shopCondition = compactShopCondition4Serch(parentId,shopCategoryId,areaId,shopName);
			ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("success", true);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("count", se.getCount());
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		
		return modelMap;
	}

	/**
	 * 封装组合条件到shopCondition中
	 */
	private Shop compactShopCondition4Serch(long parentId, long shopCategoryId, int areaId, String shopName) {
		Shop shopCondtion = new Shop();
		//如果parentId存在，就取出一级ShopCategory下的二级ShopCategory
		if (parentId != -1L){
			ShopCategory shopCategory = new ShopCategory();
			ShopCategory parent = new ShopCategory();
			parent.setShopCategoryId(parentId);
			shopCategory.setParent(parent);
			shopCondtion.setShopCategory(shopCategory);
		}
		//查询某个二级shopCategory下的店铺列表
		if(shopCategoryId != -1L){
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondtion.setShopCategory(shopCategory);
		}
		//按照区域查询
		if (areaId != -1){
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondtion.setArea(area);
		}
		//按照店铺名字模糊查询
		if (shopName != null){
			shopCondtion.setShopName(shopName);
		}
		//商铺都需要通过审核才行
		shopCondtion.setEnableStatus(1);
		return shopCondtion;
	}
}
