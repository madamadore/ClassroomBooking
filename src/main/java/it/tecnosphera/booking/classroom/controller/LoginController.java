package it.tecnosphera.booking.classroom.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.tecnosphera.booking.classroom.model.Aula;
import it.tecnosphera.booking.classroom.model.Prenotazione;
import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.model.UserRole;
import it.tecnosphera.booking.classroom.repository.RepositoryInterface;
import it.tecnosphera.booking.classroom.repository.UserRepositoryInterface;

@Controller
public class LoginController {

	@Autowired
	UserRepositoryInterface userRepository;

	@Autowired
	RepositoryInterface<Aula> aulaRepository;

	@Autowired
	RepositoryInterface<Prenotazione> prenotazioniRepository;

	@RequestMapping(value = "/")
	public String home(Model m) {
		Date now = Calendar.getInstance().getTime();
		m.addAttribute("dataCorrente", now);
		m.addAttribute("aule", aulaRepository.findAll());
		return "prenotazioni/list";
	}

	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String newUser(Model model) {
		model.addAttribute(new User());
		return "user/register";
	}

	@RequestMapping(value = "/saveUserRegister", method = RequestMethod.POST)
	public String saveUserRegister(@ModelAttribute("user") User user) {
		String email = user.getEmail();
		if (email != null && !email.contains("@tecnosphera.it")) {
			email = email.concat("@tecnosphera.it");
		}

		user.setEmail(email);
		user.setEnabled(true);
		user.setPassword(userRepository.MD5Hashing(user.getPassword()));

		UserRole userRole = new UserRole();
		userRole.setRole("ROLE_USER");
		userRole.setDescrizione("Utente Guest");
		userRole.setUser(user);
		user.getUserRole().add(userRole);

		userRepository.save(user);
		return "redirect:login";
	}

	@RequestMapping(value = "/403", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String provideUploadInfo() {
		return "Access Denied";
	}

	// Login form with error
	@RequestMapping("/login-error")
	public String loginError(Model model) {
		Map<String, Object> map = model.asMap();
		for (String index : map.keySet()) {
			System.out.println(index + " : " + map.get(index).toString());
		}
		model.addAttribute("loginError", true);
		return "redirect:login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:login";
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