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
	public static final String INPUT_MISSING = "missInput";
	public static final String OK = "ok";
	public static final String WRONG_INPUT = "wrongInput";

	@RequestMapping(value = "/prenotazioni", method = RequestMethod.GET)
	public @ResponseBody List<Prenotazione> elaboraPrenotazioni() {
		return prenotazioneRepository.findAll();
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
			return INPUT_MISSING;
		}

		org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User user = userRepository.findByEmail(u.getUsername());
		Prenotazione prenotazione = new Prenotazione();
		Date start = utilityMethods.creaData(startTime, startDate);
		Date end = utilityMethods.creaData(endTime, endDate);
		Aula a = aulaRepository.find(Long.parseLong(aula));
		prenotazione.setOwner(user);
		prenotazione.setClassRoom(a);
		prenotazione.setStart(start);
		prenotazione.setEnd(end);
		if (!"".equals(id)) {
			long preId = Long.parseLong(id);
			prenotazione.setId(preId);
		}
		if (utilityMethods.verificaPrenotazione(prenotazione)) {
			prenotazioneRepository.save(prenotazione);
			return OK;
		} else {
			return WRONG_INPUT;
		}
	}
}