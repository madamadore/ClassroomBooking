package it.tecnosphera.booking.classroom.repository;

import java.util.Date;
import java.util.List;
import it.tecnosphera.booking.classroom.model.Aula;
import it.tecnosphera.booking.classroom.model.User;

import it.tecnosphera.booking.classroom.model.Prenotazione;

public interface PrenotazioneRepositoryInterface extends RepositoryInterface<Prenotazione> {

	public List<Prenotazione> getPrenotazioniInConflitto(Date da, Date a, Aula aula);
	
}
