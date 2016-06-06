package it.tecnosphera.booking.classroom.model;

import java.util.Date;

public class Prenotazione {
	private long id;
	private Date start;
	private Date end;
	private User owner;
	private Aula classRoom;
	private String description;
	
	public long getId() {
		return id;
	}
	public Date getStart() {
		return start;
	}
	public Date getEnd() {
		return end;
	}
	public User getOwner() {
		return owner;
	}
	public Aula getClassRoom() {
		return classRoom;
	}
	public String getDescription() {
		return description;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public void setClassRoom(Aula classRoom) {
		this.classRoom = classRoom;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
}
