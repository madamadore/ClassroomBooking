package it.tecnosphera.booking.classroom.repository;

import it.tecnosphera.booking.classroom.model.Prenotazione;

import java.util.Date;
import java.util.List;

public interface PrenotazioneRepositoryInterface {

	public Prenotazione getPrenotazione(long id);
	public List<Prenotazione> getPrenotazioni(Date da, Date a);
	public long save(Prenotazione prenotazione);
	public boolean cancel(Prenotazione prenotazione);
	
}
