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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ExchangeRateService;

import domain.ExchangeRate;

@Controller
@RequestMapping("/exchangeRate")
public class ExchangeRateController extends AbstractController {
	
	@Autowired
	ExchangeRateService exchangeRateService;

	// Constructors -----------------------------------------------------------
	
	public ExchangeRateController() {
		super();
	}
		
	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/edit")
	public ModelAndView edit(@RequestParam(required=false, defaultValue="", value="requestURI") String requestURI
			, @RequestParam(required=true, value="exchangeRateId") String exchangeRateId
			, HttpServletResponse response) {

		ModelAndView result;
		Cookie cook1;
		Cookie cook2;
		Cookie cook3;
		Cookie cook4;		
		Cookie cook5;
		ExchangeRate exchangeRate;
		String res;
		
		res = "";
		exchangeRate = exchangeRateService.findOne(Integer.valueOf(exchangeRateId));
		
		for(ExchangeRate ja:exchangeRateService.findAll()){
			res += String.valueOf(ja.getId()) + ", " + String.valueOf(ja.getRate()) +
					", " + ja.getCurrency() + ", " + ja.getName() + "|";
		}
		
		cook1 = new Cookie("exchangeRate_all", res);
		cook2 = new Cookie("exchangeRate_id", exchangeRateId);
		cook3 = new Cookie("exchangeRate_rate", String.valueOf(exchangeRate.getRate()));
		cook4 = new Cookie("exchangeRate_currency", exchangeRate.getCurrency());		
		cook5 = new Cookie("exchangeRate_name", exchangeRate.getName());		
		
		cook1.setPath("/");
		cook2.setPath("/");
		cook3.setPath("/");
		cook4.setPath("/");
		cook5.setPath("/");
		
		response.addCookie(cook1);
		response.addCookie(cook2);
		response.addCookie(cook3);
		response.addCookie(cook4);
		response.addCookie(cook5);
		
		result = new ModelAndView("redirect:../" + requestURI);

		return result;
	}
}