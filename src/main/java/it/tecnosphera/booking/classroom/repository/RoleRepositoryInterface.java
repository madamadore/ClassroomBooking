package it.tecnosphera.booking.classroom.repository;

import java.util.List;

import it.tecnosphera.booking.classroom.model.Role;

public interface RoleRepositoryInterface extends RepositoryInterface<Role> {

	public List<Role> find(String name);
}
