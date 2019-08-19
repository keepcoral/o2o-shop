$(function() {
	var changePwdUrl = '/o2o_test/local/changelocalpwd';
	var usertype = getQueryString('usertype');

	$('#submit').click(function() {
		var username = $('#username').val();
		var password = $('#password').val();
		var newPassword = $('#newPassword').val();
		var confirmPassword = $('#confirmPassword').val();
		var verifyCodeActual = $('#j_captcha').val();
		if (newPassword != confirmPassword) {
			$.toast('两次输入的密码不一致!');
			return;
		}
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		var formData = new FormData();
		formData.append('username', username);
		formData.append('password', password);
		formData.append('newPassword', newPassword);
		formData.append('confirmPassword', confirmPassword);
		formData.append('verifyCodeActual', verifyCodeActual);
		$.ajax({
			url : changePwdUrl,
			cache : false,
			contentType : false,
			processData : false,
			type : 'POST',
			data : formData,
			success : function(data) {
				if (data.success) {
					$.toast('密码修改成功!');
					window.location.href = '/o2o_test/shopadmin/shoplist';
				} else {
					$.toast(data.errMsg);
					$('#captcha_img').click();
				}
			}
		});
	});
});