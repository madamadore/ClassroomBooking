package it.tecnosphera.booking.classroom.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "lezioni")
@DiscriminatorValue(value = "Lezione")
public class Lezione extends Prenotazione {

	@Transient
	@JsonProperty
	private final String type = "Lezione";

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_lezioni", joinColumns = { @JoinColumn(name = "lezione_id") }, inverseJoinColumns = {
			@JoinColumn(name = "user_id") })
	private List<User> iscritti = new ArrayList<User>();

	@Column(name = "limite")
	private int limite;

	@Column(name = "description")
	private String descrizione;

	@JoinColumn(name = "docente")
	private String docente;

	public List<User> getIscritti() {
		// hibernate (probabilmente a causa di un join) duplica gli iscritti,
		// quest'operazione e' necessaria per rimuovere i duplicati
		Set<User> set = new HashSet<User>(iscritti);
		List<User> list = new ArrayList<User>(set);
		iscritti = list;
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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDocente() {
		return docente;
	}

	public void setDocente(String docente) {
		this.docente = docente;
	}
}
