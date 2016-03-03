package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Curriculum extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private Date updateMoment;
	private String picture;
	private String statement;
	private Collection<String> skills;
	private Collection<String> likes;
	private Collection<String> dislikes;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/dd/MM HH:mm")
	public Date getUpdateMoment() {
		return updateMoment;
	}

	public void setUpdateMoment(Date updateMoment) {
		this.updateMoment = updateMoment;
	}
	
	@URL
	@Valid
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	@NotBlank
	@NotNull
	public String getStatement() {
		return statement;
	}
	public void setStatement(String statement) {
		this.statement = statement;
	}
	
	@Valid
	@NotNull
	@NotEmpty
	public Collection<String> getSkills() {
		return skills;
	}
	public void setSkills(Collection<String> skills) {
		this.skills = skills;
	}
	
	@Valid
	@NotNull
	@NotEmpty
	public Collection<String> getLikes() {
		return likes;
	}
	public void setLikes(Collection<String> likes) {
		this.likes = likes;
	}
	
	@Valid
	@NotNull
	@NotEmpty
	public Collection<String> getDislikes() {
		return dislikes;
	}
	public void setDislikes(Collection<String> dislikes) {
		this.dislikes = dislikes;
	}

	// Relationships ----------------------------------------------------------
	
}
