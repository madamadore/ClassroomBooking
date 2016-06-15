package it.tecnosphera.booking.classroom.repository;

import it.tecnosphera.booking.classroom.model.Aula;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AulaRepository implements RepositoryInterface<Aula> {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Aula find(long id) {
		return em.find(Aula.class, id);
	}

	@Override
	public List<Aula> findAll() {
		return em.createQuery("SELECT a FROM Aula a").getResultList();
	}

	@Override
	public List<Aula> find(String name) {
		List<Aula> lista = em.createQuery("SELECT a FROM Aula a WHERE a.name LIKE :name")
				.setParameter("name", "%" + name + "%")
				.getResultList();
		return lista;
	}

	@Override
	@Transactional
	public long save(Aula object) {
		Session session = em.unwrap(Session.class);
		session.persist(object);
		session.flush();
		return object.getId();
	}

	@Override
	@Transactional
	public boolean delete(long id) {
		Aula aula = em.find(Aula.class, id);
		em.remove(aula);
		return true;
	}

}
