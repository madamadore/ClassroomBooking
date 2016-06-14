package it.tecnosphera.booking.classroom.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="user_roles")
public class UserRole implements Serializable {

	@Id
	@Column(name="user_role_id")
	private Integer userRoleId;
	
	@ManyToOne
    @JoinColumn(name="email")
	private User user;
	
	@Column(name="ROLE")
	private String role;
	
	public Integer getUserRoleId() {
		return userRoleId;
	}
	
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
}
