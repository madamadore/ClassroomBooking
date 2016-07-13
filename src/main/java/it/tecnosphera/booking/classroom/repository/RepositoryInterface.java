package it.tecnosphera.booking.classroom.repository;

import java.util.List;

public interface RepositoryInterface<T> {

	public T find(long id);
	public List<T> findAll();
	public List<T> find(String name);
	public long save(T object);
	public boolean delete(long id);
	public String MD5Hashing(String password);
}
