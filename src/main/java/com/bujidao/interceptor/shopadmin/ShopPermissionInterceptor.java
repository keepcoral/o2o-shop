package com.bujidao.interceptor.shopadmin;


import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bujidao.entity.PersonInfo;
import com.bujidao.entity.Shop;
import com.bujidao.util.HttpServletRequestUtil;

public class ShopPermissionInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
		List<Shop> shopList=(List<Shop>) request.getSession().getAttribute("shopList");
		if(currentShop==null) return false;
		for(Shop shop:shopList){
			//如果当前店铺在可操作列表中则返回true
			if(shop.getShopId()==currentShop.getShopId()){
				return true;
			}
		}
		System.out.println("当前店铺不能操作！");
		return false;
	}
	
	
}
