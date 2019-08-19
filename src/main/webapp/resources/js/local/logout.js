$(function(){
	var logoutUrl='/o2o_test/local/logout';
	$('#log-out').click(function(){
		//清空session
		$.ajax({
			url:logoutUrl,
			async:false,
			cache:false,
			type:"post",
			dataType:'json',
			success:function(data){
				if(data.success){
					$.toast('退出成功！');
					var usertype=$("#log-out").attr("usertype");
					window.location.href="/o2o_test/local/login?usertype="+usertype;
					return false;
				}
			},
			error:function(data,error){
				alert(error);
			}
		});
	});
});