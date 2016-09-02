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

import it.tecnosphera.booking.classroom.model.Aula;
import it.tecnosphera.booking.classroom.model.Lezione;
import it.tecnosphera.booking.classroom.model.Prenotazione;
import it.tecnosphera.booking.classroom.model.Role;
import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.repository.LezioneRepositoryInterface;

@Controller
@RequestMapping(value = "/lezioni")
public class LezioniController extends PrenotazioniController {

	@Autowired
	LezioneRepositoryInterface lezioneRepository;

	public static final String LIMIT_EXEEDED = "limitExeeded";

	@RequestMapping(value = "/view")
	public String visualizzaPrenotazione() {
		return "lezioni/view";
	}

	@RequestMapping(value = "/ajaxLezioni/save", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse save(Model m, @RequestParam("aula") String aula,
			@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam("id") String id, @RequestParam("titolo") String titolo, @RequestParam("limite") String limite,
			@RequestParam("docente") String docente, @RequestParam("descrizione") String descrizione) {
		if (!utilityMethods.isLogged()) {
			return new AjaxResponse(LOGIN_REQUIRED, null);
		}

		// se mancano dei parametri
		if ("".equals(startTime) || "".equals(endTime) || "".equals(startDate) || "".equals(endDate)) {
			return new AjaxResponse(MISSING_INPUT, null);
		}

		if (!utilityMethods.hasRole(Role.ROLE_TEACHER))
			return new AjaxResponse(MISSING_PERMISSION, null);
		
		org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User user = userRepository.findByEmail(u.getUsername());

		Lezione lezione = new Lezione();
		Date start = utilityMethods.creaData(startTime, startDate);
		Date end = utilityMethods.creaData(endTime, endDate);
		Aula a = null;
		try {
			a = aulaRepository.find(Long.parseLong(aula));
		} catch (NumberFormatException e) {
			return new AjaxResponse(MISSING_INPUT, null);
		}
		lezione.setTitle(titolo);
		lezione.setClassRoom(a);
		lezione.setStart(start);
		lezione.setEnd(end);
		User owner = user;
		if (id != null && !id.isEmpty()) {
			long preId = Long.parseLong(id);
			lezione.setId(preId);
			Lezione vecchiaLezione = lezioneRepository.find(preId);
			owner = vecchiaLezione.getOwner();
			lezione.setIscritti(vecchiaLezione.getIscritti());
		}
		lezione.setOwner(owner);
		lezione.setDescrizione(descrizione);
		lezione.setDocente(docente);
		if (limite != null && !limite.isEmpty()) {
			lezione.setLimite(Integer.parseInt(limite.trim()));
		} else {
			lezione.setLimite(a.getCapienza());
		}
		if (lezione.getId() > 0) {
			if (!utilityMethods.hasPermissions(lezioneRepository.find(lezione.getId()).getOwner())) {
				return new AjaxResponse(MISSING_PERMISSION, null);
			}
		}
		if (utilityMethods.verificaPrenotazione(lezione)) {
			lezioneRepository.save(lezione);
			return new AjaxResponse(OK, lezione);
		} else {
			return new AjaxResponse(PRENOTAZIONE_CONFLICT, null);
		}
	}

	@RequestMapping(value = "/ajaxLezioni/delete", method = RequestMethod.POST)
	public @ResponseBody String deleteEvent(@RequestParam("id") String id) {
		if (!utilityMethods.isLogged()) {
			return LOGIN_REQUIRED;
		}
		long idLezione = Long.parseLong(id);
		if (!utilityMethods.hasPermissions(lezioneRepository.find(idLezione).getOwner())) {
			return MISSING_PERMISSION;
		}
		Prenotazione p = lezioneRepository.find(idLezione);
		lezioneRepository.delete(idLezione);
		return OK;
	}

	@RequestMapping(value = "/ajaxLezioni/iscriviti", method = RequestMethod.POST)
	public @ResponseBody String iscriviti(@RequestParam("idLezione") String idLezione) {
		if (!utilityMethods.isLogged()) {
			return LOGIN_REQUIRED;
		}
		org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User user = userRepository.findByEmail(u.getUsername());
		Lezione lezione = lezioneRepository.find(Long.parseLong(idLezione));
		List<User> iscritti = lezione.getIscritti();
		iscritti.add(user);
		if (iscritti.size() > lezione.getLimite()) {
			return LIMIT_EXEEDED;
		}
		lezioneRepository.save(lezione);
		return OK;
	}

	@RequestMapping(value = "/ajaxLezioni/annullaIscrizione", method = RequestMethod.POST)
	public @ResponseBody String annullaIscrizione(@RequestParam("idLezione") String idLezione) {
		if (!utilityMethods.isLogged()) {
			return LOGIN_REQUIRED;
		}
		org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User user = userRepository.findByEmail(u.getUsername());

		Lezione lezione = lezioneRepository.find(Long.parseLong(idLezione));
		lezione.getIscritti().remove(user);
		lezioneRepository.save(lezione);
		return OK;
	}

	@RequestMapping(value = "/ajaxLezioni/verificaIscrizione", method = RequestMethod.POST)
	public @ResponseBody boolean verificaIscrizione(@RequestParam("idLezione") String idLezione) {
		if (!utilityMethods.isLogged()) {
			return false;
		}
		org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		User user = userRepository.findByEmail(u.getUsername());
		Lezione lezione = lezioneRepository.find(Long.parseLong(idLezione));
		return lezione.getIscritti().contains(user);
	}

	@RequestMapping(value = "/ajaxLezioni/getIscritti", method = RequestMethod.POST)
	public @ResponseBody String[] getIscritti(@RequestParam("idLezione") String idLezione) {
		Object[] iscritti = lezioneRepository.find(Long.parseLong(idLezione)).getIscritti().toArray();
		String[] nomi = new String[iscritti.length];
		for (int i = 0; i < nomi.length; i++) {
			User u = (User) iscritti[i];
			nomi[i] = u.getName() + " " + u.getCognome();
		}
		return nomi;
	}
}
