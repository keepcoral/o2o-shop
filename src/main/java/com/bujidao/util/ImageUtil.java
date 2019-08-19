package com.bujidao.util;

import java.io.File;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bujidao.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	private static Logger logger=LoggerFactory.getLogger(ImageUtil.class);
//	private static String basePath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static String basePath=PathUtil.getImgBasePath();
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdddHHmmssSSS");
	private static Random random=new Random();
	/**
	 * 创建缩略图
	 * CommonsMultipartFile为spring自带的文件处理对象,targetAddr为图片的相对路径
	 * 真实绝对路径等于：根路径+相对路径+文件名+后缀名
	 */
	public static String generateThumbnail(ImageHolder thumbnail,String targetAddr){
		String realFileName=getRandomFileName();//随机文件名
		String extension=getFileExtension(thumbnail.getImageName());//后缀名
		makeDirPath(targetAddr);
		String relativeAddr=targetAddr+realFileName+extension;
		//PathUtil.getImgBasePath()返回的是存放图片的路径！与上面的basePath是不同的
		File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
		System.out.println("要创建文件的路径："+dest.getAbsolutePath());
		try{
			//根据当前线程获取项目下的resources的路径
			//将路径中的中文重新用UTF-8转码，因为我这里的目录带有中文
			basePath=URLDecoder.decode(basePath, "UTF-8");
			//传入需要处理的图片
			Thumbnails.of(thumbnail.getImage())
			//设置处理后的图片大小
						.size(200, 200)
			//设置水印在图片的位置，水印的路径，定义水印的透明度
						.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.8f)
			//压缩比
						.outputQuality(0.5f)
			//新的文件存放在地点
						.toFile(dest);
		}catch (Exception e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		//数据库里面存的就是相对路径
		return relativeAddr;
	}
	
	/**
	 * 创建详情图片，和generateThumbnail一摸一样，只是图片大小不同而已
	 */
	public static String generateNormalImg(ImageHolder thumbnail,String targetAddr){
		String realFileName=getRandomFileName();//随机文件名
		String extension=getFileExtension(thumbnail.getImageName());//后缀名
		makeDirPath(targetAddr);
		String relativeAddr=targetAddr+realFileName+extension;
		//PathUtil.getImgBasePath()返回的是存放图片的路径！与上面的basePath是不同的
		File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
		try{
			//根据当前线程获取项目下的resources的路径
			//将路径中的中文重新用UTF-8转码，因为我这里的目录带有中文
			basePath=URLDecoder.decode(basePath, "UTF-8");
			//传入需要处理的图片
			Thumbnails.of(thumbnail.getImage())
			//设置处理后的图片大小
						.size(600, 600)
			//设置水印在图片的位置，水印的路径，定义水印的透明度
						.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.5f)
			//压缩比
						.outputQuality(0.5f)
			//新的文件存放在地点
						.toFile(dest);
		}catch (Exception e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		//数据库里面存的就是相对路径
		return relativeAddr;
	}
	/**
	 *	创建涉及目录的文件夹 
	 */
	private static void makeDirPath(String targetAddr) {
		String realFilePath=PathUtil.getImgBasePath()+targetAddr;
		File dirPath=new File(realFilePath);
		if(!dirPath.exists()) dirPath.mkdirs();
	}
	
	/**
	 * 获取文件流的拓展名
	 */
	public static String getFileExtension(String fileName) {
		//获取文件流的文件名
		String originalFilename=fileName;
		return originalFilename.substring(originalFilename.lastIndexOf("."));
	}
	/**
	 * 获取随机文件名
	 */
	public static String getRandomFileName() {
		int tmp=random.nextInt(89999)+10000;
		String fileName=sdf.format(new Date())+tmp;
		return fileName;
	}
	/**
	 * 如果是目录就删除下面的所有文件
	 * 如果是文件直接删除文件
	 */
	public static void deleteFileOrPath(String storePath){
		File file=new File(PathUtil.getImgBasePath()+storePath);
		if(file.exists()){
			if(file.isDirectory()){
				File[] files=file.listFiles();
				for(int i=0;i<=files.length-1;i++){
					files[i].delete();
				}
			}
			file.delete();
		}
	}
}
