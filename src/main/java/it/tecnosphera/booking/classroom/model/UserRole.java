package it.tecnosphera.booking.classroom.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="user_roles")
public class UserRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="user_role_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer userRoleId;
	
	@JsonIgnore 
	@ManyToOne
    @JoinColumn(name="user_id")
	private User user;
	
	@Column(name="ruolo")
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
