package com.bujidao.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.bujidao.dto.ImageHolder;
import com.bujidao.dto.ProductExecution;
import com.bujidao.entity.Product;
import com.bujidao.entity.ProductCategory;
import com.bujidao.entity.Shop;
import com.bujidao.enums.ProductStateEnum;
import com.bujidao.exception.ProductOperationException;
import com.bujidao.service.ProductCategoryService;
import com.bujidao.service.ProductService;
import com.bujidao.util.CodeUtil;
import com.bujidao.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value="/shopadmin")
public class ProductManagementController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	//最大详情图片数量
	private static final int IMAGE_MAX_COUNT=6;
	
	@RequestMapping(value="/addproduct",method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProduct(HttpServletRequest request){
		Map<String, Object> modelMap=new HashMap<String, Object>();
		if(!CodeUtil.checkVerifyCode(request)){
			modelMap.put("success", false);
			modelMap.put("errMsg","验证码错误");
			return modelMap;
		}
		//接受前端参数的变量初始化，包括商品，缩略图，详情图片的实体类
		ObjectMapper mapper=new ObjectMapper();
		Product product=new Product();
		String productStr=HttpServletRequestUtil.getString(request, "productStr");
		MultipartHttpServletRequest multipartHttpServletRequest=null;
		ImageHolder thumbnail=null;
		List<ImageHolder> productImgHolderList=new ArrayList<ImageHolder>();
		//从session中获取到文件流
		CommonsMultipartResolver multipartResolver=
				new CommonsMultipartResolver(request.getSession().getServletContext());
		
		try{
			//判断请求中是否存在文件流，包括缩略图和详情图
			if(multipartResolver.isMultipart(request)){
				thumbnail = handleImage(request, thumbnail, productImgHolderList);
			}else{
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传图片不能为空");
				return modelMap;
			}
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg","上传图片失败:"+e.getMessage());
			return modelMap;
		}
		
		try{
			product=mapper.readValue(productStr, Product.class);
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg","上传图片失败:"+e.getMessage());
			return modelMap;
		}
		
		if(product!=null && thumbnail!=null && productImgHolderList.size()>0){
			try{
				Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				ProductExecution pe=
						productService.addProduct(product, thumbnail, productImgHolderList);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState()){
					modelMap.put("success",true);
				}else{
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			}catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息");
		}
		return modelMap;
	}

	/**
	 * 处理图片，缩略图以及详情图片
	 */
	private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail,
			List<ImageHolder> productImgHolderList) throws IOException {
		MultipartHttpServletRequest multipartHttpServletRequest;
		multipartHttpServletRequest=(MultipartHttpServletRequest) request;
		//从multipartHttpServletRequest取出文件流
		
		//取出缩略图thumbnailFile
		CommonsMultipartFile thumbnailFile=
				(CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
		//不为空则转换为ImageHolder
		if(thumbnailFile != null){
			thumbnail=new ImageHolder
					(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
		}
		//取出详情图片，并构造productImgHolderList
		for(int i=0;i<IMAGE_MAX_COUNT;i++){
			//取出图片
			CommonsMultipartFile productImgFile=
					(CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg"+i);
			if(productImgFile!=null){
				ImageHolder productImgHolder=new ImageHolder
						(productImgFile.getOriginalFilename(),productImgFile.getInputStream());
				productImgHolderList.add(productImgHolder);
			}else{
				break;
			}
		}
		return thumbnail;
	}

	/**
	 * 通过id查找product和该店铺下的productCategory
	 * 如果没有@RequestParam Long productId 这个参数，那么页面就会报错
	 * 	HTTP Status 400 - Required Long parameter 'productId' is not present
	 */
	@RequestMapping(value="/getproductbyid",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getPorductById(@RequestParam Long productId){
		Map<String,Object> modelMap=new HashMap<String, Object>();
		if(productId > -1){
			Product product=productService.getProductById(productId);
			List<ProductCategory> productCategoryList=productCategoryService
					.getProductCategoryList(product.getShop().getShopId());
			modelMap.put("success",true);
			modelMap.put("productCategoryList",productCategoryList);
			modelMap.put("product", product);
		}else{
			modelMap.put("success",true);
			modelMap.put("errMsg", "empty productId");
		}
		return modelMap;
	}
	
	/**
	 * 商品编辑
	 */
	@RequestMapping(value="/modifyproduct",method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProduct(HttpServletRequest request){
		Map<String, Object> modelMap=new HashMap<String, Object>();
		//判断是商品编辑还是商品上下架
		//若为前者则需要验证码，后者不需要
		boolean statusChange =
				HttpServletRequestUtil.getBoolean(request, "statusChange");
		if(!statusChange && !CodeUtil.checkVerifyCode(request)){
			modelMap.put("success", false);
			modelMap.put("errMsg","输入了错误的验证码");
			return modelMap;
		}
		//接受前端参数的变量初始化，包括商品，缩略图，详情图片的实体类
		ObjectMapper mapper=new ObjectMapper();
		Product product=new Product();
		MultipartHttpServletRequest multipartHttpServletRequest=null;
		ImageHolder thumbnail=null;
		List<ImageHolder> productImgHolderList=new ArrayList<ImageHolder>();
		//从session中获取到文件流
		CommonsMultipartResolver multipartResolver=
				new CommonsMultipartResolver(request.getSession().getServletContext());
		try{
			if(multipartResolver.isMultipart(request)){
				System.out.println("controller层有缩略图");
				thumbnail = handleImage(request, thumbnail, productImgHolderList);
			}
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "修改商品失败！"+e.getMessage());
			return modelMap;
		}
		
		try{
			String productStr=HttpServletRequestUtil.getString(request, "productStr");
			product=mapper.readValue(productStr,Product.class);
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "product传入失败！"+e.getMessage());
			return modelMap;
		}
		
		if(product!=null){
			try{
				Shop currentShop=
						(Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				ProductExecution pe=
						productService.modifyProduct(product, thumbnail, productImgHolderList);
				if(pe.getState()==ProductStateEnum.SUCCESS.getState()){
					modelMap.put("success", true);
				}else{
					modelMap.put("success", true);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			}catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
			
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息！");
		}
		
		return modelMap;
	}

	@RequestMapping(value="/getproductlistbyshop",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductListByShop(HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<>();
		int pageIndex=HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize=HttpServletRequestUtil.getInt(request, "pageSize");
		Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
		
		if(pageIndex>-1 && pageSize>-1 && currentShop!=null && currentShop.getShopId()!=null){
			//模糊查询的查询条件
			long productCategoryId=HttpServletRequestUtil.getLong(request, "productCategoryId");
			String productName=HttpServletRequestUtil.getString(request, "productName");
			Product productCondition=compactProductCondition(currentShop.getShopId()
					, productCategoryId, productName);
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		}else{
			modelMap.put("success",false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
	
	
	/**
	 * 根据传入的条件封装productCondition
	 */
	private Product compactProductCondition(long shopId,long productCategoryId,String productName){
		Product productCondition=new Product();
		Shop shop=new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		
		if(productCategoryId != -1){
			ProductCategory productCategory=new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		
		if(productName != null){
			productCondition.setProductName(productName);
		}
		
		return productCondition;
	}
}

