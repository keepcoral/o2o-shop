package com.bujidao.config.web;

import javax.servlet.ServletException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.bujidao.interceptor.shopadmin.ShopLoginInterceptor;
import com.bujidao.interceptor.shopadmin.ShopPermissionInterceptor;
import com.bujidao.util.PathUtil;
import com.google.code.kaptcha.servlet.KaptchaServlet;

/**
 * 开启mvc自动注入spring容器，WebMvcConfigurerAdapter:配置视图解析器
 * 当一个类实现这个接口ApplicationContextAware，这个类就可以方便获取ApplicaitonContext中的所有bean
 * @author wyq
 *
 */
@Configuration
//等价于<mvc:annotation-driven/>
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware{
	//spring容器
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

	/**
	 * 静态资源配置
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resource/");
		//win目录
//		registry.addResourceHandler("/Image/**").addResourceLocations("file:D:/IDM缓存/Image/");
		//linux目录
//		registry.addResourceHandler("/Image/**").addResourceLocations("file:/root/Image/");
		registry.addResourceHandler("/Image/**").addResourceLocations("file:"+PathUtil.getImgBasePath()+"/Image/");
	}

	/**
	 * 定义默认请求处理器
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	@Bean(name="viewResolver")
	public ViewResolver createViewResolver(){
		InternalResourceViewResolver viewResolver=
				new InternalResourceViewResolver();
		//设置spring容器
		viewResolver.setApplicationContext(applicationContext);
		//取消缓存
		viewResolver.setCache(false);
		//设置前缀
		viewResolver.setPrefix("/WEB-INF/html/");
		//设置后缀
		viewResolver.setSuffix(".html");
		return viewResolver;
	}
	
	@Bean(name="multipartResolver")
	public CommonsMultipartResolver createMultipartResolver(){
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding("utf-8");
		multipartResolver.setMaxUploadSize(20971520);
		multipartResolver.setMaxUploadSize(20971520);
		return multipartResolver;
	}
	
	@Value("${kaptcha.border}")
	private String border;
	
	@Value("${kaptcha.textproducer.font.color}")
	private String fcolor;
	
	@Value("${kaptcha.image.width}")
	private String width;
	
	@Value("${kaptcha.textproducer.char.string}")
	private String cstring;
	
	@Value("${kaptcha.image.height}")
	private String height;
	
	@Value("${kaptcha.textproducer.font.size}")
	private String fsize;
	
	@Value("${kaptcha.noise.color}")
	private String ncolor;
	
	@Value("${kaptcha.textproducer.char.length}")
	private String clength;
	
	@Value("${kaptcha.textproducer.font.names}")
	private String fanmes;
	
	/**
	 * 由于web.xml不生效了，需要在这里配置Kapthcha验证码servlet
	 * ServletRegistrationBean是往spring容器里面注册servlet
	 */
	@Bean
	public ServletRegistrationBean registBean() throws ServletException{
		//注册哪个servlet并定义路由
		ServletRegistrationBean bean=new ServletRegistrationBean(new KaptchaServlet(),"/Kaptcha");
		bean.addInitParameter("kaptcha.border", border);
		bean.addInitParameter("kaptcha.textproducer.font.color", fcolor);
		bean.addInitParameter("kaptcha.image.width", width);
		bean.addInitParameter("kaptcha.textproducer.char.string", cstring);
		bean.addInitParameter("kaptcha.image.height", height);
		bean.addInitParameter("kaptcha.textproducer.font.size", fsize);
		bean.addInitParameter("kaptcha.noise.color", ncolor);
		bean.addInitParameter("kaptcha.textproducer.char.length", clength);
		bean.addInitParameter("kaptcha.textproducer.font.names", fanmes);
		return bean;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		String interceptPath="/shopadmin/**";
		//注册拦截器
		InterceptorRegistration loginIR=
				registry.addInterceptor(new ShopLoginInterceptor());
		//配置拦截路径
		loginIR.addPathPatterns(interceptPath);
		
		//扫描二维码的时候是不需要登陆路径的
		loginIR.excludePathPatterns("/shopadmin/addshopauthmap");
		//还可以注册别的拦截器
		InterceptorRegistration permissionIR=
				registry.addInterceptor(new ShopPermissionInterceptor());
		permissionIR.addPathPatterns(interceptPath);
		
		//配置不拦截路径
		//shoplist page
		permissionIR.excludePathPatterns("/shopadmin/shoplist");
		permissionIR.excludePathPatterns("/shopadmin/getshoplist");
		//shopregister page
		permissionIR.excludePathPatterns("/shopadmin/getshopinitinfo");
		permissionIR.excludePathPatterns("/shopadmin/registershop");
		permissionIR.excludePathPatterns("/shopadmin/shopoperation");
		//shopmanage page
		permissionIR.excludePathPatterns("/shopadmin/shopmanagement");
		permissionIR.excludePathPatterns("/shopadmin/getshopmanagementinfo");
		permissionIR.excludePathPatterns("/shopadmin/addshopauthmap");
	}
	
	
}
