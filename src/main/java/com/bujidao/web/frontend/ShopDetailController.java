package com.bujidao.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bujidao.dto.ProductExecution;
import com.bujidao.entity.Product;
import com.bujidao.entity.ProductCategory;
import com.bujidao.entity.Shop;
import com.bujidao.service.ProductCategoryService;
import com.bujidao.service.ProductService;
import com.bujidao.service.ShopService;
import com.bujidao.util.HttpServletRequestUtil;


@Slf4j
@Controller
@RequestMapping("/frontend")
public class ShopDetailController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private ProductService productService;
	
	/**
	 * 获取店铺信息以及店铺下的商铺类别列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshopdetailpageinfo",method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> listShopDetailPageInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		Shop shop = null;
		List<ProductCategory> productCategoryList = null;
		if(shopId != -1){
			//通过店铺ID获取店铺信息
			shop = shopService.getByShopId(shopId);
			//通过店铺ID获取商铺下面的所有商铺类别
			productCategoryList = productCategoryService.getProductCategoryList(shopId);
			modelMap.put("shop", shop);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "获取店铺ID失败");
		}
		return modelMap;
	}
	
	/**
	 * 获取该店铺下的商品信息列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listproductsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> listProductByShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (pageIndex > -1 && pageSize > -1 && shopId != -1){
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			String productName = HttpServletRequestUtil.getString(request, "productName");
			//组合查询信息，放入到productCondition里面
			Product productCondition = compactPorductCondition4Search(shopId, productCategoryId, productName);
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("success", true);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg","empty pageIndex or pageSize or shopId");
		}
		return modelMap;
	}
	
	/**
	 * 组合查询信息，放入到productCondition里面
	 * @param shopId
	 * @param productCategoryId
	 * @param productName
	 * @return
	 */
	private Product compactPorductCondition4Search(long shopId, long productCategoryId, String productName) {
		Product productCondition = new Product();
		if(shopId != -1) {
			Shop shop = new Shop();
			shop.setShopId(shopId);
			productCondition.setShop(shop);
		}
		if(productCategoryId != -1){
			ProductCategory productCategory=new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if(productName != null){
			productCondition.setProductName(productName);
		}
		//只有通过审核的商品才能被查看
		productCondition.setEnableStatus(1);
		return productCondition;
	}
}
