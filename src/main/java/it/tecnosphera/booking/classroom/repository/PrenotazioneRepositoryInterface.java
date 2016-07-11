package it.tecnosphera.booking.classroom.repository;

import it.tecnosphera.booking.classroom.model.Prenotazione;

import java.util.Date;
import java.util.List;

public interface PrenotazioneRepositoryInterface extends RepositoryInterface<Prenotazione> {

	public List<Prenotazione> getPrenotazioni(Date da, Date a);
	
}
