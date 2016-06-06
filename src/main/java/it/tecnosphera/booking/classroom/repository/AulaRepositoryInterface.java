package it.tecnosphera.booking.classroom.repository;

import it.tecnosphera.booking.classroom.model.Aula;

import java.util.List;

public interface AulaRepositoryInterface {

	public Aula getAula(long id);
	public List<Aula> getAulas();
	public List<Aula> getAulas(String name);
	public List<Aula> getAulas(String name,  String description);
	public long save(Aula aula);
	public boolean delete(Aula aula);
	
}
