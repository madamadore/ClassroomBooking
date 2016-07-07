package it.tecnosphera.booking.classroom.repository;

import it.tecnosphera.booking.classroom.model.User;

public interface UserRepositoryInterface extends RepositoryInterface<User> {
	User findByEmail(String username);
}
