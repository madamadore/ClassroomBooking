package it.tecnosphera.booking.classroom.model;

public class Aula {
	private long id;
	private String name;
	private String description;
	
	public Aula() {
		name = "";
		description = "";
	}
	
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
