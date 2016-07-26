package it.tecnosphera.booking.classroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.tecnosphera.booking.classroom.business.UtilityMethods;
import it.tecnosphera.booking.classroom.model.Aula;
import it.tecnosphera.booking.classroom.repository.PrenotazioneRepositoryInterface;
import it.tecnosphera.booking.classroom.repository.RepositoryInterface;
import it.tecnosphera.booking.classroom.repository.UserRepositoryInterface;

@Controller
@RequestMapping(value = "/prenotazioni")
public class PrenotazioniController {

	@Autowired
	PrenotazioneRepositoryInterface prenotazioneRepository;

	@Autowired
	RepositoryInterface<Aula> aulaRepository;

	@Autowired
	UserRepositoryInterface userRepository;

	@Autowired
	UtilityMethods utilityMethods;

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deletePrenotazione(@RequestParam("id") String id) {
		if (!utilityMethods.isLogged()) {
			return "login";
		}
		long idPrenotazione = Long.parseLong(id);
		if (!utilityMethods.hasPermissions(prenotazioneRepository.find(idPrenotazione).getOwner())) {
			return "redirect:/prenotazioni/error";
		}
		prenotazioneRepository.delete(idPrenotazione);
		return "redirect:/";
	}

	@RequestMapping(value = "/view")
	public String visualizzaPrenotazione() {
		return "prenotazioni/view";
	}

	@RequestMapping(value = "/save")
	public String save() {
		return "prenotazioni/save";
	}

	@RequestMapping(value = "/error")
	public String error() {
		return "prenotazioni/error";
	}
}
