package com.bujidao.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bujidao.entity.Product;
import com.bujidao.service.ProductService;
import com.bujidao.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ProductDetailController {
	@Autowired
	private ProductService productService;
	
	@RequestMapping("/listproductdetailpageinfo")
	@ResponseBody
	public Map<String,Object> listProductDetailPageInfo(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String, Object>();
		long productId=HttpServletRequestUtil.getLong(request, "productId");
		Product product = null;
		if(productId!=-1){
			product = productService.getProductById(productId);
			if(product!=null)
			{
				modelMap.put("success", true);
				modelMap.put("product", product);
			}else{
				modelMap.put("success", false);
				modelMap.put("errMsg", "商品不存在！");
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "商品不存在！");
		}
		return modelMap;
	}
}
