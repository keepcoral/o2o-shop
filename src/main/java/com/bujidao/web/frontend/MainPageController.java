package com.bujidao.web.frontend;

import java.util.ArrayList;
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

import com.bujidao.entity.HeadLine;
import com.bujidao.entity.ShopCategory;
import com.bujidao.service.HeadLineService;
import com.bujidao.service.ShopCategoryService;
import com.bujidao.web.shopadmin.ProductCategoryManagementController;

@Controller
@RequestMapping("/frontend")
public class MainPageController {
	private Logger logger = LoggerFactory.getLogger(MainPageController.class);
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private HeadLineService headLineService;
	
	@RequestMapping(value="/listmainpageinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> listMainPageInfo(HttpServletRequest request){
		Map<String, Object> modelMap=new HashMap<String, Object>();
		//获取一级店铺类别，即parentId为空的ShopCategory
		List<ShopCategory> shopCategoryList=new ArrayList<ShopCategory>();
		try{
			shopCategoryList=shopCategoryService.getShopCategory(null);
			modelMap.put("shopCategoryList", shopCategoryList);
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		
		List<HeadLine> headLineList=new ArrayList<HeadLine>();
		try{
			HeadLine headLineCondition=new HeadLine();
			//查询可用的头条
			headLineCondition.setEnableStatus(1);
			headLineList=headLineService.getHeadLineList(headLineCondition);
			modelMap.put("headLineList", headLineList);
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}
}
