package com.bujidao.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class CodeUtil {
	private static Logger logger=LoggerFactory.getLogger(CodeUtil.class);
	
	/**
	 * 验证码校验
	 * @param request
	 * @return
	 */
	public static boolean checkVerifyCode(HttpServletRequest request){
		String verifyCodeExpected=(String) request.getSession()
				.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		String verifyCodeActual=HttpServletRequestUtil.getString(request, "verifyCodeActual");
		logger.debug("验证码是"+verifyCodeExpected.toLowerCase());
		logger.debug("我输入的是"+(verifyCodeActual==null?null:verifyCodeActual.toLowerCase()));
//		logger.debug(""+verifyCodeActual.toLowerCase().equals(verifyCodeExpected.toLowerCase()));
		if(verifyCodeActual == null || !verifyCodeActual.toLowerCase().equals(verifyCodeExpected.toLowerCase())){
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * 生成二维码图片流
	 * @return
	 */
	public static BitMatrix generateQRCodeStream(String content,HttpServletResponse response){
		//给响应头添加信息，主要告诉浏览器返回的是图片流
		response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragma","no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/png");
		//设置图片的文字编码以及内边框距
		Map<EncodeHintType, Object> hints=new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.MARGIN, 0);
		BitMatrix bitMatrix=null;
		try{
			//参数分别为：编码内容，编码类型，图片宽度，图片高度，设置参数
			bitMatrix=new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300,300,hints);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return bitMatrix;
	}
}
