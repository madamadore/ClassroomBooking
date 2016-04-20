package it.technosphera.booking.classroom.repository;

import java.util.List;

import it.technosphera.booking.classroom.model.User;

public interface UserRepositoryInterface {

	public User getUser(long id);
	public List<User> getUsers();
	public List<User> getUsers(String name);
	public long save(User user);
	public boolean delete(User user);
	
}
