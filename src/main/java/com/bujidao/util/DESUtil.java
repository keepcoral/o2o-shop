package com.bujidao.util;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;

/**
 * 数据库密码加密
 * @author wyq
 *
 */
public class DESUtil {
	//设置密钥
	private static Key key;
	private static String KEY_STR = "myKey";
	private static String CHARSETNAME = "UTF-8";
	private static String ALGORITHM = "DES";
	static{
		try{
			//生成DES算法对象
			KeyGenerator generator=KeyGenerator.getInstance(ALGORITHM);
			//运用SHA1安全策略
			SecureRandom secureRandom=SecureRandom.getInstance("SHA1PRNG");
			//设置密钥中i在
			secureRandom.setSeed(KEY_STR.getBytes());
			//初始化基于SHA1的算法对象
			generator.init(secureRandom);
			//生成密钥对象
			key=generator.generateKey();
			generator=null;
		}catch (Exception e) {
			throw new RuntimeException();
		}
	}
	/**
	 * 获取加密后的信息,将传入的str加密
	 * @param str
	 * @return
	 */
	public static String getEncryptString(String str){
		Base64 base64 =new Base64();
		try{
			//设置编码
			byte[] bs=str.getBytes(CHARSETNAME);
			//获取加密对象
			Cipher cipher=Cipher.getInstance(ALGORITHM);
			//初始化密码信息
			cipher.init(Cipher.ENCRYPT_MODE, key);
			//加密
			byte[] doFinal=cipher.doFinal(bs);
			return base64.encodeBase64String(doFinal);
		}catch (Exception e) {
			throw new RuntimeException("加密抛出异常！");
		}
	}
	/**
	 * 解码
	 */
	public static String getDecryptString(String str){
		Base64 base64 =new Base64();
		try{
			//将str信息转换为byte[]
			byte[] bs=base64.decode(str);
			//获取解密信息
			Cipher cipher=Cipher.getInstance(ALGORITHM);
			//解密
			cipher.init(Cipher.DECRYPT_MODE, key);
			//返回解密之后的信息
			byte[] doFinal=cipher.doFinal(bs);
			return new String(doFinal,CHARSETNAME);
		}catch (Exception e) {
			throw new RuntimeException("解密抛出异常！");
		}
	}
	@Test
	public void test(){
		System.out.println(DESUtil.getEncryptString("root"));
		System.out.println(DESUtil.getEncryptString("Clmss123*"));
	}
}
