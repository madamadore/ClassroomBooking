package it.tecnosphera.booking.classroom.controller;

import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.repository.RepositoryInterface;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/user")
public class UserController {

	@Autowired
	RepositoryInterface<User> userRepository;

	@RequestMapping(value="/")
	public String listUsers(Model model) {
		List<User> users = userRepository.findAll();
		model.addAttribute("users", users);
		return "user/list";
	}
	
	@RequestMapping(value="/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User user, Model model) {
		userRepository.save(user);
		model.addAttribute("user", user);
        return "user/view";
    }
}
