package it.tecnosphera.booking.classroom.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import it.tecnosphera.booking.classroom.model.Aula;

@Repository
public class AulaRepositoryFake implements AulaRepositoryInterface {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Aula getAula(long id) {
		Aula aula = new Aula();
		aula.setId(id);
		aula.setName("Aula Corsi");
		aula.setDescription("Aula al sesto piano, San Donato (MI)");
		return aula;
	}

	@Override
	public List<Aula> getAulas() {
		List<Aula> aule = new ArrayList<Aula>();
		aule.add(getAula(1L));
		aule.add(getAula(2L));
		return aule;
	}

	@Override
	public List<Aula> getAulas(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Aula> getAulas(String name, String description) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long save(Aula aula) {
		entityManager.persist(aula);
		entityManager.flush();
		return aula.getId();	
	}

	@Override
	public boolean delete(Aula aula) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
