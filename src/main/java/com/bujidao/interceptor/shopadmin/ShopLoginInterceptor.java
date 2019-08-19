package com.bujidao.interceptor.shopadmin;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bujidao.entity.PersonInfo;



public class ShopLoginInterceptor extends HandlerInterceptorAdapter{
	private Logger logger=LoggerFactory.getLogger(ShopLoginInterceptor.class);
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
//		logger.debug("登陆拦截器已拦截！");
		Object userObj=request.getSession().getAttribute("user");
		if(userObj!=null){
			PersonInfo user=(PersonInfo) userObj;
			if(user!=null&&user.getUserId()!=null
					&&user.getUserId()>0&&user.getEnableStatus()==1){
				return true;
			}
		}
		//如果不满足登陆这跳转到登陆页面
		PrintWriter pw=response.getWriter();
		pw.println("<html>");
		pw.println("<script>");
		pw.println("window.open('"+request.getContextPath()+"/local/login?usertype=2','_self')");
		pw.println("</script>");
		pw.println("</html>");
		return false;
	}
	
}
