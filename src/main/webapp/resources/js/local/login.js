$(function(){
	var loginUrl='/o2o_test/local/logincheck';
	var usertype=getQueryString('usertype');
	var loginCount=0;
	
	$('#submit').click(function(){
		var username = $('#username').val().trim();
		var password = $('#psw').val().trim();
		var verifyCodeActual = $('#j_captcha').val();
		var needVerify=false;
		if(loginCount>=3){
			if(!verifyCodeActual){
				$.toast("请输入验证码！");
				return ;
			}else{
				needVerify=true;
			}
		}
		
		$.ajax({
			url:loginUrl,
			async:false,
			cache:false,
			type:'POST',
			dataType:'json',
			data:{
				username:username,
				password:password,
				verifyCodeActual:verifyCodeActual,
				needVerify:needVerify
			},
			success:function(data){
				if(data.success){
					$.toast("登陆成功!");
					if(usertype==1){
						window.location.href='/o2o_test/frontend/index';
					}else{
						window.location.href='/o2o_test/shopadmin/shoplist';
					}
				}else{
					$.toast("登陆失败!"+data.errMsg);
					loginCount++;
					if(loginCount>=3){
						$('#verifyPart').show();
					}
					$('#captcha_img').click();
				}
			}
		});
	});
});