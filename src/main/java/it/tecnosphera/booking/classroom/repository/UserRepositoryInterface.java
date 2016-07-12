package it.tecnosphera.booking.classroom.repository;

import java.util.List;

import it.tecnosphera.booking.classroom.model.User;
import it.tecnosphera.booking.classroom.model.UserRole;

public interface UserRepositoryInterface extends RepositoryInterface<User> {
	User findByUserName(String username);
	List<UserRole> distinctUserRole(User user);
}
