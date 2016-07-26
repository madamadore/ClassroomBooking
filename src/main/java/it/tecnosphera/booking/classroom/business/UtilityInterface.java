package it.tecnosphera.booking.classroom.business;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestBody;

import it.tecnosphera.booking.classroom.model.Prenotazione;
import it.tecnosphera.booking.classroom.model.User;

public interface UtilityInterface {
	public boolean isLogged();
	public boolean verificaPrenotazione(Prenotazione prenotazione);
	public Date creaData(String time, String date);
	public boolean hasPermissions(@RequestBody User owner);
}
