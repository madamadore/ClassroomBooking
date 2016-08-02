package it.tecnosphera.booking.classroom.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.tecnosphera.booking.classroom.model.Lezione;

public class LezioneRepository extends PrenotazioneRepository implements LezioneRepositoryInterface{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Lezione> findAllLezioni() {
		List<Lezione> lista = entityManager.createQuery("SELECT u FROM Lezione u").getResultList();
		return lista;
	}
}
