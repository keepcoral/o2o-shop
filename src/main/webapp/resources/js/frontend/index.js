$(function(){
	var url='/o2o_test/frontend/listmainpageinfo';
	
	$.getJSON(url,function(data){
		if(data.success){
			var headLineList=data.headLineList;
			var swiperHtml='';
			//遍历从后台传过来的头条列表，拼接出轮播图组
			headLineList.map(function(item,index){
				 swiperHtml += ''
                     + '<div class="swiper-slide img-wrap">'
                     + '<a href="#"><img class="banner-img" src="'+ getContextPath()+item.lineImg +'" alt="'+ item.lineName +'"></a>'
                     + '</div>';
			});
			//将轮播图组赋值给前端html
			$('.swiper-wrapper').html(swiperHtml);
			$(".swiper-container").swiper({
				autoplay : 3000,
				autoplayDisableOnInteraction:false
			});

		    var shopCategoryList = data.shopCategoryList;
            var categoryHtml = '';
            shopCategoryList.map(function (item, index) {
                categoryHtml += ''
                             +  '<div class="col-50 shop-classify" data-category='+ item.shopCategoryId +'>'
                             +      '<div class="word">'
                             +          '<p class="shop-title">'+ item.shopCategoryName +'</p>'
                             +          '<p class="shop-desc">'+ item.shopCategoryDesc +'</p>'
                             +      '</div>'
                             +      '<div class="shop-classify-img-warp">'
                             +          '<img class="shop-img" src="'+ getContextPath()+item.shopCategoryImg +'">'
                             +      '</div>'
                             +  '</div>';
            });
            $('.row').html(categoryHtml);               
		}
	});
	
	//点击'我的'显示右侧栏
	$('#me').click(function(){
//		$.ajax({
//			url:'/o2o_test/local/islogined',
//			type:"GET",
//			dataType:"json",
//			success:function(data){
//				if(data.success){
//					$.openPanel('#panel-right-demo');
//				}else{
//					$.openPanel('#panel-login');
//				}
//			}
//		});
		$.openPanel('#panel-right-demo');
	});
	$('.row').on('click','.shop-classify',function(e){
		var shopCategoryId = e.currentTarget.dataset.category;
		var newUrl='/o2o_test/frontend/shoplist?parentId='+shopCategoryId;
		window.location.href = newUrl;
	});
});