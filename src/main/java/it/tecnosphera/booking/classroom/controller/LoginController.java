package it.tecnosphera.booking.classroom.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	@RequestMapping(value = "/")
    public String home() {
        return "prenotazioni/list";
    }
	
    @RequestMapping(value ="/login")
    public String login() {
        return "login";
    }
    
    @RequestMapping(value="/403", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody String provideUploadInfo() {
        return "Access Denied";
    }
 
    // Login form with error
    @RequestMapping("/login-error")
    public String loginError(Model model) {
    	Map<String, Object> map = model.asMap();
    	for (String index : map.keySet()) {
    		System.out.println( index + " : " + map.get(index).toString() );
    	}
        model.addAttribute("loginError", true);
        return "login";
    }
    
	 @RequestMapping(value="/logout", method = RequestMethod.GET)  
	 public String logout(Model model) {  
		 return "login";  	
	 }  
	
	 @RequestMapping("/error")
	 public String error(HttpServletRequest request, Model model) {
		 model.addAttribute("errorCode", request.getAttribute("javax.servlet.error.status_code"));
	     Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
	     String errorMessage = null;
	     if (throwable != null) {
	    	 errorMessage = throwable.getMessage();
	    	 model.addAttribute("errorMessage", errorMessage.toString());
	     }
	     return "error";
	 }
}