package com.bujidao.web.weixin;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bujidao.dto.UserAccessToken;
import com.bujidao.dto.WechatAuthExecution;
import com.bujidao.dto.WechatUser;
import com.bujidao.entity.PersonInfo;
import com.bujidao.entity.WechatAuth;
import com.bujidao.enums.WechatAuthStateEnum;
import com.bujidao.service.PersonInfoService;
import com.bujidao.service.WechatAuthService;
import com.bujidao.util.weixin.WechatUtil;

@Slf4j
@Controller
@RequestMapping("wechatlogin")
/**
 * 获取关注公众号之后的微信用户信息的接口，如果在微信浏览器里访问
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf5147cbc3f0e7700&redirect_uri=http://139.224.134.99/o2o_test/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * 则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 * @author wyq
 */
public class WechatLoginController {

	public static final String FRONTEND = "1";
	public static final String SHOPEND = "2";
	@Autowired
	private PersonInfoService personInfoService;

	@Autowired
	private WechatAuthService wechatAuthService;

	@RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.info("weixin login get...");
		// 获取微信公众号传输过来的code,通过code可获取access_token,进而获取用户信息
		String code = request.getParameter("code");
		// 这个state可以用来传我们自定义的信息，方便程序调用，这里也可以不用
		// 这里可用于判断是店家还是普通用户
		String roleType = request.getParameter("state");
		log.info("weixin login code:" + code);
		WechatUser user = null;
		String openId = null;
		WechatAuth wechatAuth = null;
		if (null != code) {
			UserAccessToken token;
			try {
				// 通过微信发过来的code获取access_token
				token = WechatUtil.getUserAccessToken(code);
				log.info("weixin login token:" + token.toString());
				// 通过token获取accessToken
				String accessToken = token.getAccessToken();
				// 通过token获取openId
				openId = token.getOpenId();
				// 通过access_token和openId获取用户昵称等信息
				// 这个user是包含微信所有的信息
				user = WechatUtil.getUserInfo(accessToken, openId);
				log.info("weixin login user:" + user.toString());
				request.getSession().setAttribute("openId", openId);
				// 通过openId获取wechatAuth
				wechatAuth = wechatAuthService.getWechatAuthById(openId);
			} catch (IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
				e.printStackTrace();
			}
		}
		if (wechatAuth == null) {
			// 通过user创建personInfo
			PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
			if (FRONTEND.equals(roleType)) {
				personInfo.setUserType(1);
			} else {
				personInfo.setUserType(2);
			}
			wechatAuth = new WechatAuth();
			wechatAuth.setOpenId(openId);
			wechatAuth.setPersonInfo(personInfo);
			WechatAuthExecution we = wechatAuthService.regist(wechatAuth);
			if(we.getState()!=WechatAuthStateEnum.SUCCESS.getState()){
				return null;
			}else{
				personInfo= personInfoService.getPersonInfo(wechatAuth.getPersonInfo().getUserId());
				//设置为登陆状态
				request.getSession().setAttribute("user", personInfo);
			}
		}else{
			//如果存在wechatAuth，直接登陆
			PersonInfo personInfo= personInfoService.getPersonInfo(wechatAuth.getPersonInfo().getUserId());
			//设置为登陆状态
			request.getSession().setAttribute("user", personInfo);
			
		}
		//点击的是前端展示系统则进入前端展示系统
		if (FRONTEND.equals(roleType)) {
			return "frontend/index";
		} else {
			return "shop/shoplist";
		}

	}
}
