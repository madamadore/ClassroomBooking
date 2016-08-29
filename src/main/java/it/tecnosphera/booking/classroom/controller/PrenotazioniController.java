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
import org.springframework.web.bind.annotation.ResponseBody;

import it.tecnosphera.booking.classroom.business.UtilityMethods;
import it.tecnosphera.booking.classroom.model.Aula;
import it.tecnosphera.booking.classroom.model.Lezione;
import it.tecnosphera.booking.classroom.model.Prenotazione;
import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.repository.PrenotazioneRepositoryInterface;
import it.tecnosphera.booking.classroom.repository.RepositoryInterface;
import it.tecnosphera.booking.classroom.repository.UserRepositoryInterface;

@Controller
@RequestMapping(value = "/prenotazioni")
public class PrenotazioniController {

	@Autowired
	private PrenotazioneRepositoryInterface prenotazioneRepository;

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
	public static final String PRENOTAZIONE_CONFLICT = "conflict";

	@RequestMapping(value = "/ajax/view")
	public String visualizzaPrenotazione() {
		return "prenotazioni/view";
	}

	@RequestMapping(value = "/ajax/list", method = RequestMethod.GET)
	public @ResponseBody List<Prenotazione> elaboraPrenotazioni() {
		return prenotazioneRepository.findAll();
	}

	@RequestMapping(value = "/ajax/save", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse save(Model m, @RequestParam("aula") String aula,
			@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam("id") String id) {
		System.out.println("startDate = " + startDate);
		if (!utilityMethods.isLogged()) {
			return new AjaxResponse(LOGIN_REQUIRED, null);
		}

		// se mancano dei parametri
		if ("".equals(startTime) || "".equals(endTime) || "".equals(startDate) || "".equals(endDate)) {
			return new AjaxResponse(MISSING_INPUT, null);
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
			return new AjaxResponse(MISSING_INPUT, null);
		}
		prenotazione.setTitle(a.getName());
		prenotazione.setClassRoom(a);
		prenotazione.setStart(start);
		prenotazione.setEnd(end);
		User owner = user;
		if (id != null && !id.isEmpty()) {
			long preId = Long.parseLong(id);
			prenotazione.setId(preId);
			owner = prenotazioneRepository.find(preId).getOwner();
		}
		prenotazione.setOwner(owner);

		if (prenotazione.getId() > 0) {
			if (!utilityMethods.hasPermissions(prenotazioneRepository.find(prenotazione.getId()).getOwner())) {
				return new AjaxResponse(MISSING_PERMISSION, null);
			}
		}
		if (utilityMethods.verificaPrenotazione(prenotazione)) {
			prenotazioneRepository.save(prenotazione);
			return new AjaxResponse(OK, prenotazione);
		} else {
			return new AjaxResponse(PRENOTAZIONE_CONFLICT, null);
		}
	}

	@RequestMapping(value = "/ajax/delete", method = RequestMethod.POST)
	public @ResponseBody String deleteEvent(@RequestParam("id") String id) {
		if (!utilityMethods.isLogged()) {
			return LOGIN_REQUIRED;
		}
		long idPrenotazione = Long.parseLong(id);
		if (!utilityMethods.hasPermissions(prenotazioneRepository.find(idPrenotazione).getOwner())) {
			return MISSING_PERMISSION;
		}
		Prenotazione p = prenotazioneRepository.find(idPrenotazione);
		prenotazioneRepository.delete(idPrenotazione);
		return OK;
	}

	protected class AjaxResponse {
		String result;
		Prenotazione event;

		public AjaxResponse(String result, Prenotazione event) {
			super();
			this.result = result;
			this.event = event;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public Prenotazione getEvent() {
			return event;
		}

		public void setEvent(Prenotazione event) {
			this.event = event;
		}
	}
}
