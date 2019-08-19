package com.bujidao.web.shopadmin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bujidao.dto.ShopAuthMapExecution;
import com.bujidao.dto.UserAccessToken;
import com.bujidao.dto.WechatInfo;
import com.bujidao.entity.PersonInfo;
import com.bujidao.entity.Shop;
import com.bujidao.entity.ShopAuthMap;
import com.bujidao.entity.WechatAuth;
import com.bujidao.enums.ShopAuthMapStateEnum;
import com.bujidao.service.PersonInfoService;
import com.bujidao.service.ShopAuthMapService;
import com.bujidao.service.WechatAuthService;
import com.bujidao.util.CodeUtil;
import com.bujidao.util.HttpServletRequestUtil;
import com.bujidao.util.baidu.BaiduDwzUtil;
import com.bujidao.util.weixin.WechatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Controller
@RequestMapping("/shopadmin")
public class ShopAuthManagementController {
	Logger logger = LoggerFactory.getLogger(ShopAuthManagementController.class);

	@Autowired
	private ShopAuthMapService shopAuthMapService;

	@Autowired
	private WechatAuthService wechatAuthService;

	@Autowired
	private PersonInfoService personInfoService;
	// 微信api前缀
	private static String urlPrefix;
	// 微信api中间部分
	private static String urlMiddle;
	// 微信api后缀
	private static String urlSuffix;
	// 微信回传响应添加授权信息的url
	private static String authUrl;

	@Value("${wechat.prefix}")
	public void setUrlPrefix(String urlPrefix) {
		ShopAuthManagementController.urlPrefix = urlPrefix;
	}

	@Value("${wechat.middle}")
	public void setUrlMiddle(String urlMiddle) {
		ShopAuthManagementController.urlMiddle = urlMiddle;
	}

	@Value("${wechat.suffix}")
	public void setUrlSuffix(String urlSuffix) {
		ShopAuthManagementController.urlSuffix = urlSuffix;
	}

	@Value("${wechat.auth.url}")
	public void setAuthUrl(String authUrl) {
		ShopAuthManagementController.authUrl = authUrl;
	}

