package controllers.customer;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.InvoiceService;
import services.form.InvoiceFormService;
import controllers.AbstractController;
import domain.Actor;
import domain.Invoice;
import domain.form.InvoiceForm;

@Controller
@RequestMapping("/invoice/customer")
public class InvoiceCustomerController extends AbstractController {
	
	// Services ----------------------------------------------------------
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private InvoiceFormService invoiceFormService;
	
	@Autowired
	private ActorService actorService;
	
	// Constructors --------------------------------------------------------
	
	public InvoiceCustomerController() {
		super();
	}
	
	
	// Creating --------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Invoice> invoices;
		Actor customer;
		Integer customerId;
		
		customer = actorService.findByPrincipal();
		customerId = customer.getId();
		invoices = invoiceService.findAllByCustomerId(customerId);
		
		
		result = new ModelAndView("invoice/list");
		result.addObject("invoices", invoices);
		result.addObject("requestURI", "invoice/customer/list.do");
		
		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@CookieValue(value = "language", required = false, defaultValue = "en") String languageCookie) {
		ModelAndView result;
		InvoiceForm invoiceForm;
		
		invoiceForm = invoiceFormService.create(languageCookie);
		
		result = createEditModelAndView(invoiceForm);
		
		return result;
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST, params="save")
	public ModelAndView create(@Valid InvoiceForm invoiceForm, BindingResult binding) {
		ModelAndView result;
		Invoice invoice;
		InvoiceForm invoiceFormErrors;
		
		invoiceFormErrors = invoiceFormService.create("en");
		invoiceFormErrors.setInvoiceesName(invoiceForm.getInvoiceesName());
		invoiceFormErrors.setVAT(invoiceForm.getVAT());
		invoiceFormErrors.setDescription(invoiceForm.getDescription());
		
		if (binding.hasErrors()) {
			result = createEditModelAndView(invoiceFormErrors);
		} else {
			try {
				invoice = invoiceFormService.reconstruct(invoiceForm);
				result = new ModelAndView("invoice/draft");
				result.addObject("invoice", invoice);
			} catch (Throwable oops) {
				result = createEditModelAndView(invoiceFormErrors, "invoice.commit.error");
			}
		}
		
		return result;
	}
	
	@RequestMapping(value = "/draft", method = RequestMethod.GET)
	public ModelAndView draft(@RequestParam @Valid Invoice invoice) {
		ModelAndView result;
		
		result = new ModelAndView("invoice/draft");
		result.addObject("invoice", invoice);
		
		return result;
	}
	
	@RequestMapping(value="/draft", method=RequestMethod.POST, params="save")
	public ModelAndView draft(@Valid Invoice invoice, BindingResult binding) {
		ModelAndView result;
		Actor customer;
		Integer customerId;
		InvoiceForm invoiceForm;
		
		customer = actorService.findByPrincipal();
		customerId = customer.getId();
		
		if (binding.hasErrors()) {
			invoiceForm = invoiceFormService.deconstruct(invoice);
			result = createEditModelAndView(invoiceForm);
		} else {
			try {
				invoiceService.save(invoice);
				
				result = new ModelAndView("redirect:../list.do?customerId=" + customerId);
			} catch (Throwable oops) {
				invoiceForm = invoiceFormService.deconstruct(invoice);
				result = createEditModelAndView(invoiceForm, "invoice.commit.error");
			}
		}
		
		return result;
	}
	
	@RequestMapping(value="/draft", method=RequestMethod.POST, params="cancel")
	public ModelAndView draftCancel(@Valid Invoice invoice, BindingResult binding) {
		ModelAndView result;
		InvoiceForm invoiceForm;
		
		if (binding.hasErrors()) {
			invoiceForm = invoiceFormService.deconstruct(invoice);
			result = createEditModelAndView(invoiceForm);
		} else {
			try {
				invoiceForm = invoiceFormService.deconstruct(invoice);
				result = createEditModelAndView(invoiceForm);
			} catch (Throwable oops) {
				invoiceForm = invoiceFormService.deconstruct(invoice);
				result = createEditModelAndView(invoiceForm, "invoice.commit.error");
			}
		}
		
		return result;
	}
	
	// Ancillary methods ---------------------------------------------------
	
	protected ModelAndView createEditModelAndView(InvoiceForm invoiceForm) {
		ModelAndView result;
		
		result = createEditModelAndView(invoiceForm, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(InvoiceForm invoiceForm, String message) {
		ModelAndView result;
		
		result = new ModelAndView("invoice/create");
		result.addObject("invoiceForm", invoiceForm);
		result.addObject("message", message);
		
		return result;
	}
}
