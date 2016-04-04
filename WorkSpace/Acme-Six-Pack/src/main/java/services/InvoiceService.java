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
		
		invoiceesName = actorService.findByPrincipal().getName();
		
		result = new Invoice();
		
		result.setInvoiceesName(invoiceesName);
		result.setCreationMoment(new Date()); // Se crea una fecha en este momento porque no puede ser null, pero la fecha real se fijará en el método "save"
		
		return result;
	}
	
	public Invoice save(Invoice invoice){
		Assert.isTrue(actorService.checkAuthority("CUSTOMER"), "Only a customer can manage an invoice.");
		Assert.notNull(invoice);
		Assert.notEmpty(invoice.getFeePayments());
		
		for(FeePayment fee : invoice.getFeePayments()) {
			Assert.isTrue(fee.getInvoice() == null);
		}
		
		Collection<FeePayment> feePayments;
		double totalCost;
		
		totalCost = 0.0;
		
		feePayments = invoice.getFeePayments();
		for(FeePayment f: feePayments){
			totalCost += f.getAmount();
		}
		
		invoice.setTotalCost(totalCost);
		invoice.setCreationMoment(new Date());
		
		invoice = invoiceRepository.save(invoice);
		
		for(FeePayment feePayment : invoice.getFeePayments()) {
			feePayment.setInvoice(invoice);
			feePaymentService.save(feePayment);
		}
		
		return invoice;
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
	
	public void checkCustomer(int customerId) {
		Actor customerPrincipal;
		Actor customerReal;
		
		customerPrincipal = actorService.findByPrincipal();
		customerReal = actorService.findOne(customerId);
		
		Assert.isTrue(customerPrincipal == customerReal, "You can't manage an Invoice of other Customer.");
	}
	
	public void flush() {
		invoiceRepository.flush();
	}
	
}
