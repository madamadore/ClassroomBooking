package it.tecnosphera.booking.classroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import it.tecnosphera.booking.classroom.model.Prenotazione;
import it.tecnosphera.booking.classroom.repository.PrenotazioneRepositoryInterface;

@Controller
@RequestMapping(value = "/prenotazioni")
public class PrenotazioniController {

	@Autowired
	PrenotazioneRepositoryInterface prenotazioneRepository;

	@RequestMapping(value = "/new")
	public String nuovaPrenotazione(@ModelAttribute("prenotazione") Prenotazione prenotazione) {
		if (verificaPrenotazione(prenotazione)) {
			prenotazioneRepository.save(prenotazione);
		}
		return "prenotazioni/list";
	}

	@RequestMapping(value = "/view")
	public String visualizzaPrenotazione() {
		return "prenotazioni/view";
	}

	@RequestMapping(value = "/save")
	public String save() {
		return "prenotazioni/save";
	}
	
	private boolean verificaPrenotazione(Prenotazione prenotazione) {
		// TODO deve verificare la coerenza degli input e che l'aula non sia già prenotata nella data in questione
		return true;
	}
}
