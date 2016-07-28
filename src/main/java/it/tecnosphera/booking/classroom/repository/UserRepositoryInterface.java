package it.tecnosphera.booking.classroom.repository;

import java.util.List;

import it.tecnosphera.booking.classroom.model.User;

public interface UserRepositoryInterface extends RepositoryInterface<User> {

	User findByEmail(String username);
	public String MD5Hashing(String password);
}
