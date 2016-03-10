package controllers.customer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityService;

import controllers.AbstractController;
import domain.Activity;

@Controller
@RequestMapping(value = "/activity/customer")
public class ActivityCustomerController extends AbstractController{

	// Services ----------------------------------------------------------
	@Autowired
	private ActivityService activityService;
	
	// Constructors ----------------------------------------------------------
	public ActivityCustomerController(){
		super();
	}
	
	// Listing ----------------------------------------------------------
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(){
		
		ModelAndView result;
		Collection<Activity> activities;
		
		activities = activityService.findAllByCustomer();
		
		result = new ModelAndView("activity/list");
		result.addObject("requestURI", "activity/customer/list.do");
		result.addObject("activities", activities);
		
		return result;
	}
	
}
