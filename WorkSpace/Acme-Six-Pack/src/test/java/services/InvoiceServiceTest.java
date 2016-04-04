package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Customer;
import domain.FeePayment;
import domain.Invoice;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class InvoiceServiceTest extends AbstractTest {

	// Service under test -------------------------
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private CustomerService customerService;
	
	// Test ---------------------------------------
	
	/**
	 * Acme-Six-Pack - Level C - 5.1
	 * Customer can request an invoice to the system.
	 */
	
	/**
	 * Test que comprueba que se crea un Invoice sin problemas
	 */
	@Test
	public void testCreateInvoiceOk() {
		Invoice invoice;
		Collection<FeePayment> feePayments;
		Customer customer;
		double totalCost;
		
		totalCost = 0;
		
		authenticate("customer1");
		
		customer = customerService.findByPrincipal();
		feePayments = new ArrayList<FeePayment>();
		
		for(FeePayment fee : customer.getFeePayments()) {
			if(fee.getInvoice() == null) {
				feePayments.add(fee);
			}
		}
		
		invoice = invoiceService.create();
		invoice.setInvoiceesName("Prueba");
		invoice.setDescription("Prueba");
		invoice.setFeePayments(feePayments);
		invoice.setVAT("Prueba");
		invoice = invoiceService.save(invoice);
		
		Assert.isTrue(invoice.getDescription().equals("Prueba"));
		Assert.isTrue(invoice.getInvoiceesName().equals("Prueba"));
		Assert.isTrue(invoice.getVAT().equals("Prueba"));
		
		for(FeePayment fee : invoice.getFeePayments()) {
			totalCost = totalCost + fee.getAmount(); 
		}
		
		Assert.isTrue(invoice.getTotalCost() == totalCost);
		Assert.isTrue(invoice.getFeePayments().containsAll(feePayments));
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level C - 5.1
	 * Customer can request an invoice to the system.
	 */
	
	/**
	 * Test que comprueba que se crea un Invoice sin introducir un nombre sin problemas
	 */
	@Test
	public void testCreateInvoiceWithoutInvoiceesNameOk() {
		Invoice invoice;
		Collection<FeePayment> feePayments;
		Customer customer;
		double totalCost;
		
		totalCost = 0;
		
		authenticate("customer1");
		
		customer = customerService.findByPrincipal();
		feePayments = new ArrayList<FeePayment>();
		
		for(FeePayment fee : customer.getFeePayments()) {
			if(fee.getInvoice() == null) {
				feePayments.add(fee);
			}
		}
		
		invoice = invoiceService.create();
		//invoice.setInvoiceesName("Prueba");
		invoice.setDescription("Prueba");
		invoice.setFeePayments(feePayments);
		invoice.setVAT("Prueba");
		invoice = invoiceService.save(invoice);
		
		Assert.isTrue(invoice.getDescription().equals("Prueba"));
		Assert.isTrue(invoice.getInvoiceesName().equals(customer.getName()));
		Assert.isTrue(invoice.getVAT().equals("Prueba"));
		
		for(FeePayment fee : invoice.getFeePayments()) {
			totalCost = totalCost + fee.getAmount(); 
		}
		
		Assert.isTrue(invoice.getTotalCost() == totalCost);
		Assert.isTrue(invoice.getFeePayments().containsAll(feePayments));
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level C - 5.1
	 * Customer can request an invoice to the system.
	 */
	
	/**
	 * Test que comprueba que no falla al cambiar el totalCost de un Invoice
	 */
	@Test
	public void testCreateInvoiceError1() {
		Invoice invoice;
		Collection<FeePayment> feePayments;
		Customer customer;
		double totalCost;
		
		totalCost = 0;
		
		authenticate("customer1");
		
		customer = customerService.findByPrincipal();
		feePayments = new ArrayList<FeePayment>();
		
		for(FeePayment fee : customer.getFeePayments()) {
			if(fee.getInvoice() == null) {
				feePayments.add(fee);
			}
		}
		
		invoice = invoiceService.create();
		invoice.setInvoiceesName("Prueba");
		invoice.setDescription("Prueba");
		invoice.setFeePayments(feePayments);
		invoice.setVAT("Prueba");
		
		//Aunque se cambie el totalCost, en el save se pondrá el totalCost correcto
		invoice.setTotalCost(1);
		invoice = invoiceService.save(invoice);
		
		Assert.isTrue(invoice.getDescription().equals("Prueba"));
		Assert.isTrue(invoice.getInvoiceesName().equals("Prueba"));
		Assert.isTrue(invoice.getVAT().equals("Prueba"));
		
		for(FeePayment fee : invoice.getFeePayments()) {
			totalCost = totalCost + fee.getAmount(); 
		}
		
		Assert.isTrue(invoice.getTotalCost() != 1);
		Assert.isTrue(invoice.getTotalCost() == totalCost);
		Assert.isTrue(invoice.getFeePayments().containsAll(feePayments));
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level C - 5.1
	 * Customer can request an invoice to the system.
	 */
	
	/**
	 * Test que comprueba que al crear un Invoice sin descripción falla
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateInvoiceError2() {
		Invoice invoice;
		Collection<FeePayment> feePayments;
		Customer customer;
		double totalCost;
		
		totalCost = 0;
		
		authenticate("customer1");
		
		customer = customerService.findByPrincipal();
		feePayments = new ArrayList<FeePayment>();
		
		for(FeePayment fee : customer.getFeePayments()) {
			if(fee.getInvoice() == null) {
				feePayments.add(fee);
			}
		}
		
		invoice = invoiceService.create();
		invoice.setInvoiceesName("Prueba");
		//invoice.setDescription("Prueba");
		invoice.setFeePayments(feePayments);
		invoice.setVAT("Prueba");
		invoice = invoiceService.save(invoice);
		invoiceService.flush();
		
		//Assert.isTrue(invoice.getDescription().equals("Prueba"));
		Assert.isTrue(invoice.getInvoiceesName().equals("Prueba"));
		Assert.isTrue(invoice.getVAT().equals("Prueba"));
		
		for(FeePayment fee : invoice.getFeePayments()) {
			totalCost = totalCost + fee.getAmount(); 
		}
		
		Assert.isTrue(invoice.getTotalCost() == totalCost);
		Assert.isTrue(invoice.getFeePayments().containsAll(feePayments));
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level C - 5.1
	 * Customer can request an invoice to the system.
	 */
	
	/**
	 * Test que comprueba que al crear un Invoice sin VAT falla
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateInvoiceError3() {
		Invoice invoice;
		Collection<FeePayment> feePayments;
		Customer customer;
		double totalCost;
		
		totalCost = 0;
		
		authenticate("customer1");
		
		customer = customerService.findByPrincipal();
		feePayments = new ArrayList<FeePayment>();
		
		for(FeePayment fee : customer.getFeePayments()) {
			if(fee.getInvoice() == null) {
				feePayments.add(fee);
			}
		}
		
		invoice = invoiceService.create();
		invoice.setInvoiceesName("Prueba");
		invoice.setDescription("Prueba");
		invoice.setFeePayments(feePayments);
		//invoice.setVAT("Prueba");
		invoice = invoiceService.save(invoice);
		invoiceService.flush();
		
		Assert.isTrue(invoice.getDescription().equals("Prueba"));
		Assert.isTrue(invoice.getInvoiceesName().equals("Prueba"));
		//Assert.isTrue(invoice.getVAT().equals("Prueba"));
		
		for(FeePayment fee : invoice.getFeePayments()) {
			totalCost = totalCost + fee.getAmount(); 
		}
		
		Assert.isTrue(invoice.getTotalCost() == totalCost);
		Assert.isTrue(invoice.getFeePayments().containsAll(feePayments));
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level C - 5.1
	 * Customer can request an invoice to the system.
	 */
	
	/**
	 * Test que comprueba que al crear un Invoice sin FeePayments falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateInvoiceError4() {
		Invoice invoice;
		Collection<FeePayment> feePayments;
		Customer customer;
		double totalCost;
		
		totalCost = 0;
		
		authenticate("customer1");
		
		customer = customerService.findByPrincipal();
		feePayments = new ArrayList<FeePayment>();
		
		for(FeePayment fee : customer.getFeePayments()) {
			if(fee.getInvoice() == null) {
				feePayments.add(fee);
			}
		}
		
		invoice = invoiceService.create();
		invoice.setInvoiceesName("Prueba");
		invoice.setDescription("Prueba");
		//invoice.setFeePayments(feePayments);
		invoice.setVAT("Prueba");
		invoice = invoiceService.save(invoice);
		
		Assert.isTrue(invoice.getDescription().equals("Prueba"));
		Assert.isTrue(invoice.getInvoiceesName().equals("Prueba"));
		Assert.isTrue(invoice.getVAT().equals("Prueba"));
		
		for(FeePayment fee : invoice.getFeePayments()) {
			totalCost = totalCost + fee.getAmount(); 
		}
		
		Assert.isTrue(invoice.getTotalCost() == totalCost);
		//Assert.isTrue(invoice.getFeePayments().containsAll(feePayments));
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level C - 5.1
	 * Customer can request an invoice to the system.
	 */
	
	/**
	 * Test que comprueba que al crear un Invoice con FeePayments ya usados falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateInvoiceError5() {
		Invoice invoice;
		Collection<FeePayment> feePayments;
		Customer customer;
		double totalCost;
		
		totalCost = 0;
		
		authenticate("customer1");
		
		customer = customerService.findByPrincipal();
		feePayments = new ArrayList<FeePayment>();
		
		for(FeePayment fee : customer.getFeePayments()) {
			if(fee.getInvoice() != null) {
				feePayments.add(fee);
			}
		}
		
		invoice = invoiceService.create();
		invoice.setInvoiceesName("Prueba");
		invoice.setDescription("Prueba");
		invoice.setFeePayments(feePayments);
		invoice.setVAT("Prueba");
		invoice = invoiceService.save(invoice);
		invoiceService.flush();
		
		Assert.isTrue(invoice.getDescription().equals("Prueba"));
		Assert.isTrue(invoice.getInvoiceesName().equals("Prueba"));
		Assert.isTrue(invoice.getVAT().equals("Prueba"));
		
		for(FeePayment fee : invoice.getFeePayments()) {
			totalCost = totalCost + fee.getAmount(); 
		}
		
		Assert.isTrue(invoice.getTotalCost() == totalCost);
		Assert.isTrue(invoice.getFeePayments().containsAll(feePayments));
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level C - 5.2
	 * List his or her invoices.
	 */
	
	/**
	 * Test que comprueba que el listar Invoice de un customer funciona
	 */
	@Test
	public void testListInvoicess() {
		Customer customer;
		Collection<Invoice> invoicees;
		
		authenticate("customer1");
	
		customer = customerService.findByPrincipal();
		
		invoicees = invoiceService.findAllByCustomerId(customer.getId());
		
		Assert.isTrue(invoicees.size() == 2);
		
		authenticate(null);
	}
}
