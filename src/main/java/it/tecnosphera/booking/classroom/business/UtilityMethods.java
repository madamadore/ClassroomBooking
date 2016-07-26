package it.tecnosphera.booking.classroom.business;

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
	
	@Override
	public boolean isLogged() {
		return !(SecurityContextHolder.getContext().getAuthentication() == null
				|| !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				|| "anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName()));
	}

	@Override
	public boolean verificaPrenotazione(Prenotazione prenotazione) {
		// verifica che start ed end siano coerenti, ovvero che lo start sia
		// precedente rispetto all'end
		if (prenotazione.getStart().compareTo(prenotazione.getEnd()) >= 0) {
			return false;
		}

		List<Prenotazione> list = null;

		list = prenotazioneRepository.getPrenotazioniInConflitto(prenotazione.getStart(), prenotazione.getEnd(),
				prenotazione.getClassRoom());

		if (list != null) {
			if (list.size() > 1 || (list.size() == 1 && list.get(0).getId() != prenotazione.getId()))
				return false;
		}

		if (prenotazione.getId() > 0) {
			if (!hasPermissions(prenotazioneRepository.find(prenotazione.getId()).getOwner())) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Date creaData(String time, String date) {
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

	@Override
	public boolean hasPermissions(User owner) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
				&& !auth.getName().equals(owner.getEmail())) {
			return new Boolean(false);
		}
		return new Boolean(true);
	}
}
