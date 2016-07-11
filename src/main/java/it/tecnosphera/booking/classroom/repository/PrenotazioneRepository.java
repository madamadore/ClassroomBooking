package it.tecnosphera.booking.classroom.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.tecnosphera.booking.classroom.model.Prenotazione;
import it.tecnosphera.booking.classroom.model.User;

@Repository("prenotazioneDao")
public class PrenotazioneRepository implements PrenotazioneRepositoryInterface {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Prenotazione find(long id) {
		Prenotazione prenotazione = entityManager.find(Prenotazione.class, id);
		return prenotazione;
	}

	@Override
	public List<Prenotazione> findAll() {
		List<Prenotazione> lista = entityManager.createQuery("SELECT u FROM Prenotazione u").getResultList();
		return lista;
	}

	@Override
	public List<Prenotazione> find(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(long id) {
		Prenotazione p = entityManager.find(Prenotazione.class, id);
		entityManager.remove(p);
		return true;
	}

	@Override
	public List<Prenotazione> getPrenotazioni(Date da, Date a) {
		List<Prenotazione> lista = entityManager
				.createQuery(
						"SELECT p FROM Prenotazione p WHERE (p.start BETWEEN :da AND :a) OR (p.end BETWEEN :da AND :a) ")
				.getResultList();
		return lista;
	}

	@Transactional
	@Override
	public long save(Prenotazione prenotazione) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(prenotazione);
		session.flush();
		return prenotazione.getId();
	}
}
