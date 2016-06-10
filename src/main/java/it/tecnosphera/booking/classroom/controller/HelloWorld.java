package it.tecnosphera.booking.classroom.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class HelloWorld {
	
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String home(Model model) {
        return "home";
    }
       

}
