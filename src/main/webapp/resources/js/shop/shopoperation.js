$(function(){
	var shopId=getQueryString('shopId');
	var isEdit=shopId?true:false;//如果传了shopId就是更新，没传就是添加店铺
	var initUrl='/o2o_test/shopadmin/getshopinitinfo';
	var registerShopUrl='/o2o_test/shopadmin/registershop';
	var shopInfoUrl='/o2o_test/shopadmin/getshopbyid?shopId='+shopId;
	var editShopUrl='/o2o_test/shopadmin/modifyshop';
	//想要js文件被加载的时候就调用这个方法，去后台获取信息
	if(!isEdit){
		getShopInitInfo();
	}else{
		getShopInfo(shopId);
	}
	function getShopInfo(shopId){
		$.getJSON(shopInfoUrl,function(data){
			if(data.success){
				var shop=data.shop;
				$('#shop-name').val(shop.shopName);
				$('#shop-addr').val(shop.shopAddr);
				$('#shop-phone').val(shop.phone);
				$('#shop-desc').val(shop.shopDesc);
				var shopCategory='<option data-id="'
					+shop.shopCategory.shopCategoryId+'"selected>'
					+shop.shopCategory.shopCategoryName+'</option>'
				var tempAreaHtml='';
				data.areaList.map(function(item,index){
						//item为每一个元素，遍历每一个元素
						tempAreaHtml +='<option data-id="'
							+item.areaId+'">'+item.areaName+'</option>';
				});
				$('#shop-category').html(shopCategory);
				$('#shop-category').attr('disabled','disabled');
				$('#area').html(tempAreaHtml);
				$("#area option[data-id='"+shop.area.areaId+"']").attr("selected","selected");
			}	
		});
	}
	function getShopInitInfo(){
		//从后台获取店铺信息以及区域信息，给填充到前台
		$.getJSON(initUrl,function(data){
			if(data.success){
				var tempHtml='';
				var tempAreaHtml='';
				data.shopCategoryList.map(function(item,index){
					tempHtml+='<option data-id="'+item.shopCategoryId+'">'
						+item.shopCategoryName +'</option>';
				});
				data.areaList.map(function(item,index){
					//item为每一个元素，遍历每一个元素
					tempAreaHtml +='<option data-id="'
						+item.areaId+'">'+item.areaName+'</option>';
				});
//				将该值塞进该id的html标签中
				$('#shop-category').html(tempHtml);
				$('#area').html(tempAreaHtml);
			}
		});
	}
//		点击提交获取表单内容
		$('#submit').click(function(){
			var shop={};
			if(isEdit){
				shop.shopId=shopId;
			}
			shop.shopName=$('#shop-name').val();
			shop.shopAddr=$('#shop-addr').val();
			shop.phone=$('#shop-phone').val();
			shop.shopDesc=$('#shop-desc').val();
			//从店铺分类的列表里面选出来的
			shop.shopCategory={
					shopCategoryId:$('#shop-category').find('option').not(function(){
						return !this.selected;
					}).data('id')
			};
			//同理
			shop.area={
					areaId:$('#area').find('option').not(function(){
						return !this.selected;
					}).data('id')
			};
			var shopImg=$('#shop-img')[0].files[0];
			//接受表单内容
			var formData=new FormData();
			formData.append('shopImg',shopImg);
			formData.append('shopStr',JSON.stringify(shop));
			var verifyCodeActual=$('#j_captcha').val();
			if(!verifyCodeActual){
				$.toast('请输入验证码');
				return ;
			}
			formData.append('verifyCodeActual',verifyCodeActual);
			$.ajax({
				url:(isEdit?editShopUrl:registerShopUrl),
				type:'POST',
				data:formData,
				contentType:false,
				processData:false,
				cache:false,
				success:function(data){
					if(data.success){
						$.toast('提交成功！');
						window.location.href='/o2o_test/shopadmin/shoplist';
					}else{
						$.toast('提交失败！'+data.errMsg);
					}
					$('#captcha_img').click();
				}
			});
		});
});