package it.tecnosphera.booking.classroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import it.tecnosphera.booking.classroom.model.Prenotazione;
import it.tecnosphera.booking.classroom.repository.RepositoryInterface;

@Controller
public class PrenotazioniController {

	@Autowired
	RepositoryInterface<Prenotazione> prenotazioneRepository;
	
}
