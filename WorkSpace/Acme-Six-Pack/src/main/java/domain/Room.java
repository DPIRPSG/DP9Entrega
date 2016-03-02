package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Room extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String name;
	private String description;
	private int numberOfSeats;
	private Collection<String> pictures;

	@NotBlank
	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	@NotNull
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Min(1)
	@Valid
	public int getNumberOfSeats() {
		return numberOfSeats;
	}

	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}

	@ElementCollection
	@Valid
	public Collection<String> getPictures() {
		return pictures;
	}

	public void setPictures(Collection<String> pictures) {
		this.pictures = pictures;
	}



	// Relationships ----------------------------------------------------------
	private Gym gym;
	
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Gym getGym() {
		return gym;
	}

	public void setGym(Gym gym) {
		this.gym = gym;
	}
}
