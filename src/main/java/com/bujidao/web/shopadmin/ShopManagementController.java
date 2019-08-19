package com.bujidao.web.shopadmin;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.bujidao.dto.ImageHolder;
import com.bujidao.dto.ShopExecution;
import com.bujidao.entity.Area;
import com.bujidao.entity.PersonInfo;
import com.bujidao.entity.Shop;
import com.bujidao.entity.ShopCategory;
import com.bujidao.enums.ProductCategoryStateEnum;
import com.bujidao.enums.ShopStateEnum;
import com.bujidao.exception.ShopOperationException;
import com.bujidao.service.AreaService;
import com.bujidao.service.ShopCategoryService;
import com.bujidao.service.ShopService;
import com.bujidao.util.CodeUtil;
import com.bujidao.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	Logger logger=LoggerFactory.getLogger(ShopManagementController.class);
	@Autowired
	private ShopService shopService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	/**
	 * 店铺管理
	 */
	@RequestMapping(value="/getshopmanagementinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String ,Object> getShopManagementInfo(HttpServletRequest request){
		Map<String, Object> modelMap=new HashMap<String, Object>();
		long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId<=0){
			Object currentShopObj=request.getSession().getAttribute("currentShop");
			//不经过登陆的话
			if(currentShopObj==null){
				modelMap.put("redirect", true);
				modelMap.put("url","/o2o_test/shopadmin/shoplist");
			}else{
				Shop currentShop=(Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		}else{
			Shop currentShop=new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop",currentShop);
			modelMap.put("shopId", shopId);
		}
		return modelMap;
	}
	/**
	 * 获取店铺列表
	 */
	@RequestMapping(value="/getshoplist",method=RequestMethod.GET)
	@ResponseBody
	private Map<String ,Object> getShopList(HttpServletRequest request){
		Map<String ,Object> modelMap=new HashMap<String, Object>();
		PersonInfo user=(PersonInfo) request.getSession().getAttribute("user");
		try{
			Shop shopCondition=new Shop();
			shopCondition.setOwner(user);
			ShopExecution se=shopService.getShopList(shopCondition, 0, 100);
			request.getSession().setAttribute("shopList", se.getShopList());
			modelMap.put("shopList", se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	/**
	 * 返回添加商店时候的初始信息
	 */
	@RequestMapping(value="/getshopinitinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String ,Object> getShopInitInfo(){
		Map<String, Object> modelMap=new HashMap<String, Object>();
		List<Area>	areaList=null;
		List<ShopCategory> shopCategoryList=null;
		try {
			areaList=areaService.queryArea();
			shopCategoryList=shopCategoryService.getShopCategory(new ShopCategory());
			modelMap.put("areaList",areaList);
			modelMap.put("shopCategoryList",shopCategoryList);
			modelMap.put("success",true);
		} catch (Exception e) {
			modelMap.put("success",false);
			modelMap.put("errMsg", e.getMessage());
			e.printStackTrace();
		}
		return modelMap;
	}
	/**
	 * 注册店铺
	 * 前端将所有的信息封装到httpServletRequest
	 */
	@RequestMapping(value="/registershop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String ,Object> registerShop(HttpServletRequest request){
		Map<String,Object>modelMap=new HashMap<String, Object>();
		if(!CodeUtil.checkVerifyCode(request)){
			modelMap.put("success",false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//和前端约定好用shopStr取值，包括店铺信息以及图片信息，利用该字符串转换为实体类
		String shopStr=HttpServletRequestUtil.getString(request, "shopStr");
		Shop shop=null;
		ObjectMapper mapper=new ObjectMapper();
		try {
			//利用该jackson串转换为实体类
			shop=mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			//转换失败则返回前台
			modelMap.put("success",false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//将前台的文件流，用CommonsMultipartResolver去解析，解析到CommonsMultipartFile
		CommonsMultipartFile shopImg=null;
		CommonsMultipartResolver commonsMultipartResolver=
				new CommonsMultipartResolver(request.getSession().getServletContext());
		//判断是否包含图片，如果包含，那么取出图片
		if(commonsMultipartResolver.isMultipart(request)){
			MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest) request;
			shopImg=(CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}else{
			modelMap.put("success",false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap	;
		}
		
		if(shop!=null&&shopImg!=null){
			//对店铺的操作需要先登陆
			PersonInfo owner=(PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			ShopExecution se=null;
			try {
				ImageHolder thumbnail=
						new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
				se = shopService.addShop(shop, thumbnail);
				if(se.getState()==ShopStateEnum.CHECK.getState()){
					modelMap.put("success",true);
					//取出可以操作的店铺，在添加新的店铺
					@SuppressWarnings("unchecked")//没有警告信息
					List<Shop> shopList=(List<Shop>) request.getSession().getAttribute("shopList");
					if(shopList==null||shopList.size()==0){
						shopList=new ArrayList<Shop>();
					}
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList", shopList);
				}else{
					modelMap.put("success",false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success",false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success",false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		}else{
			modelMap.put("success",false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap	;
		}

	}
	
	/**
	 *	从前端获取shopId，在读取数据库
	 */
	@RequestMapping(value="/getshopbyid",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getShopById(HttpServletRequest request){
		Map<String, Object> modelMap=new HashMap<String, Object>();
		long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId>-1){
			try {
				Shop shop=shopService.getByShopId(shopId);
				List<Area> areaList=areaService.queryArea();
				modelMap.put("success", true);
				modelMap.put("areaList", areaList);
				modelMap.put("shop", shop);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
	@RequestMapping(value="/modifyshop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String ,Object> modifyShop(HttpServletRequest request){
		Map<String,Object>modelMap=new HashMap<String, Object>();
		if(!CodeUtil.checkVerifyCode(request)){
			modelMap.put("success",false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//和前端约定好用shopStr取值，包括店铺信息以及图片信息，利用该字符串转换为实体类
		String shopStr=HttpServletRequestUtil.getString(request, "shopStr");
		Shop shop=null;
		ObjectMapper mapper=new ObjectMapper();
		try {
			//利用该jackson串转换为实体类
			shop=mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			//转换失败则返回前台
			modelMap.put("success",false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//将前台的文件流，用CommonsMultipartResolver去解析，解析到CommonsMultipartFile
		CommonsMultipartFile shopImg=null;
		CommonsMultipartResolver commonsMultipartResolver=
				new CommonsMultipartResolver(request.getSession().getServletContext());
		//判断是否包含图片，如果包含，那么取出图片
		if(commonsMultipartResolver.isMultipart(request)){
			MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest) request;
			shopImg=(CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}else{
			modelMap.put("success",false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap	;
		}
		//修改店铺
		if(shop!=null&&shop.getShopId()!=null){
			
			ShopExecution se=null;
			try {
				if(shopImg==null){
					se = shopService.modifyShop(shop,null);
				}else{
					ImageHolder thumbnail=
							new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
					se = shopService.modifyShop(shop,thumbnail);
				}
				if(se.getState()==ProductCategoryStateEnum.SUCCESS.getState()){
					modelMap.put("success",true);
				}else{
					modelMap.put("success",false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success",false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success",false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		}else{
			modelMap.put("success",false);
			modelMap.put("errMsg", "请输入店铺id");
			return modelMap	;
		}

	}
}
