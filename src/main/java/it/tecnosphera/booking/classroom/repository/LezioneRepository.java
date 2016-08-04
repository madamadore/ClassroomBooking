package it.tecnosphera.booking.classroom.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.tecnosphera.booking.classroom.model.Aula;
import it.tecnosphera.booking.classroom.model.Lezione;
import it.tecnosphera.booking.classroom.model.Prenotazione;

@Repository("lezioneDao")
public class LezioneRepository implements LezioneRepositoryInterface{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	@Override
	public long save(Lezione lezione) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(lezione);
		session.flush();
		return lezione.getId();
	}
	
	public List<Lezione> findAll() {
		List<Lezione> lista = entityManager.createQuery("SELECT u FROM Lezione u").getResultList();
		return lista;
	}

	@Override
	public Lezione find(long id) {
		Lezione lezione = entityManager.find(Lezione.class, id);
		return lezione;
	}

	@Override
	public boolean delete(long id) {
		Lezione lezione = entityManager.find(Lezione.class, id);
		entityManager.remove(lezione);
		return true;
	}
}
