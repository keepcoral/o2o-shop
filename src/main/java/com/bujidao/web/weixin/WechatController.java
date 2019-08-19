package com.bujidao.web.weixin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bujidao.entity.PersonInfo;
import com.bujidao.entity.WechatAuth;
import com.bujidao.util.weixin.SignUtil;


/**
 * 做wechat应答
 * @author wyq
 *
 */
@Controller
@RequestMapping("wechat")
public class WechatController {

	private static Logger log = LoggerFactory.getLogger(WechatController.class);

	@RequestMapping(method = { RequestMethod.GET })
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("weixin get...");
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		log.debug("微信加密签名----"+signature+"时间戳----"+timestamp+"随机数----"+nonce+"随机字符串----"+echostr+"响应头："+response);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if (SignUtil.checkSignature(signature, timestamp, nonce)) {
				log.debug("weixin get success....");
				out.print(echostr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}

}
