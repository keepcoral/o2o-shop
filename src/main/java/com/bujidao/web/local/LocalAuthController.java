package com.bujidao.web.local;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bujidao.dto.LocalAuthExecution;
import com.bujidao.entity.LocalAuth;
import com.bujidao.entity.PersonInfo;
import com.bujidao.enums.LocalAuthStateEnum;
import com.bujidao.service.LocalAuthService;
import com.bujidao.util.CodeUtil;
import com.bujidao.util.HttpServletRequestUtil;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/local", method = { RequestMethod.GET, RequestMethod.POST })
public class LocalAuthController {
	private Logger logger = LoggerFactory.getLogger(LocalAuthController.class);
	@Autowired
	private LocalAuthService localAuthService;

	/**
	 * 绑定账户
	 * 
	 */
	@RequestMapping(value = "/bindlocalauth", method = { RequestMethod.POST })
	public Map<String, Object> bindLocalAuth(String username,String password,HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		if (username != null && password != null && user != null && user.getUserId() != null) {
			LocalAuth localAuth = new LocalAuth();
			localAuth.setPersonInfo(user);
			localAuth.setUsername(username);
			localAuth.setPassword(password);
			LocalAuthExecution le = localAuthService.bindLocalAuth(localAuth);
			if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", le.getStateInfo());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码均不能为空");
		}
		return modelMap;
	}

	/**
	 * 更改密码
	 */
	@RequestMapping(value = "/changelocalpwd", method = RequestMethod.POST)
	public Map<String, Object> changeLocalPwd(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		String username = HttpServletRequestUtil.getString(request, "username");
		String password = HttpServletRequestUtil.getString(request, "password");
		String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		if (username != null && password != null && newPassword != null 
				&& user != null && user.getUserId() != null
				&& !password.equals(newPassword)) {
			try {
				LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
				if (localAuth == null ) {
					modelMap.put("success", false);
					modelMap.put("errMsg", "输入的账号并非当前用户");
					return modelMap;
				}
				LocalAuthExecution le = localAuthService.modifyLocalAuthPassword(user.getUserId(), username, password,
						newPassword);
				if (le.getState() != LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success", false);
					modelMap.put("errMsg", le.getStateInfo());
				} else {
					modelMap.put("success", true);
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入用户名和旧密码");
		}
		return modelMap;
	}

	/**
	 * 登陆验证
	 */
	@RequestMapping(value = "/logincheck", method = RequestMethod.POST)
	public Map<String, Object> loginCheck(String username,String password,HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取是否需要验证码校验
		boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
		if (needVerify && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		if(StringUtils.isEmpty(username.trim())||StringUtils.isEmpty(password.trim())){
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码都不能为空");
			return modelMap;
		}
		if (username != null && password != null) {
			LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, password);
			// 不为空则登陆成功
			if (localAuth != null) {
				modelMap.put("success", true);
				request.getSession().setAttribute("user", localAuth.getPersonInfo());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户名或者密码错误");
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码都不能为空");
		}
		return modelMap;
	}

	/**
	 * 注销用户
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public Map<String, Object> logout(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		request.getSession().setAttribute("user", null);
		modelMap.put("success", true);
		return modelMap;
	}


	/**
	 * 	判断是否已登陆
	 */
	@RequestMapping(value="/islogined",method = { RequestMethod.GET })
	public Map<String,Object> isLogined(HttpServletRequest request){
		Map<String, Object>modelMap=new HashMap<String, Object>();
		PersonInfo user=(PersonInfo) request.getSession().getAttribute("user");
		if(user==null||user.getUserId()==null||user.getUserId()<=0){
			modelMap.put("success", false);
		}else{
			modelMap.put("success", true);
		}
		return modelMap;
	}


}
