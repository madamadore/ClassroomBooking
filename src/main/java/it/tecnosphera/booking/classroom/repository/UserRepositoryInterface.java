package it.tecnosphera.booking.classroom.repository;

import it.tecnosphera.booking.classroom.model.User;

import java.util.List;

public interface UserRepositoryInterface {

	public User getUser(long id);
	public List<User> getUsers();
	public List<User> getUsers(String name);
	public long save(User user);
	public boolean delete(User user);
	
}
