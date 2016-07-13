package it.tecnosphera.booking.classroom.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.tecnosphera.booking.classroom.model.Aula;
import it.tecnosphera.booking.classroom.model.Prenotazione;
import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.repository.RepositoryInterface;
import it.tecnosphera.booking.classroom.repository.UserRepositoryInterface;

@Controller
@RequestMapping(value = "/prenotazioni")
public class PrenotazioniController {

	@Autowired
	RepositoryInterface<Prenotazione> prenotazioneRepository;

	@Autowired
	RepositoryInterface<Aula> aulaRepository;
	
	@Autowired
	UserRepositoryInterface userRepository;

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String nuovaPrenotazione(@ModelAttribute("aula") long aula, @ModelAttribute("startTime") String startTime,
			@ModelAttribute("endTime") String endTime, @ModelAttribute("startDate") String startDate,
			@ModelAttribute("endDate") String endDate) {
		
		//se non sei loggato vieni reindirizzato alla pagina di login
		if (SecurityContextHolder.getContext().getAuthentication() == null
				|| !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				|| "anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
			return "login";
		}
		
		User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
		Prenotazione prenotazione = new Prenotazione();
		Date start = new Date();
		Date end = new Date();
		// TODO settare qui i valori delle date
		Aula a = aulaRepository.find(aula);
		prenotazione.setOwner(user);
		prenotazione.setClassRoom(a);
		prenotazione.setStart(start);
		prenotazione.setEnd(end);

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
		// TODO deve verificare che l'aula non sia già occupata nell'intervallo specificato
		return true;
	}
}
