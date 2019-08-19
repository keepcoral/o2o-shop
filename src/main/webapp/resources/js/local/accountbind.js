$(function() {
	var bindUrl = '/o2o_test/local/bindlocalauth';
	//usertype=1则为前端展示系统，其余为店家管理系统
	var usertype=getQueryString('usertype');
	$('#submit').click(function() {
		var username = $('#username').val();
		var password = $('#psw').val();
		var verifyCodeActual = $('#j_captcha').val();
		var needVerify = false;
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		$.ajax({
			url : bindUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				username : username,
				password : password,
				verifyCodeActual : verifyCodeActual
			},
			success : function(data) {
				if (data.success) {
					$.toast('绑定成功！');
//					window.location.href = 'javascript :history.back(-1);';
					if(usertype==1){
						window.location.href='/o2o_test/frontend/index';
					}else{
						window.location.href='/o2o_test/shopadmin/shoplist';
					}
				} else {
					$.toast('绑定失败！'+data.errMsg);
					$('#captcha_img').click();
				}
			}
		});
	});
});