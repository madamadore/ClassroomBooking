package it.tecnosphera.booking.classroom.controller;

import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.repository.UserRepositoryInterface;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/user")
public class UserController {

	@Autowired
	UserRepositoryInterface userRepository;

	@RequestMapping(value="/")
	public String listUsers(Model model) {
		List<User> users = userRepository.getUsers();
		model.addAttribute("users", users);
		return "user/list";
	}
}
