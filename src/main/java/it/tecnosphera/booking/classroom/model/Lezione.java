package it.tecnosphera.booking.classroom.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "lezioni")
@DiscriminatorValue(value = "Lezione")
public class Lezione extends Prenotazione {

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_lezioni", joinColumns = { @JoinColumn(name = "lezione_id") }, inverseJoinColumns = {
			@JoinColumn(name = "user_id") })
	private List<User> iscritti = new ArrayList<User>();

	@Column(name = "limite")
	private int limite;

	@Column(name = "description")
	private String description;

	@OneToOne
	@JoinColumn(name = "docente")
	private String docente;

	public List<User> getIscritti() {
		return iscritti;
	}

	public void setIscritti(List<User> iscritti) {
		this.iscritti = iscritti;
	}

	public int getLimite() {
		return limite;
	}

	public void setLimite(int limite) {
		this.limite = limite;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDocente() {
		return docente;
	}

	public void setDocente(String docente) {
		this.docente = docente;
	}
}
