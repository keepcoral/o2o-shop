package com.bujidao.web.shopadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bujidao.dto.ProductCategoryExecution;
import com.bujidao.dto.Result;
import com.bujidao.entity.ProductCategory;
import com.bujidao.entity.Shop;
import com.bujidao.enums.ProductCategoryStateEnum;
import com.bujidao.exception.ProductCategoryOperationException;
import com.bujidao.service.ProductCategoryService;

@Slf4j
@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
	private Logger logger = LoggerFactory.getLogger(ProductCategoryManagementController.class);
	@Autowired
	private ProductCategoryService productCategoryService;

	/**
	 * 获取商店中商品的类别的List
	 */
	@RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
	@ResponseBody
	private Result<List<ProductCategory>> getProductCategory(HttpServletRequest request) {
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		List<ProductCategory> list = null;
		if (currentShop != null && currentShop.getShopId() > 0) {
			logger.debug("id为" + currentShop.getShopId());
			list = productCategoryService.getProductCategoryList(currentShop.getShopId());
			return new Result<List<ProductCategory>>(true, list);
		} else {
			ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
			return new Result<List<ProductCategory>>(false, ps.getState(), ps.getStateInfo());
		}
	}

	/**
	 * 批量添加ProductCategory
	 * @param productCategoryList 自动接受从前端传入的List信息
	 */
	@RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProductCategory(@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		for (ProductCategory pc : productCategoryList) {
			pc.setShopId(currentShop.getShopId());
		}
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategoryList);
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("errMsg", pe.getStateInfo());
					modelMap.put("success", false);
				}

			} catch (ProductCategoryOperationException e) {
				modelMap.put("errMsg", "出现异常" + e.getMessage());
				modelMap.put("success", false);
			}
		} else {
			modelMap.put("errMsg", "至少输入一个商品类别");
			modelMap.put("success", false);
		}
		return modelMap;
	}
	
	/**
	 * 删除商品类别
	 */
	@RequestMapping(value="/removeproductcategory")
	@ResponseBody
	private Map<String, Object> removeProductCategory(Long productCategoryId,
			HttpServletRequest request) {
		Map<String, Object> modelMap=new HashMap<String, Object>();
		if(productCategoryId!=null && productCategoryId>0){
			logger.debug("delete方法已调用");
			try{
				Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
				logger.debug("currentshop为:"+(currentShop==null?"空":currentShop.getShopId()));
				ProductCategoryExecution pe=productCategoryService.deleteProductCategory
						(productCategoryId, currentShop.getShopId());
				if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState()){
					modelMap.put("success",true);
				}else{
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
				return modelMap;
			}catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "删除操作失败！"+e.getMessage());
				return modelMap;
			}
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "至少选择一个商品类别");
		}
		return modelMap;
	}

	
}
