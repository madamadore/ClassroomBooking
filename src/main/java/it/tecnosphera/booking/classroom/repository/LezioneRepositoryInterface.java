package it.tecnosphera.booking.classroom.repository;

import java.util.List;

import it.tecnosphera.booking.classroom.model.Lezione;

public interface LezioneRepositoryInterface extends PrenotazioneRepositoryInterface {

	List<Lezione> findAllLezioni();

}
