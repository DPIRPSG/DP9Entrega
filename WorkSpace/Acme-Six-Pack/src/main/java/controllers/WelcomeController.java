/* WelcomeController.java
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityService;
import services.ActorService;
import services.ExchangeRateService;
import domain.Activity;
import domain.ExchangeRate;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	// Services ----------------------------------------------------------
	
	@Autowired
	private ActorService actorService;

	@Autowired
	private ExchangeRateController exchangeRateController;
	
	@Autowired
	private ExchangeRateService exchangeRateService;
	
	@Autowired
	private ActivityService activityService;
	
	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------

	@RequestMapping(value = "/index")
	public ModelAndView index(
			@RequestParam(required = false, defaultValue = "") String messageStatus
			,@CookieValue(value = "createCreditCard", required = false, defaultValue = "false") String createCreditCard
			,@CookieValue(value = "createSocialIdentity", required = false, defaultValue = "false") String createSocialIdentity
			,@CookieValue(value = "exchangeRate_id", required = false, defaultValue = "null") String exchangeRate_id
			,@CookieValue(value = "exchangeRate_all", required = false, defaultValue = "null") String exchangeRate_all
			, HttpServletResponse response
			) {
		ModelAndView result;
		SimpleDateFormat formatter;
		String moment;
		Collection<Activity> activities;
		Activity activity;
		int actorId;
		
		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());
		if(actorService.checkAuthority("CUSTOMER")){
			try{
				activities = activityService.findAllPaidAndNotBookedByCustomerId();
				if(!activities.isEmpty()){
					Random rnd = new Random();
					int i = rnd.nextInt(activities.size());
					activity = (Activity) activities.toArray()[i];
				}else{
					activities = null;
					activity = null;
				}
			}catch(org.springframework.dao.DataIntegrityViolationException oops){
				activities = null;
				activity = null;
			}
		}else{
			activities = null;
			activity = null;
		}
		

		result = new ModelAndView("welcome/index");
		result.addObject("moment", moment);
		result.addObject("activity", activity);
		
		if(messageStatus != ""){
			result.addObject("messageStatus", messageStatus);
		}
		
		try{
			actorId = actorService.findByPrincipal().getId();
		}catch (Exception e) {
			actorId = 0;
		}
		
		if(createCreditCard.equals(String.valueOf(actorId) + "true") && actorService.checkAuthority("CUSTOMER")){
			result = new ModelAndView("redirect:/creditCard/customer/edit.do");
		}else if(createSocialIdentity.equals(String.valueOf(actorId) + "true") && actorService.checkAuthority("CUSTOMER")){
			result = new ModelAndView("redirect:/socialIdentity/customer/edit.do");			
		}
		
		if(exchangeRate_all.equals("null")||exchangeRate_id.equals("null")){
			ExchangeRate exRate;
			
			exRate = exchangeRateService.findByCurrency("EUR");
			
			exchangeRateController.loadCookies(exRate, response);
		}

		return result;
	}
}