	@RequestMapping(value = "/listshopauthmapsbyshop", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listShopAuthMapsByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		System.out.println("在listShopAuthMapsByShop中的user为"+(PersonInfo)request.getSession().getAttribute("user"));
		if (pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null) {
			ShopAuthMapExecution se = shopAuthMapService.listShopAuthMapByShopId(currentShop.getShopId(), pageIndex,
					pageSize);
			modelMap.put("shopAuthMapList", se.getShopAuthMapList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageIndex or pageSize or shopId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/getshopauthmapbyid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopAuthMapById(@RequestParam Long shopAuthId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (shopAuthId != null && shopAuthId > -1) {
			ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
			modelMap.put("shopAuthMap", shopAuthMap);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopAuthId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/modifyshopauthmap", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modifyShopAuthMap(HttpServletRequest request, String shopAuthMapStr) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		ObjectMapper mapper = new ObjectMapper();
		ShopAuthMap shopAuthMap = null;
		try {
			// 利用该jackson串转换为实体类
			shopAuthMap = mapper.readValue(shopAuthMapStr, ShopAuthMap.class);
		} catch (Exception e) {
			// 转换失败则返回前台
			modelMap.put("success", false);
			modelMap.put("errMsg", "jason转换失败" + e.getMessage());
			return modelMap;
		}
		if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
			try {
				// 判断是否为店家本人
				if (!checkPermission(shopAuthMap.getShopAuthId())) {
					modelMap.put("success", false);
					modelMap.put("errMsg", "不能对店家本身做操作(已是店铺最高权限)");
					return modelMap;
				}
				System.out.println("状态为" + shopAuthMap.getEnableStatus() + "--id为" + shopAuthMap.getShopAuthId());
				System.out.println(shopAuthMapStr);
				ShopAuthMapExecution se = shopAuthMapService.modifyShopAuthMap(shopAuthMap);
				if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", "---" + se.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入要修改的信息！");
		}
		return modelMap;
	}

	public boolean checkPermission(Long shopAuthId) {
		ShopAuthMap grantedPerson = shopAuthMapService.getShopAuthMapById(shopAuthId);
		if (grantedPerson.getTitleFlag() == 0) {
			// 是店家本身，那么不能操作
			System.out.println("是店家不能修改！");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 生成带有url的二维码，微信扫一扫就能连接到对应的url里面
	 * 
	 * @param reque
	 *            st
	 * @param response
	 */
	@RequestMapping(value = "generateqrcode4shopauth", method = RequestMethod.GET)
	@ResponseBody
	public void generateQRCode4ShopAuth(HttpServletRequest request, HttpServletResponse response) {
		// 获取当前店铺
		Shop shop = (Shop) request.getSession().getAttribute("currentShop");
		if (shop != null && shop.getShopId() != null) {
			// 获取当前时间戳，保证二维码的时间有效性，精确到毫秒
			long timpStamp = System.currentTimeMillis();
			// 将id和timestamp传入content,赋值到state,这样微信获取这些信息后会回传到授权信息的添加
			// 加上aaa是为了添加信息的方法替换这些信息
			String content = "{aaashopIdaaa:" + shop.getShopId() + ",aaacreateTimeaaa:" + timpStamp + "}";
			try {
				// 增加base64防止特殊字符干扰
				String longUrl = urlPrefix + authUrl + urlMiddle 
						+ URLEncoder.encode(content, "UTF-8") + urlSuffix;
				// 将目标url转为短url，url过长会导致二维码生成失败
				logger.debug("url为:" + longUrl);
				String shortUrl = BaiduDwzUtil.createShortUrl(longUrl);
				logger.debug("短url为:" + shortUrl);
				// 传入短url生成二维码
				BitMatrix bitMatrix = CodeUtil.generateQRCodeStream(shortUrl, response);
				// 将二维码以图片流形式输出到前端
				MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据微信回传过来的参数添加店铺授权信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "addshopauthmap", method = RequestMethod.GET)
	public String addShopAuthMap(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		// 根据request里面获取微信用户信息
		WechatAuth wechatAuth = getEmployeeInfo(request);
		if (wechatAuth != null) {
			// 根据userId获取user
			PersonInfo user = personInfoService.getPersonInfo(wechatAuth.getPersonInfo().getUserId());
			// 将用户信息添加到user中
//			logger.debug("addShopAuthMap当前user为:"+(PersonInfo)request.getSession().getAttribute("user"));
//			System.out.println("在addShopAuthMap中的user为"+(PersonInfo)request.getSession().getAttribute("user"));
			request.getSession().setAttribute("user", user);
			// 因为之前进行了编码，所以这里要进行解码
			String qrCodeinfo = new String(
					URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
			ObjectMapper mapper = new ObjectMapper();
			WechatInfo wechatInfo = null;
			try {
				wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WechatInfo.class);
			} catch (Exception e) {
				return "shop/operationfail";
			}
			// 检验二维码是否过期
			if (!checkQRCodeInfo(wechatInfo)) {
				return "shop/operationfail";
			}
			// 去重校验
			ShopAuthMapExecution sae = shopAuthMapService.listShopAuthMapByShopId(wechatInfo.getShopId(), 0, 100);
			List<ShopAuthMap> shopAuthMapList = sae.getShopAuthMapList();
			for (ShopAuthMap sam : shopAuthMapList) {
				if (sam.getEmployee().getUserId() == user.getUserId()) {
					return "shop/operationfail";
				}
			}
			try {
				//根据去获取的信息添加微信授权信息
				ShopAuthMap shopAuthMap = new ShopAuthMap();
				Shop shop = new Shop();
				shop.setShopId(wechatInfo.getShopId());
				shopAuthMap.setShop(shop);
				shopAuthMap.setEmployee(user);
				shopAuthMap.setTitle("员工");
				shopAuthMap.setTitleFlag(1);
				ShopAuthMapExecution se = shopAuthMapService.addShopAuthMap(shopAuthMap);
				if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
					return "shop/operationsuccess";
				} else {
					return "shop/operationfail";
				}
			} catch (Exception e) {
				return "shop/operationfail";
			}
		}
		return "shop/operationfail";
	}

	/**
	 * 判断二维码的创建时间是否超过十分钟
	 * 
	 * @param wechatInfo
	 * @return
	 */
	public boolean checkQRCodeInfo(WechatInfo wechatInfo) {
		if (wechatInfo != null && wechatInfo.getShopId() != null && wechatInfo.getCreateTime() != null) {
			long nowTime = System.currentTimeMillis();
			if (nowTime - wechatInfo.getCreateTime() <= 600000) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 根据微信回传的code获取用户信息
	 * 
	 * @param request
	 * @return
	 */
	private WechatAuth getEmployeeInfo(HttpServletRequest request) {
		String code = request.getParameter("code");
		WechatAuth wechatAuth = null;
		if (null != code) {
			UserAccessToken token;
			try {
				token = WechatUtil.getUserAccessToken(code);
				String openId = token.getOpenId();
				request.getSession().setAttribute("openId", openId);
				wechatAuth = wechatAuthService.getWechatAuthById(openId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return wechatAuth;
	}

}
