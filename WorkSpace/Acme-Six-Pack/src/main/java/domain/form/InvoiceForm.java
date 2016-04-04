package domain.form;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import domain.FeePayment;

public class InvoiceForm {
	private String invoiceesName;
	private String VAT;
	private String description;
	private Collection<FeePayment> feePaymentsNotIssued;
	
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
	
	@NotBlank
	@NotNull
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Valid
	@NotNull
	public Collection<FeePayment> getFeePaymentsNotIssued() {
		return feePaymentsNotIssued;
	}
	public void setFeePaymentsNotIssued(Collection<FeePayment> feePaymentsNotIssued) {
		this.feePaymentsNotIssued = feePaymentsNotIssued;
	}
}
