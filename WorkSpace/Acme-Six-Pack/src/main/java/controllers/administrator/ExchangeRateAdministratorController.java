/* WelcomeController.java
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import services.ExchangeRateService;

import domain.ExchangeRate;

@Controller
@RequestMapping("/exchangeRate/administrator")
public class ExchangeRateAdministratorController extends AbstractController {
	
	@Autowired
	ExchangeRateService exchangeRateService;

	// Constructors -----------------------------------------------------------
	
	public ExchangeRateAdministratorController() {
		super();
	}
		
	// List ------------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<ExchangeRate> exchangesRates;

		exchangesRates = exchangeRateService.findAll();
		
		result = new ModelAndView("exchangeRate/list");
		result.addObject("exchangesRates", exchangesRates);
		result.addObject("requestURI", "exchangeRate/administrator/list.do");

		return result;
	}
	
	// Create & Edit -------------------
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		ExchangeRate exchangeRate;

		exchangeRate = exchangeRateService.create();
		result = createEditModelAndView(exchangeRate);

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int exchangeRateId) {

		ModelAndView result;
		ExchangeRate exchangeRate;

		exchangeRate = exchangeRateService.findOne(exchangeRateId);
		result = createEditModelAndView(exchangeRate);

		return result;
	}
	
	

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid ExchangeRate exchangeRate, BindingResult binding) {

		ModelAndView result;
		
		if (binding.hasErrors()) {
			result = createEditModelAndView(exchangeRate);
		} else {
			try {
				exchangeRateService.save(exchangeRate);
				result = new ModelAndView("redirect:/exchangeRate/administrator/list.do");
			} catch (Throwable oops) {
				String errorCode;
				errorCode = "exchangeRate.commit.error";
				result = createEditModelAndView(exchangeRate, errorCode);
			}
		}

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid ExchangeRate exchangeRate, BindingResult binding) {
		ModelAndView result;
		
		try {
			exchangeRateService.delete(exchangeRate);
			result = new ModelAndView("redirect:/exchangeRate/administrator/list.do");
		} catch (Throwable oops) {
			String errorCode;
			errorCode = "exchangeRate.commit.error";
			result = createEditModelAndView(exchangeRate, errorCode);
		}
		return result;
	}
	
	// Other methods -----------------------
	
	private ModelAndView createEditModelAndView(ExchangeRate exchangeRate) {
		ModelAndView result;
		
		result = createEditModelAndView(exchangeRate, null);
		
		return result;
	}

	private ModelAndView createEditModelAndView(ExchangeRate exchangeRate,
			String message) {
		ModelAndView result;
		
		result = new ModelAndView("exchangeRate/edit");
		result.addObject("exchangeRate", exchangeRate);
		result.addObject("message", message);
		result.addObject("urlAction", "exchangeRate/administrator/edit.do");
		
		return result;
	}
	
}