package com.bujidao.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

//只有用这个注解我们才能从application.properties中读出内容
@Configuration
public class PathUtil {
    //获取当前系统的文件分割符
    private static String seperator = System.getProperty("file.separator");
    private static String winPath = "D:/IDM缓存";
    private static String linuxPath;
    private static String shopPath;

//	@Value("${win.base.path}")
//	public void setWinPath(String winPath) {
//		PathUtil.winPath = tmp;
//	}

    @Value("${linux.base.path}")
    public void setLinuxPath(String linuxPath) {
        PathUtil.linuxPath = linuxPath;
    }


    //通过application.properties注入
    @Value("${shop.relevant.path}")
    public void setShopPath(String shopPath) {
        PathUtil.shopPath = shopPath;
    }

    /**
     * 存放在当前系统的根路径！！！
     *
     * @return
     */
    public static String getImgBasePath() {
        //		获取当前系统名称
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().contains("win")) {
            basePath = winPath;
        } else {
            basePath = linuxPath;
        }
        //将分隔符转换
        basePath = basePath.replace("/", seperator);
        return basePath;
    }

    /**
     * 每个店铺应该都有自己的存放图片的文件夹
     * 是一个相对路径！
     */
    public static String getShopImagePath(Long shopId) {
        String imagePath = shopPath + shopId + "/";
        return imagePath.replace("/", seperator);
    }

}
