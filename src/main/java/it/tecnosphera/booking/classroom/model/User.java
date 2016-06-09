package it.tecnosphera.booking.classroom.model;

public class User {
	private long id;
	private String name;
	private String cognome;
	private String email;
	private String password;
	
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
}
