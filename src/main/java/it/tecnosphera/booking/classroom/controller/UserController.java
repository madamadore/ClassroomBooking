package it.tecnosphera.booking.classroom.controller;

import it.tecnosphera.booking.classroom.model.Aula;
import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.repository.UserRepositoryInterface;

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
	UserRepositoryInterface userRepository;


    @RequestMapping(value="/edit/{id}", method = RequestMethod.GET)
    public String editUser(@PathVariable long id, Model model) {
    		User user = userRepository.getUser(id);
        model.addAttribute("user", user);
        return "user/edit";
    }
  
    @RequestMapping(value="/view/{id}", method = RequestMethod.GET)
    public String viewUser(@PathVariable long id, Model model) {
    	User user = userRepository.getUser(id);
        model.addAttribute("user", user);
        return "user/view";
    }
    
    @RequestMapping(value="/new")
    public String newUser(Model model) {
    		model.addAttribute(new User());
        return "user/edit";
    }       
    
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String viewAllAule(Model model) {
    	List<User> users = userRepository.getUsers();
        model.addAttribute("users", users);
        return "user/list";
    }
    
    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User user, Model model) {
    	userRepository.save(user);
		model.addAttribute("user", user);
        return "user/view";
    }

    @RequestMapping(value="/delete", method = RequestMethod.POST)
    public String deleteAula(@PathVariable long id, Model model) {
    	User user = userRepository.getUser(id);
    	userRepository.delete(user);
    	List<User> users = userRepository.getUsers();
        model.addAttribute("users", users);
        return "user/list";
    }
}
