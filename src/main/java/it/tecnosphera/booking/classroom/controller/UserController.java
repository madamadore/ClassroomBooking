package it.tecnosphera.booking.classroom.controller;

import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.repository.RepositoryInterface;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/user")
public class UserController {

	@Autowired
	RepositoryInterface<User> userRepository;

    @RequestMapping(value="/edit/{id}", method = RequestMethod.GET)
    public String editUser(@PathVariable long id, Model model) {
    		User user = userRepository.find(id);
        model.addAttribute("user", user);
        return "user/edit";
    }
  
    @RequestMapping(value="/view/{id}", method = RequestMethod.GET)
    public String viewUser(@PathVariable long id, Model model) {
   		User user = userRepository.find(id);
        model.addAttribute("user", user);
        return "user/view";
    }
    
    @RequestMapping(value="/new", method = RequestMethod.GET)
    public String newUser(Model model) {
    		model.addAttribute(new User());
        return "user/edit";
    }       
    
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String viewAllAule(Model model) {
    	List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user/list";
    }
    
    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User user) {
    		long id = userRepository.save(user);
        return "redirect:view/"+id;
    }

    @RequestMapping(value="/delete", method = RequestMethod.POST)
    public String deleteUser(@PathVariable long id) {
    		User user = userRepository.find(id);
    		userRepository.delete(user);
        return "redirect:list";
    }
}
