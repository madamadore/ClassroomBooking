package main.java.it.tecnosphera.booking.classroom.repository;

import java.util.List;

import main.java.it.tecnosphera.booking.classroom.model.Aula;

public interface AulaRepositoryInterface {

	public Aula getAula(long id);
	public List<Aula> getAulas();
	public List<Aula> getAulas(String name);
	public List<Aula> getAulas(String name,  String description);
	public long save(Aula aula);
	public boolean delete(Aula aula);
	
}
