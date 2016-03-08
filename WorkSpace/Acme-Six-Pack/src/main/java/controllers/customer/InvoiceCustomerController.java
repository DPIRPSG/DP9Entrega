package controllers.customer;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.InvoiceService;
import controllers.AbstractController;
import domain.Actor;
import domain.Invoice;

@Controller
@RequestMapping("/invoice/customer")
public class InvoiceCustomerController extends AbstractController {
	
	// Services ----------------------------------------------------------
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private ActorService actorService;
	
	
	// Constructors --------------------------------------------------------
	
	public InvoiceCustomerController() {
		super();
	}
	
	
	// Creating --------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam Integer customerId) {
		ModelAndView result;
		Collection<Invoice> invoices;
		
		invoices = invoiceService.findAllByCustomerId(customerId);
		
		result = new ModelAndView("invoice/list");
		result.addObject("invoices", invoices);
		result.addObject("requestURI", "invoice/customer/list.do");
		
		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Invoice invoice;
		
		invoice = invoiceService.create();
		
		result = createEditModelAndView(invoice);
		
		return result;
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST, params="save")
	public ModelAndView create(@Valid Invoice invoice, BindingResult binding) {
		ModelAndView result;
		
		if (binding.hasErrors()) {
			result = createEditModelAndView(invoice);
		} else {
			try {
				result = new ModelAndView("invoice/draft");
				result.addObject("invoice", invoice);
			} catch (Throwable oops) {
				result = createEditModelAndView(invoice, "comment.commit.error");
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
		
		if (binding.hasErrors()) {
			result = createEditModelAndView(invoice);
		} else {
			try {
				customer = actorService.findByPrincipal();
				customerId = customer.getId();
				invoiceService.save(invoice);
				
				result = new ModelAndView("redirect:../list.do?customerId=" + customerId);
			} catch (Throwable oops) {
				result = createEditModelAndView(invoice, "comment.commit.error");
			}
		}
		
		return result;
	}
	
	// Ancillary methods ---------------------------------------------------
	
	protected ModelAndView createEditModelAndView(Invoice invoice) {
		ModelAndView result;
		
		result = createEditModelAndView(invoice, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(Invoice invoice, String message) {
		ModelAndView result;
		
		result = new ModelAndView("invoice/create");
		result.addObject("invoice", invoice);
		result.addObject("message", message);
		
		return result;
	}
}
