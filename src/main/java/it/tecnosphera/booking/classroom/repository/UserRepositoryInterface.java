package it.tecnosphera.booking.classroom.repository;

import java.util.List;

import it.tecnosphera.booking.classroom.model.User;

public interface UserRepositoryInterface extends RepositoryInterface<User> {
	User findByUserName(String username);
	List<?> distinctUserRole();
}
