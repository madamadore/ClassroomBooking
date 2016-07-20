package it.tecnosphera.booking.classroom.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="utenti")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private long id;
	
	
	@Column(name="name")
	private String name;
	
	@Column(name="surname")
	private String cognome;
	
	@Column(name="email", unique = true)
	private String email;
	
	@Column(name="password")
	private String password;
	
	@Column(name="conf_password")
	private String conf_password;
	
	@Column(name="enabled", columnDefinition="tinyint")
	private boolean enabled;
	
	@OneToMany(fetch = FetchType.LAZY, 
			cascade={CascadeType.ALL}, 
			mappedBy = "user")
	private Set<UserRole> userRole = new HashSet<UserRole>(0);
	
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getCognome() {
		return cognome;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConf_password() {
		return conf_password;
	}
	public void setConf_password(String conf_password) {
		this.conf_password = conf_password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Set<UserRole> getUserRole() {
		return userRole;
	}
	public void setUserRole(Set<UserRole> userRole) {
		this.userRole = userRole;
	}
}
