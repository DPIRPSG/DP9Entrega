package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = { @Index(columnList = "term") })
public class SpamTerm extends DomainEntity{

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String term;

	@Column(unique=true)
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	
	// Relationships ----------------------------------------------------------
	
}
