package com.bujidao.util.baidu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShortNetAddress {
	private static Logger log = LoggerFactory.getLogger(ShortNetAddress.class);
	
	public static int TIMEOUT = 30 * 1000;
	public static String ENCODING = "UTF-8";

	/**
	 * JSON get value by key
	 * 
	 * @param replyText
	 * @param key
	 * @return
	 */
	private static String getValueByKey_JSON(String replyText, String key) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node;
		String tinyUrl = null;
		try {
			System.out.println("text为"+replyText);
			node = mapper.readTree(replyText);
			tinyUrl = node.get(key).asText();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			log.error("getValueByKey_JSON error:--JsonProcessingException" + e.toString());
//			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("getValueByKey_JSON error:--IOException" + e.toString());
		}

		return tinyUrl;
	}

	/**
	 * 通过HttpConnection 获取返回的字符串
	 * 
	 * @param connection
	 * @return
	 * @throws IOException
	 */
	private static String getResponseStr(HttpURLConnection connection)
			throws IOException {
		StringBuffer result = new StringBuffer();
		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			System.out.println("code为"+responseCode);
			InputStream in = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, ENCODING));
			String inputLine = "";
			while ((inputLine = reader.readLine()) != null) {
				result.append(inputLine);
			}
		}
		System.out.println("result为"+String.valueOf(result));
		return String.valueOf(result);
	}

	public static String getShortURL(String originURL) {
		String tinyUrl = null;
		try {
			//指定百度短链接的接口
			URL url = new URL("https://dwz.cn/admin/v2/create");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// POST Request Define:
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(TIMEOUT);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.setRequestProperty("Token", "277157bceb58f356974213a6b7e377ad"); // 设置发送数据的格式";
            
			String postData = URLEncoder.encode(originURL, "utf-8");
			connection.getOutputStream().write(("url=" + postData).getBytes());
			//连接百度短链接的接口
			connection.connect();
			String responseStr = getResponseStr(connection);
//			log.info("response string: " + responseStr);
			tinyUrl = getValueByKey_JSON(responseStr, "tinyurl");
//			log.info("tinyurl: " + tinyUrl);
			connection.disconnect();
		} catch (IOException e) {
			log.error("getshortURL error:" + e.toString());
		}
		return tinyUrl;

	}

	/**
	 * ‘ 百度短链接接口 无法处理不知名网站，会安全识别报错
	 * 
	 * @param args
	 */
	@Test
	public void testGetUrl(){
		
//		getShortURL("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2c605206217d88b5&redirect_uri=http://115.28.159.6/cityrun/wechatlogin.action&role_type=1&response_type=code&scope=snsapi_userinfo&state=STATE123qweasd#wechat_redirect");
//		getShortURL("https://baijiahao.baidu.com/s?id=1631145705675225873&wfr=spider&for=pc");
//		getShortURL("https://www.baidu.com/");
		System.out.println(getShortURL("https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login"));
	}
	
}
