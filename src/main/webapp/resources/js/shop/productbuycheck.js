$(function(){
	var productName='';
	getProductSellDailyList();
	getList();
	function getList(){
		var listUrl='/o2o_test/shopadmin/listuserproductmapsbyshop?pageIndex=1&pageSize=100&pageName='
				+productName;
		$.getJSON(listUrl,function(data){
				if(data.success){
					var userProductMapList=data.userProductMapList;
					var tempHtml='';
					userProductMapList.map(function(item,index){
						tempHtml+=''+'<div class="row row-productbuycheck">'
								+'<div class="col-20">'+item.product.productName
								+'</div>'
								+'<div class="col-40 prodcutbuycheck-time">'
								+new Date(item.createTime).Format("yyyy-MM-dd")
								+'</div>'+'<div class="col-25">'
								+item.user.name+'</div>'
								+'<div class="col-15">' + item.point+'</div>'
								+'</div>';
					});
				$('.productbuycheck-wrap').html(tempHtml);
			}
		});
	}
	
	$('#search').on('change',function(e){
		//获取输入在搜索框里面的商铺名
		productName=e.target.value;
		//清空购买记录列表
		$('.productbuycheck-wrap').empty();
		getList();
	});
	/**
	 * 测试使用

	//echarts逻辑部分
	//获取html中id为chart的元素
	var myChart = echarts.init(document.getElementById('chart'));
	
	var option={
		//提示框，鼠标悬浮的时候的信息提示
		tooltip:{
			trigger:'axis',
			axisPointer:{//坐标轴指示器，坐标轴触发有效
				type:'shadow'//默认为直线，可选为'line'|'shadow'
			}
		},
		//图例 每个图标最多仅有一个图例
		legend:{
			//图例数组内容，数组项通常为string，每一项代表一个系列的name
			data:['奶茶','拿铁','红茶']
		},
		//直角坐标系内绘图网格
		grid:{
			left:'3%',
			right:'4%',
			botton:'3%',
			containLabel:true
		},
		//直角坐标系中横轴数组，数组中每一项代表一条横轴坐标轴
		xAxis:[{
			//类目型：需要指定类目列表，坐标轴内有且仅有这些指定的类目坐标
			type:'category',
			data:['周一','周二','周三','周四','周五','周六','周日']
		}],
		yAxis:[{
			type:'value'
		}],
		//驱动图表中生成的数据内容数组，数据中每一项为一个系列的选项以及数据
		series:[{
			name:'奶茶',
			type:'bar',
			data:[120,123,5,200,89,23,79]

		},{
			name:'拿铁',
			type:'bar',
			data:[150,113,8,221,89,98,109]

		},{
			name:'红茶',
			type:'bar',
			data:[122,123,55,123,58,140,90]
		}],
	};
	myChart.setOption(option);

	*/
	
	
	function getProductSellDailyList(){
		//获取该店铺7天的销量url
		var listProductSellDailyUrl='/o2o_test/shopadmin/listproductselldailyinfobyshop'
		$.getJSON(listProductSellDailyUrl,function(data){
			if(data.success){
				var myChart = echarts.init(document.getElementById('chart'));
				//生成静态的Echart信息的部分
				var option=generateStaticEchartPart();
				//遍历销量统计列表动态设定echarts
				option.legend.data=data.legendData;
				option.xAxis=data.xAxis;
				option.series=data.series;
				myChart.setOption(option);
			}
			
		});
	}
	//获取echarts静态信息部分
	function generateStaticEchartPart(){
		//echarts逻辑部分
		var option={
			//提示框，鼠标悬浮的时候的信息提示
			tooltip:{
				trigger:'axis',
				axisPointer:{//坐标轴指示器，坐标轴触发有效
					type:'shadow'//默认为直线，可选为'line'|'shadow'
				}
			},
			//图例 每个图标最多仅有一个图例
			legend:{},
			//直角坐标系内绘图网格
			grid:{
				left:'3%',
				right:'4%',
				botton:'3%',
				containLabel:true
			},
			//直角坐标系中横轴数组，数组中每一项代表一条横轴坐标轴
			xAxis:[{
				type:'category',
				data:['周一','周二','周三','周四','周五','周六','周日']
			}],
			yAxis:[{
				type:'value'
			}],
			//驱动图表中生成的数据内容数组，数据中每一项为一个系列的选项以及数据

		};
		return option;
		 
	}
	
});