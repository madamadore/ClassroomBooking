package it.tecnosphera.booking.classroom.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import it.tecnosphera.booking.classroom.model.Prenotazione;
import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.repository.PrenotazioneRepositoryInterface;

@Service
public class UtilityMethods implements UtilityInterface {

	@Autowired
	PrenotazioneRepositoryInterface prenotazioneRepository;

	public boolean isLogged() {
		return !(SecurityContextHolder.getContext().getAuthentication() == null
				|| !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				|| "anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName()));
	}

	public boolean verificaPrenotazione(Prenotazione prenotazione) {

		List<Prenotazione> list = null;

		list = prenotazioneRepository.getPrenotazioniInConflitto(prenotazione.getStart(), prenotazione.getEnd(),
				prenotazione.getClassRoom());

		if (list != null) {
			if (list.size() > 1 || (list.size() == 1 && list.get(0).getId() != prenotazione.getId()))
				return false;
		}
		return true;
	}

	public Date creaData(String time, String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date data = null;
		try {
			data = formatter.parse(date + " " + time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data;
	}

	public boolean hasPermissions(User owner) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
				&& !auth.getName().equals(owner.getEmail())) {
			return new Boolean(false);
		}
		return new Boolean(true);
	}

	public boolean hasRole(String role) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(role))) {
			return false;
		}
		return true;
	}
}
