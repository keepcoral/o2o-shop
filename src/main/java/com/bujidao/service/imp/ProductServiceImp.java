package com.bujidao.service.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bujidao.dao.ProductDao;
import com.bujidao.dao.ProductImgDao;
import com.bujidao.dto.ImageHolder;
import com.bujidao.dto.ProductExecution;
import com.bujidao.entity.Product;
import com.bujidao.entity.ProductImg;
import com.bujidao.enums.ProductStateEnum;
import com.bujidao.exception.ProductOperationException;
import com.bujidao.service.ProductService;
import com.bujidao.util.ImageUtil;
import com.bujidao.util.PageCalculator;
import com.bujidao.util.PathUtil;

@Service
public class ProductServiceImp implements ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	/**
	 * 1.处理缩略图，获取缩略图相对路径并赋值给product
	 * 2.往tb_product写入商品信息，获取productId
	 * 3.根据productId批量处理商品详情图片
	 * 4.批量插入详情图片
	 */
	@Override
	@Transactional
	public ProductExecution addProduct(Product product
			, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ProductOperationException {
		if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null){
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			product.setEnableStatus(1);
			//如果缩略图不为空则添加
			if(thumbnail!=null){
				addThumnail(product, thumbnail);
			}
			try{
				int effectedNum = productDao.insertProduct(product);
				if(effectedNum<=0){
					throw new ProductOperationException("添加商品失败！");
				}
			}catch (Exception e) {
				throw new ProductOperationException("创建商品失败！"+e.getMessage());
			}
			//商品详情图不为空则添加
			if(productImgHolderList!=null&&productImgHolderList.size()>0){
				addProductListImg(product, productImgHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS,product);
		}
		else{
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	
	/**
	 * 给product添加缩略图
	 * @param product
	 * @param thumbnail
	 */
	private void addThumnail(Product product,ImageHolder thumbnail){
		String dest=PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumnailAddr=ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumnailAddr);
	}
	
	/**
	 * 添加详情图片
	 * @param product
	 * @param productImgHolderList
	 */
	private void addProductListImg(Product product,List<ImageHolder> productImgHolderList){
		//获取图片存储的相对路径，这里直接存放在店铺下面的文件夹
		String dest=PathUtil.getShopImagePath(product.getShop().getShopId());
		//将前端传过来的文件流保存到本地并且添加到productImgList中
		List<ProductImg> productImgList=new ArrayList<ProductImg>();
		for(ImageHolder imageHolder:productImgHolderList){
			//和之前处理缩略图的方法一摸一样只是换了图片大小而已
			String imgAddr=ImageUtil.generateNormalImg(imageHolder, dest);
			
			ProductImg productImg=new ProductImg();
			productImg.setCreateTime(new Date());
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImgList.add(productImg);
		}
		//如果确实有图片要添加，那么就批量添加ProductImg
		if(productImgList.size()>0){
			try{
				int effectedNum=productImgDao.batchInsertProductImg(productImgList);
				if(effectedNum<=0){
					throw new ProductOperationException("创建详情图片失败！");
				}
			}catch (Exception e) {
				throw new ProductOperationException("创建详情图片失败:"+e.getMessage()); 
			}
		}
	}

	/**
	 * 删除详情图片
	 * @param productId
	 */
	private void deleteProductImgList(long productId){
		List<ProductImg> productImgList=productImgDao.queryProductImgList(productId);
		//在本地删除原来的详情图片
		for(ProductImg productImg:productImgList){
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		//在tb_product_img数据库删除productImg信息
		productImgDao.deleteProductImgByProductId(productId);
	}
	
	/**
	 * 通过id查询product
	 */
	@Override
	public Product getProductById(long productId) {
		return productDao.queryByProductId(productId);
	}

	
	
	/**
	 * 1.若缩略图有值，则处理缩略图，先删缩略图在添加新的缩略图
	 * 2.若商品详情图片有值，对详情图片进行同样的操作
	 * 3.将tb_product_img下面原先的所有详情图都删除
	 * 4.更新tb_product和tb_product_img的信息
	 */
	@Override
	@Transactional
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgHolderList) throws ProductOperationException {
		System.out.println("调用了modify方法");
		if(product!=null && product.getShop()!=null && product.getShop().getShopId()!=null){
			product.setLastEditTime(new Date());
			//有缩略图
			if(thumbnail!=null){
				System.out.println("service层有缩略图");
				//先获取原有的信息，因为原有信息里面有着缩略图的地址
				Product tempProduct=
						productDao.queryByProductId(product.getProductId());
				//先删缩略图在添加新的缩略图
				if(tempProduct.getImgAddr()!=null){
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				addThumnail(product, thumbnail);
			}
			if(productImgHolderList!=null && productImgHolderList.size()>0){
				deleteProductImgList(product.getProductId());
				addProductListImg(product, productImgHolderList);
			}
			try{
				int effectedNum=productDao.updateProduct(product);
				if(effectedNum<=0){
					throw new ProductOperationException("商品更新失败！");
				}
			}catch (Exception e) {
				throw new ProductOperationException("商品更新失败！"+e.getMessage());
			}
			return new ProductExecution(ProductStateEnum.SUCCESS);
		}else{
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	/**
	 * 分页查询
	 */
	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		int rowIndex=PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList=productDao.queryProductList
				(productCondition, rowIndex, pageSize);
		int count=productDao.queryProductCount(productCondition);
		ProductExecution pe=new ProductExecution(ProductStateEnum.SUCCESS,productList,count);
		return pe;
	}

	
	


}
