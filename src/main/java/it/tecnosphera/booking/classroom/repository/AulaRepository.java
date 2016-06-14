package it.tecnosphera.booking.classroom.repository;

import it.tecnosphera.booking.classroom.model.Aula;
import it.tecnosphera.booking.classroom.model.User;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
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

	@Transactional
	@Override
	public long save(Aula object) {
		em.persist(object);
		em.flush();
		return object.getId();
	}

	@Transactional
	@Override
	public boolean delete(Aula object) {
		em.remove(object);
		return false;
	}

}
