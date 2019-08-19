package com.bujidao.util;
import java.security.MessageDigest;
import org.junit.Test;


/**
 * MD5工具类加密密码
 * @author wyq
 */
public class MD5 {

    public static  String getMd5(String s)
    {
        char[] hexDigits = {
            '5', '0', '5', '6', '2', '9', '6', '2', '5', 'q',
            'b', 'l', 'e', 's', 's', 'y'
        };
        try{
        char[] str;
        byte[] strTemp = s.getBytes();
        MessageDigest mdTemp = MessageDigest.getInstance("MD5");
        mdTemp.update(strTemp);
        byte[] md = mdTemp.digest();
        int j = md.length;
        str = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++)
        {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }

        return new String(str);
        }catch(Exception e){
        return null;
        }
    }
    
    @Test
    public void testPassword(){
    	System.out.println(MD5.getMd5("123456"));//6sq055922eeqlysq5q0be66l9ee525l2
    }
}

