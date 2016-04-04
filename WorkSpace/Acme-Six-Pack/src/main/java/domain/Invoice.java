package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Invoice extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String invoiceesName;
	private String VAT;
	private Date creationMoment;
	private String description;
	private double totalCost;

	@NotBlank
	@NotNull
	public String getInvoiceesName() {
		return invoiceesName;
	}

	public void setInvoiceesName(String invoiceesName) {
		this.invoiceesName = invoiceesName;
	}	

	@Valid
	@NotBlank
	@NotNull
	public String getVAT() {
		return VAT;
	}

	public void setVAT(String vAT) {
		VAT = vAT;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/dd/MM HH:mm")
	public Date getCreationMoment() {
		return creationMoment;
	}

	public void setCreationMoment(Date createdMoment) {
		this.creationMoment = createdMoment;
	}

	@Valid
	@Min(0)
	@Digits(integer=9, fraction=2)
	public double getTotalCost() {
		return totalCost;
	}
	
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	@NotBlank
	@NotNull
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// Relationships ----------------------------------------------------------
	private Collection<FeePayment> feePayments;
	
	@Valid
	@NotNull
	@NotEmpty
	@OneToMany(mappedBy = "invoice")
	public Collection<FeePayment> getFeePayments() {
		return feePayments;
	}
	public void setFeePayments(Collection<FeePayment> feePayments) {
		this.feePayments = feePayments;
	}
}
