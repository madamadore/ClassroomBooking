package it.tecnosphera.booking.classroom.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "prenotazioni")
public class Prenotazione {

	private String title;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;

	@Column(name = "start_date")
	private Date start;

	@Column(name = "end_date")
	private Date end;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User owner;

	@OneToOne
	@JoinColumn(name = "aula_id")
	private Aula classRoom;

	@Column(name = "description")
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

	public String getTitle() {
		if (title == null)
			title = getClassRoom().getName();
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
