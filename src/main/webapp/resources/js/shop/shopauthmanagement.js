$(function() {
	var listUrl = '/o2o_test/shopadmin/listshopauthmapsbyshop?pageIndex=1&pageSize=100'
	var statusUrl =	'/o2o_test/shopadmin/modifyshopauthmap';
	getList();
	function getList() {
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var shopauthList = data.shopAuthMapList;
				var tempHtml = '';
				shopauthList.map(function(item, index) {
					var textOp = "删除";
					var contraryStatus = 1;
					if (item.enableStatus == 0) {
						textOp = "恢复";
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					tempHtml += '' + '<div class="row row-shopauth">'
								+ '<div class="col-33">'
								+ item.employee.name
								+ '</div>'
					if(item.titleFlag!=0){
						//如果不是店家
						tempHtml +='<div class="col-20">'+item.title+'</div>'
							+'<div class="col-40">'
							+ '<a href="#" class="edit" data-auth-id="'
							+ item.shopAuthId
							+ '" data-employee-id="'
							+ item.employee.userId
							+ '">编辑</a>'
							+ '<a href="#" class="status" data-auth-id="'
							+ item.shopAuthId
							+ '" data-status="'
							+ contraryStatus
							+ '">'
							+ textOp
							+ '</a>'
							+ '</div>'
							+ '</div>';
					}else{
						tempHtml+='<div class="col-20">'+item.title
									+'</div>'+'<div class="col-40">'
									+'<span>不可操作</span>'+'</div>';
					}
					tempHtml+='</div>';
				});
				$('.shopauth-wrap').html(tempHtml);
			}
		});
	}

	function changeItemStatus(id, enableStatus) {
		var shopAuth = {};
		shopAuth.shopAuthId = id;
		shopAuth.enableStatus = enableStatus;
		$.confirm('确定么?', function() {
			$.ajax({
				url : statusUrl,
				type : 'POST',
				data : {
					//将shopauth的json对象解析为字符串
					shopAuthMapStr : JSON.stringify(shopAuth),
					statusChange : true
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.toast('操作成功！');
						getList();
					} else {
						$.toast('操作失败！');
					}
				}
			});
		});
	}

	//给带有a标签的按钮绑定事件
	$('.shopauth-wrap')
			.on(
					'click',
					'a',
					function(e) {
						var target = $(e.currentTarget);
						if (target.hasClass('edit')) {
							window.location.href = '/o2o_test/shopadmin/shopauthedit?shopAuthId='
									+ e.currentTarget.dataset.authId;
						} else if (target.hasClass('status')) {
							changeItemStatus(e.currentTarget.dataset.authId,
									e.currentTarget.dataset.status);
						} 
					});

	$('#new').click(function() {
		window.location.href = '#';
	});
});