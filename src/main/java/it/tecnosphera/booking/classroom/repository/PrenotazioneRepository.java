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

	@Transactional
	@Override
	public boolean delete(long id) {
		Prenotazione p = entityManager.find(Prenotazione.class, id);
		entityManager.remove(p);
		return true;
	}

	@Override
	public List<Prenotazione> getPrenotazioni(Date da, Date a, Aula aula) {

		Query q = entityManager.createQuery(
				"SELECT p FROM Prenotazione p WHERE ((p.start > ? AND p.start < ?) OR (p.end > ? AND p.end < ?)) AND p.classRoom = ?");
		q.setParameter(1, da);
		q.setParameter(2, a);
		q.setParameter(3, da);
		q.setParameter(4, a);
		q.setParameter(5, aula);
		List<Prenotazione> lista = q.getResultList();
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

	@Override
	public List<Prenotazione> getPrenotazioni(Date da, Date a, Aula aula, long id) {
		Query q = entityManager.createQuery(
				"SELECT p FROM Prenotazione p WHERE ((p.start > ? AND p.start < ?) OR (p.end > ? AND p.end < ?)) AND p.classRoom = ? AND p.id <> ?");
		q.setParameter(1, da);
		q.setParameter(2, a);
		q.setParameter(3, da);
		q.setParameter(4, a);
		q.setParameter(5, aula);
		q.setParameter(6, id);
		List<Prenotazione> lista = q.getResultList();
		return lista;
	}
}
