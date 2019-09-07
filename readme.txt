技术：
从springmvc+spring+mybatis开始开发，之后升级为springboot1.5+mybatis版本
数据库采用mysql
日志框架采用logback
定时任务框架采用Quartz，用于统计前一天的消费记录
安全校验使用的是原生springmvc的拦截器
缓存使用reids，缓存首页的类别和轮播图等
前端页面基于SUI Mobile框架
利用原生的js+html，进行前后端分离开发
前端商品消费记录柱状图使用echarts框架

注意:
因为是针对手机端的web页面开发，所以在pc端上页面可能没有那么好看
积分兑换，顾客积分，奖品管理等模块并未完全进行开发


展示系统
http://139.224.134.99/o2o_test/frontend/index
店家管理系统
http://139.224.134.99/o2o_test/shopadmin/shoplist


已提供微信接口(需要关注测试公众号)
展示系统
https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf5147cbc3f0e7700&redirect_uri=http://139.224.134.99/o2o_test/wechatlogin/logincheck&role_type=2&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
店家管理系统
https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf5147cbc3f0e7700&redirect_uri=http://139.224.134.99/o2o_test/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=2#wechat_redirect


