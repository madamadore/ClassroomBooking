package it.tecnosphera.booking.classroom.repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import it.tecnosphera.booking.classroom.model.User;

@Repository("userDao")
public class UserRepository implements UserRepositoryInterface {

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
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
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(user);
		session.flush();
		return user.getId();
	}
	
	@Transactional
	@Override
	public boolean delete(long id) {
		User user = entityManager.find(User.class, id);
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
	
	public User findByEmail(String email) {
		 
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

	public List<?> distinctUserRole() {
		List<?> lista = entityManager.createQuery(
				"SELECT DISTINCT r.role, r.descrizione from UserRole as r")
				.getResultList();
		return lista;
	}
	
	public String MD5Hashing(String password){
    	
    	MessageDigest md;
    	StringBuffer sb = new StringBuffer();
    	
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
	        byte byteData[] = md.digest();
	 
	        //convert the byte to hex format
	        for (int i = 0; i < byteData.length; i++) {
	        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
    }
	
}
