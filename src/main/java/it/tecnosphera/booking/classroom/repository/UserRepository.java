package it.tecnosphera.booking.classroom.repository;

import it.tecnosphera.booking.classroom.model.User;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements UserRepositoryInterface {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public User getUser(long id) {
		User user = entityManager.find(User.class, id);
		return user;
	}

	@Override
	public List<User> getUsers() {
		List<User> lista = entityManager.createQuery("SELECT u FROM User u").getResultList();
		return lista;
	}	

	@Transactional
	@Override
	public long save(User user) {
		entityManager.persist(user);
		entityManager.flush();
		return user.getId();	
	}

	@Transactional
	@Override
	public boolean delete(User user) {
		entityManager.remove(user);
		return true;
	}

	@Override
	public List<User> getUsers(String name) {
		List<User> lista = entityManager.createQuery("SELECT u FROM User u WHERE u.name LIKE :name")
				.setParameter("name", "%" + name + "%")
				.getResultList();
		return lista;
	}
	
}
