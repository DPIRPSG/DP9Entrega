package services.form;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import services.ActorService;
import services.FeePaymentService;
import services.InvoiceService;
import domain.Actor;
import domain.FeePayment;
import domain.Invoice;
import domain.form.InvoiceForm;

@Service
@Transactional
public class InvoiceFormService {

	// Managed repository -----------------------------------------------------

	
	// Supporting services ----------------------------------------------------

	@Autowired
	private InvoiceService invoiceService;	
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private FeePaymentService feePaymentService;
	
	// Constructors -----------------------------------------------------------
	
	public InvoiceFormService(){
		super();
	}

	// Methods -----------------------------------------------------------

	public InvoiceForm create() {
		Assert.isTrue(actorService.checkAuthority("CUSTOMER"), "Only a customer can manage an invoice.");
		InvoiceForm result;
		String invoiceesName;
		Actor customer;
		Integer customerId;
		String description;
		Integer numberOfFeePayments;
		Collection<FeePayment> feePaymentsNotIssued;
		
		result = new InvoiceForm();
		customer = actorService.findByPrincipal();
		customerId = customer.getId();
		invoiceesName = customer.getName();
		feePaymentsNotIssued = feePaymentService.findAllByCustomerIdNotIssued(customerId);
		numberOfFeePayments = feePaymentsNotIssued.size();
		description = "This invoice summarizes the total of " + numberOfFeePayments + " pays, that you (" + customer.getName() + " " + customer.getSurname() + ") have made to Acme-Six-Pack Co., Inc.";
		
		result.setInvoiceesName(invoiceesName);
		result.setDescription(description);
		
		return result;
	}



	public Invoice reconstruct(InvoiceForm invoiceForm) {
		Assert.isTrue(actorService.checkAuthority("CUSTOMER"), "Only a customer can manage an invoice.");
		Assert.notNull(invoiceForm);
		
		Invoice result;
		
		result = invoiceService.create();
		result.setInvoiceesName(invoiceForm.getInvoiceesName());
		result.setVAT(invoiceForm.getVAT());
		result.setDescription(invoiceForm.getDescription());
		result.setFeePayments(invoiceForm.getFeePaymentsNotIssued());
		
		return result;
	}
	
	public InvoiceForm deconstruct(Invoice invoice) {
		Assert.isTrue(actorService.checkAuthority("CUSTOMER"), "Only a customer can manage an invoice.");
		Assert.notNull(invoice);
		
		InvoiceForm result;
		
		result = this.create();
		result.setInvoiceesName(invoice.getInvoiceesName());
		result.setVAT(invoice.getVAT());
		result.setDescription(invoice.getDescription());
		
		return result;
	}
	
}
