package it.tecnosphera.booking.classroom.repository;

import java.util.List;

import it.tecnosphera.booking.classroom.model.Aula;

public interface AulaRepositoryInterface extends RepositoryInterface<Aula> {
	
	public List<Aula> find(String name);
}
