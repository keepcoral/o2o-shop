package com.bujidao.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {
	/**
	 * o2o首页路由
	 */
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String toIndex(){
		return "frontend/index";
	}
	
	@RequestMapping(value="/shoplist",method=RequestMethod.GET)
	public String showShopList(){
		return "frontend/shoplist";
	}
	
	@RequestMapping(value="/shopdetail",method=RequestMethod.GET)
	public String showShopDetail(){
		return "frontend/shopdetail";
	}
	
	@RequestMapping(value="/productdetail",method=RequestMethod.GET)
	public String productDetail(){
		return "frontend/productdetail";
	}
}
