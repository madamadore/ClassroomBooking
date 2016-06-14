package it.tecnosphera.booking.classroom.repository;

import it.tecnosphera.booking.classroom.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements UserRepositoryInterface {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public User find(long id) {
		User user = entityManager.find(User.class, id);
		return user;
	}

	@Override
	public List<User> findAll() {
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
	public List<User> find(String name) {
		List<User> lista = entityManager.createQuery("SELECT u FROM User u WHERE u.name LIKE :name")
				.setParameter("name", "%" + name + "%")
				.getResultList();
		return lista;
	}
	
	public User findByUserName(String email) {
		 
		List<User> users = new ArrayList<User>();
 
		Session session = entityManager.unwrap(Session.class);
		users = session
				.createQuery("from User where email=:email")
				.setParameter("email", email).list();
 
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}
 
	}
	
}
