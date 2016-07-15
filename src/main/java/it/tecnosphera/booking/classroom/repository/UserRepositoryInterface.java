package it.tecnosphera.booking.classroom.repository;

import java.util.List;

import it.tecnosphera.booking.classroom.model.User;

public interface UserRepositoryInterface extends RepositoryInterface<User> {

	List<?> distinctUserRole();
	User findByEmail(String username);

}
