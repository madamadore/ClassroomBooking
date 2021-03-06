package it.tecnosphera.booking.classroom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.tecnosphera.booking.classroom.business.UtilityMethods;
import it.tecnosphera.booking.classroom.model.Role;
import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.repository.RoleRepositoryInterface;
import it.tecnosphera.booking.classroom.repository.UserRepositoryInterface;

@Controller
@RequestMapping(value = "/admin/user")
public class UserController {

	@Autowired
	UserRepositoryInterface userRepository;
	
	@Autowired
	RoleRepositoryInterface roleRepository;
	
	@Autowired
	UtilityMethods utilityMethods;

	@RequestMapping(value = "/ajax/emailValidation", method = RequestMethod.GET)
	private @ResponseBody Boolean emailValidation(@RequestParam("email") String email) {
		if (email != null && !email.contains("@tecnosphera.it")) {
			email = email.concat("@tecnosphera.it");
		}
		return userRepository.findByEmail(email) == null;
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editUser(@PathVariable long id, Model model) {
		User user = userRepository.find(id);

		String email = "@tecnosphera.it";
		user.setEmail(user.getEmail().substring(0, user.getEmail().length() - email.length()));

		List<Role> listaRuoli = roleRepository.findAll();
		model.addAttribute("user", user);
		model.addAttribute("listaRuoli", listaRuoli);
		return "user/edit";
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String viewUser(@PathVariable long id, Model model) {
		User user = userRepository.find(id);
		model.addAttribute("user", user);
		return "user/view";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newUser(Model model) {
		List<Role> listaRuoli = roleRepository.findAll();
		model.addAttribute(new User());
		model.addAttribute("listaRuoli", listaRuoli);
		return "user/edit";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String viewAllAule(Model model) {
		List<User> users = userRepository.findAll();
		model.addAttribute("users", users);
		return "user/list";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveUser(@ModelAttribute("user") User user) {
		String email = user.getEmail();
		if (email != null && !email.contains("@tecnosphera.it")) {
			email = email.concat("@tecnosphera.it");
		}

		user.setEmail(email);
		user.setPassword(userRepository.MD5Hashing(user.getPassword()));
		
		for(Role r:user.getRoles()){
			Role ruoloCompleto=roleRepository.find(r.getName()).get(0);
			r.setId(ruoloCompleto.getId());
			r.setDescription(r.getDescription());
		}
		
		long id = userRepository.save(user);
		return "redirect:view/" + id;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public String deleteUser(@PathVariable long id) {
		userRepository.delete(id);
		return "redirect:/admin/user/";
	}
	
	@RequestMapping(value = "/ajax/hasRole", method = RequestMethod.POST)
	private @ResponseBody Boolean hasRole(@RequestParam String role) {
		return utilityMethods.hasRole(role);
	}
	
}
