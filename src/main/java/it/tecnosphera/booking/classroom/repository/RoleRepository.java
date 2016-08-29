package it.tecnosphera.booking.classroom.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import it.tecnosphera.booking.classroom.model.Role;

@Repository
@Transactional
public class RoleRepository implements RoleRepositoryInterface {

	@PersistenceContext
	private EntityManager entityManager;

	public Role find(long id) {
		Role role = entityManager.find(Role.class, id);
		return role;
	}

	public List<Role> findAll() {
		List<Role> roles = entityManager.createQuery("SELECT r FROM Role r").getResultList();
		return roles;
	}

	public List<Role> find(String name) {
		Query q = entityManager.createQuery("SELECT r FROM Role r WHERE r.name = ?");
		q.setParameter(1, name);
		return q.getResultList();
	}
//	@Override
//	public List<Role> find(String name) {
//		List<Role> lista = entityManager.createQuery("SELECT r FROM Role r WHERE r.name LIKE :name")
//				.setParameter("name", "%" + name + "%").getResultList();
//		return lista;
//	}

	public long save(Role role) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(role);
		session.flush();
		return role.getId();
	}

	public boolean delete(long id) {
		Role role = entityManager.find(Role.class, id);
		entityManager.remove(role);
		return true;
	}

}
