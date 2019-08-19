package com.bujidao.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer{
	private String[] encryptPropNames={"jdbc.username","jdbc.password"};

	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		//是否包含所需解密的字段
		if(isEncryptProp(propertyName)){
			String decryptValue=DESUtil.getDecryptString(propertyValue);
//			System.out.println(decryptValue);
			return decryptValue;
		}else{
			return propertyValue;
		}
	}
	
	private boolean isEncryptProp(String propertyName) {
		for(String str:encryptPropNames){
			if(propertyName.equals(str)){
				return true;
			}
		}
		return false;
	}
}
