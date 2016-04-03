package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = { @Index(columnList = "statement"), @Index(columnList = "skills"), @Index(columnList = "likes"), @Index(columnList = "dislikes") })
public class Curriculum extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private Date updateMoment;
	private String picture;
	private String statement;
	private String skills;
	private String likes;
	private String dislikes;
	
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
	
	@NotBlank
	@NotNull
	public String getSkills() {
		return skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}
	
	@NotBlank
	@NotNull
	public String getLikes() {
		return likes;
	}
	public void setLikes(String likes) {
		this.likes = likes;
	}
	
	@NotBlank
	@NotNull
	public String getDislikes() {
		return dislikes;
	}
	public void setDislikes(String dislikes) {
		this.dislikes = dislikes;
	}

	// Relationships ----------------------------------------------------------
	
}
