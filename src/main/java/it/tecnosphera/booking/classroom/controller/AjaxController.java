package it.tecnosphera.booking.classroom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.tecnosphera.booking.classroom.model.Prenotazione;
import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.repository.PrenotazioneRepositoryInterface;

@Controller
@RequestMapping(value = "/ajax")
public class AjaxController {

	@Autowired
	PrenotazioneRepositoryInterface prenotazioneRepository;
	
	@RequestMapping(value = "/prenotazioni", method = RequestMethod.GET)
	public @ResponseBody List<Prenotazione> elaboraPrenotazioni() {
		return prenotazioneRepository.findAll();
	}
	
	@RequestMapping(value = "/hasPermissions", method = RequestMethod.POST)
	private @ResponseBody Boolean hasPermissions(@RequestBody User owner) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
				&& !auth.getName().equals(owner.getEmail())) {
			return new Boolean(false);
		}
		return new Boolean(true);
	}
}