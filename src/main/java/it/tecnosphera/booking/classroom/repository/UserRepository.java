package it.tecnosphera.booking.classroom.repository;

import it.tecnosphera.booking.classroom.model.User;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
	
	protected User createUser(long id, String nome, String cognome, String email, String password) {
		User user = new User();
		user.setId(id);
		user.setName(nome);
		user.setCognome(cognome);
		user.setEmail(email);
		user.setPassword(password);
		return user;
	}

	@Override
	public List<User> getUsers() {
		List<User> lista = entityManager.createQuery("SELECT u FROM User u").getResultList();
		return lista;
	}	

	@Override
	public long save(User user) {
		entityManager.persist(user);
		entityManager.flush();
		return user.getId();	
	}

	@Override
	public boolean delete(User user) {
		entityManager.remove(user);
		return true;
	}

	@Override
	public List<User> getUsers(String name) {
		List<User> lista = entityManager.createQuery("SELECT u FROM User u WHERE u.name=:name")
				.setParameter("name", name)
				.getResultList();
		return lista;
	}
	
}
