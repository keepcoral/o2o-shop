package com.bujidao.web.local;

import com.bujidao.entity.LocalAuth;
import com.bujidao.entity.PersonInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/local")
public class LocalController {
	@RequestMapping(value="/accountbind",method=RequestMethod.GET)
	public String accountBind(){
		return "local/accountbind";
	}
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login(HttpServletRequest request){
		PersonInfo personInfo= (PersonInfo) request.getSession().getAttribute("user");
		if(personInfo!=null){
			return "shopadmin/shoplist";
		}
		return "local/login";
	}
	
	@RequestMapping(value="changepwd",method=RequestMethod.GET)
	public String changepwd(){
		return "local/changepwd";
	}
}
