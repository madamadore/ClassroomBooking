package it.tecnosphera.booking.classroom.repository;

import java.util.List;

public interface RepositoryInterface<T> {

	public T find(long id);
	public List<T> findAll();
	public long save(T object);
	public boolean delete(long id);
}
