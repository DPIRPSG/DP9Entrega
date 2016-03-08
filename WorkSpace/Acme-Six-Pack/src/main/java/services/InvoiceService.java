package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.InvoiceRepository;
import domain.Actor;
import domain.FeePayment;
import domain.Invoice;

@Service
@Transactional
public class InvoiceService {
	
	// Managed repository -----------------------------------------------------

	@Autowired
	private InvoiceRepository invoiceRepository;
	
	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private FeePaymentService feePaymentService;

	// Constructors -----------------------------------------------------------

	public InvoiceService() {
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------
	
	public Invoice create(){
		Assert.isTrue(actorService.checkAuthority("CUSTOMER"), "Only a customer can manage an invoice.");
		
		Invoice result;
		String invoiceesName;
		Actor customer;
		String description;
		Integer numberOfFeePayments;
		Collection<FeePayment> allHisPayments;
		
		result = new Invoice();
		customer = actorService.findByPrincipal();
		invoiceesName = customer.getName();
		allHisPayments = feePaymentService.findAllByCustomer();
		numberOfFeePayments = allHisPayments.size();
		description = "This invoice summarizes the total of " + numberOfFeePayments + " pays, that you (" + customer.getName() + customer.getSurname() + ") have made to Acme-Six-Pack Co., Inc.";
		
		result.setInvoiceesName(invoiceesName);
		result.setDescription(description);
		result.setCreationMoment(new Date()); // Se crea una fecha en este momento porque no puede ser null, pero la fecha real se fijará en el método "save"
		
		return result;
	}
	
	public void save(Invoice invoice){
		Assert.isTrue(actorService.checkAuthority("CUSTOMER"), "Only a customer can manage an invoice.");
		Assert.notNull(invoice);
		
		invoice.setCreationMoment(new Date());
		
		invoiceRepository.save(invoice);
	}
	
	public Invoice findOne(int invoiceId) {
		Assert.isTrue(actorService.checkAuthority("CUSTOMER"), "Only a customer can manage an invoice.");
		
		Invoice result;
		
		result = invoiceRepository.findOne(invoiceId);
		
		return result;
	}
	
	// Other business methods -------------------------------------------------
	
	public Collection<Invoice> findAllByCustomerId(int customerId) {
		Assert.isTrue(actorService.checkAuthority("CUSTOMER"), "Only a customer can manage an invoice.");
		
		Collection<Invoice> result;
		
		result = invoiceRepository.findAllByCustomerId(customerId);
		
		return result;
	}
	
}
