package domain;

import java.util.Collection;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = { @Index(columnList = "name"), @Index(columnList = "surname") })
public class Trainer extends Actor {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------

	private String picture;

	@URL
	@Valid
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}

	// Relationships ----------------------------------------------------------
	private Curriculum curriculum;
	private Collection<Activity> activities;
	private Collection<ServiceEntity> services;
	
	@Valid
	@OneToOne(optional = true)
	public Curriculum getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy="trainer")
	public Collection<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Collection<Activity> activities) {
		this.activities = activities;
	}
	
	public void addActivity(Activity activity) {
		this.activities.add(activity);
	}

	public void removeActivity(Activity activity) {
		this.activities.remove(activity);
	}
	
	@Valid
	@NotNull
	@ManyToMany(mappedBy = "trainers")
	public Collection<ServiceEntity> getServices() {
		return services;
	}
	public void setServices(Collection<ServiceEntity> service) {
		this.services = service;
	}

}
