package com.bujidao.dto;

import java.io.InputStream;

/**
 * 存放图片名字和InputStream
 * 这里面放的是一个流对象InputStream!!!!
 * @author wyq
 *
 */
public class ImageHolder {
	private String imageName;
	private InputStream image;
	public ImageHolder(String imageName, InputStream image) {
		super();
		this.imageName = imageName;
		this.image = image;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public InputStream getImage() {
		return image;
	}
	public void setImage(InputStream image) {
		this.image = image;
	}
	
	
}
