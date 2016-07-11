package it.tecnosphera.booking.classroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import it.tecnosphera.booking.classroom.model.Prenotazione;
import it.tecnosphera.booking.classroom.repository.RepositoryInterface;

@Controller
@RequestMapping(value = "/prenotazione")
public class PrenotazioniController {

	@Autowired
	RepositoryInterface<Prenotazione> prenotazioneRepository;

	@RequestMapping(value = "/new")
	public String nuovaPrenotazione() {
		return "prenotazione/new";
	}
	
	@RequestMapping(value = "/view")
	public String visualizzaPrenotazione() {
		return "prenotazione/view";
	}
}
