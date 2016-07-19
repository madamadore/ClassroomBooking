package it.tecnosphera.booking.classroom.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.tecnosphera.booking.classroom.model.Aula;
import it.tecnosphera.booking.classroom.model.Prenotazione;
import it.tecnosphera.booking.classroom.model.User;
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

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String nuovaPrenotazione(Model m, @RequestParam("aula") long aula,
			@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {

		if (nonLoggato()) {
			return "login";
		}

		// se mancano dei parametri
		if ("".equals(startTime) || "".equals(endTime) || "".equals(startDate) || "".equals(endDate)) {
			return "redirect:/prenotazioni/error";
		}

		org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User user = userRepository.findByEmail(u.getUsername());
		Prenotazione prenotazione = new Prenotazione();
		Date start = creaData(startTime, startDate);
		Date end = creaData(endTime, endDate);

		Aula a = aulaRepository.find(aula);
		prenotazione.setOwner(user);
		prenotazione.setClassRoom(a);
		prenotazione.setStart(start);
		prenotazione.setEnd(end);

		if (verificaPrenotazione(prenotazione)) {
			prenotazioneRepository.save(prenotazione);
			return "redirect:/";
		} else {
			return "redirect:/prenotazioni/error";
		}

	}

	private Date creaData(String time, String date) {
		Date dateObj = new Date();
		String[] t = time.split(":");
		String[] d = date.split("-");
		dateObj.setHours(Integer.parseInt(t[0]));
		dateObj.setMinutes(Integer.parseInt(t[1]));
		dateObj.setSeconds(0);
		dateObj.setDate(Integer.parseInt(d[0]));
		dateObj.setMonth(Integer.parseInt(d[1]) - 1);
		dateObj.setYear(Integer.parseInt(d[2]) - 1900);
		return dateObj;
	}

	private boolean nonLoggato() {
		return SecurityContextHolder.getContext().getAuthentication() == null
				|| !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				|| "anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName());
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

	private boolean verificaPrenotazione(Prenotazione prenotazione) {
		List<Prenotazione> list = prenotazioneRepository.getPrenotazioni(prenotazione.getStart(),
				prenotazione.getEnd());
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}
}
