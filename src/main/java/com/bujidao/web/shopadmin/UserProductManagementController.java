package com.bujidao.web.shopadmin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bujidao.dto.EchartSeries;
import com.bujidao.dto.EchartXAxis;
import com.bujidao.dto.UserProductMapExecution;
import com.bujidao.entity.Product;
import com.bujidao.entity.ProductSellDaily;
import com.bujidao.entity.Shop;
import com.bujidao.entity.UserProductMap;
import com.bujidao.service.ProductSellDailyService;
import com.bujidao.service.UserProductMapService;
import com.bujidao.util.HttpServletRequestUtil;


@Controller
@RequestMapping("/shopadmin")
public class UserProductManagementController {
	private static Logger logger=LoggerFactory.getLogger(UserProductManagementController.class);
	@Autowired
	private UserProductMapService userProductMapService;
	@Autowired
	private ProductSellDailyService productSellDailyService;

	@RequestMapping(value = "/listuserproductmapsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserProductMapsByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		if (pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null){
			UserProductMap userProductMapCondition=new UserProductMap();
			userProductMapCondition.setShop(currentShop);
			String productName=HttpServletRequestUtil.getString(request, "productName");
			if(productName!=null){
				Product product=new Product();
				product.setProductName(productName);
				userProductMapCondition.setProduct(product);
			}
			UserProductMapExecution ue=userProductMapService.getUserProductMapList(userProductMapCondition, pageIndex, pageSize);
			modelMap.put("success", true);
			modelMap.put("count", ue.getCount());
			modelMap.put("userProductMapList", ue.getUserProductMapList());
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
	
	
	@RequestMapping(value="/listproductselldailyinfobyshop",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductSellDailyInfoByShop(HttpServletRequest request){
		Map<String, Object> modelMap=new HashMap<String, Object>();
		Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
		if(currentShop!=null && currentShop.getShopId()!=null){
			ProductSellDaily productSellDailyCondition=new ProductSellDaily();
			productSellDailyCondition.setShop(currentShop);
			Calendar calendar=Calendar.getInstance();
			//获取昨天日期
			calendar.add(Calendar.DATE, -1);
			Date endTime=calendar.getTime();
			//获取7天前日期
			calendar.add(Calendar.DATE, -6);
			Date beginTime=calendar.getTime();
			//根据传入的查询条件获取该店铺的商品销售情况
			List<ProductSellDaily> productSellDailyList=
					productSellDailyService.getProductSellDailyList(productSellDailyCondition, beginTime, endTime);
			//指定日期格式
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			//商品名列表，保证唯一性
			HashSet<String> legendData=new HashSet<String>();
			//x轴数据
			LinkedHashSet<String> xData=new LinkedHashSet<String>();
			//定义series
			List<EchartSeries> seriesList=new ArrayList<EchartSeries>();
			//日销量列表
			List<Integer> totalList=new ArrayList<Integer>();
			//当前商品名，默认为空
			String currentProductName="";
//			logger.debug("长度为:"+productSellDailyList.size());
			for(int i=0;i<=productSellDailyList.size()-1;i++){
				ProductSellDaily productSellDaily=productSellDailyList.get(i);
				//自动去重
				legendData.add(productSellDaily.getProduct().getProductName());
				xData.add(sdf.format(productSellDaily.getCreateTime()));
				//名字不为空且与前一个名字不相同，那么处理相应的逻辑
				//则是遍历到了下一个商品的日销量信息，将前一轮遍历的信息放到seriesList
				//包括了商品名，商品统计的日期以及对应的销量
//				logger.debug("当前名字为+"+currentProductName+"---"+"product名字为+"+productSellDaily
//						.getProduct().getProductName());
				if(!currentProductName.isEmpty() && !currentProductName.equals(productSellDaily
						.getProduct().getProductName())){
					EchartSeries es=new EchartSeries();
					es.setName(currentProductName);
					//如果直接设置进入，那么就是把这个list的对象放入进去
					//这里我们需要的是这个list的副本，利用subList克隆一个list放到data中
					es.setData(totalList.subList(0, totalList.size()));
					seriesList.add(es);
					//重置totalList
					totalList=new ArrayList<Integer>();
					//更新productId
					currentProductName=productSellDaily.getProduct().getProductName();
					totalList.add(productSellDaily.getTotal());
				}else{
					//如果还是之前的productId继续添加值
					totalList.add(productSellDaily.getTotal());
					//如果是空值，更新
					currentProductName=productSellDaily.getProduct().getProductName();
				}
				//如果到了最后一个，那么就把最后一个也添加进去seriesList
				if(i==productSellDailyList.size()-1){
					EchartSeries es=new EchartSeries();
					es.setName(currentProductName);
					//如果直接设置进入，那么就是把这个list的对象放入进去
					//这里我们需要的是这个list的副本，利用subList克隆一个list放到data中
					es.setData(totalList.subList(0, totalList.size()));
					seriesList.add(es);
				}
			}
			modelMap.put("series",seriesList);
			modelMap.put("legendData", legendData);
			//拼接出xAxis
			List<EchartXAxis> xAxisList=new ArrayList<EchartXAxis>();
			EchartXAxis axis=new EchartXAxis();
			axis.setData(xData);
			xAxisList.add(axis);
			modelMap.put("xAxis",xAxisList);
			modelMap.put("success", true);
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
}
