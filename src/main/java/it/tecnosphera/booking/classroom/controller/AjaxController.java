package it.tecnosphera.booking.classroom.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.tecnosphera.booking.classroom.business.UtilityMethods;
import it.tecnosphera.booking.classroom.model.Aula;
import it.tecnosphera.booking.classroom.model.Prenotazione;
import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.repository.PrenotazioneRepositoryInterface;
import it.tecnosphera.booking.classroom.repository.RepositoryInterface;
import it.tecnosphera.booking.classroom.repository.UserRepositoryInterface;

@Controller
@RequestMapping(value = "/ajax")
public class AjaxController {

	@Autowired
	PrenotazioneRepositoryInterface prenotazioneRepository;

	@Autowired
	RepositoryInterface<Aula> aulaRepository;

	@Autowired
	UserRepositoryInterface userRepository;

	@Autowired
	UtilityMethods utilityMethods;

	public static final String LOGIN_REQUIRED = "login";
	public static final String MISSING_INPUT = "missInput";
	public static final String OK = "ok";
	public static final String WRONG_INPUT = "wrongInput";
	public static final String MISSING_PERMISSION = "missingPermission";

	@RequestMapping(value = "/prenotazioni", method = RequestMethod.GET)
	public @ResponseBody List<Prenotazione> elaboraPrenotazioni() {
		return prenotazioneRepository.findAll();
	}

	@RequestMapping(value = "/getEvent", method = RequestMethod.POST)
	private @ResponseBody Prenotazione getEvent(@RequestParam("id") String id) {
		return prenotazioneRepository.find(Long.parseLong(id));
	}

	@RequestMapping(value = "/emailValidation", method = RequestMethod.GET)
	private @ResponseBody Boolean emailValidation(@RequestParam("email") String email) {
		if (email != null && !email.contains("@tecnosphera.it")) {
			email = email.concat("@tecnosphera.it");
		}
		return userRepository.findByEmail(email) == null;
	}

	@RequestMapping(value = "/hasPermissions", method = RequestMethod.POST)
	private @ResponseBody Boolean hasPermissions(@RequestBody User owner) {
		return utilityMethods.hasPermissions(owner);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody String editPrenotazione(Model m, @RequestParam("aula") String aula,
			@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam("id") String id) {
		if (!utilityMethods.isLogged()) {
			return LOGIN_REQUIRED;
		}

		// se mancano dei parametri
		if ("".equals(startTime) || "".equals(endTime) || "".equals(startDate) || "".equals(endDate)) {
			return MISSING_INPUT;
		}

		org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User user = userRepository.findByEmail(u.getUsername());
		Prenotazione prenotazione = new Prenotazione();
		Date start = utilityMethods.creaData(startTime, startDate);
		Date end = utilityMethods.creaData(endTime, endDate);
		Aula a = null;
		try {
			a = aulaRepository.find(Long.parseLong(aula));
		} catch (NumberFormatException e) {
			return MISSING_INPUT;
		}
		prenotazione.setTitle(a.getName());
		prenotazione.setOwner(user);
		prenotazione.setClassRoom(a);
		prenotazione.setStart(start);
		prenotazione.setEnd(end);
		if (!"".equals(id)) {
			long preId = Long.parseLong(id);
			prenotazione.setId(preId);
			User owner = prenotazioneRepository.find(preId).getOwner();
			prenotazione.setOwner(owner);
		}
		if (utilityMethods.verificaPrenotazione(prenotazione)) {
			prenotazioneRepository.save(prenotazione);
			return OK;
		} else {
			return WRONG_INPUT;
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody String deleteEvent(@RequestParam("id") String id) {
		if (!utilityMethods.isLogged()) {
			return LOGIN_REQUIRED;
		}
		long idPrenotazione = Long.parseLong(id);
		if (!utilityMethods.hasPermissions(prenotazioneRepository.find(idPrenotazione).getOwner())) {
			return MISSING_PERMISSION;
		}
		prenotazioneRepository.delete(idPrenotazione);
		return OK;
	}
}