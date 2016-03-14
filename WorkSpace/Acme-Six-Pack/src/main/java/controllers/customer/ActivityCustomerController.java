package controllers.customer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@RequestMapping(value="book", method = RequestMethod.GET)
	public ModelAndView book(@RequestParam int activityId){
		
		ModelAndView result;
		Activity activity;
		
		activity = activityService.findOne(activityId);
		Assert.notNull(activity);
		
		try{
			activityService.book(activity);
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "activity.book.ok");
		}catch(Throwable oops){
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "activity.commit.error");
		}
		
		
		return result;
	}
	
	@RequestMapping(value="/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam int activityId){
		
		ModelAndView result;
		Activity activity;
		
		activity = activityService.findOne(activityId);
		Assert.notNull(activity);
		
		try{
			activityService.cancel(activity);
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "activity.cancel.ok");
		}catch(Throwable oops){
			result = new ModelAndView("redirect:list.do");
			result = createEditModelAndView(activity, "booking.cancel.error");
		}
		
		return result;
	}
	
	// Ancillary Methods
	// ----------------------------------------------------------
	
	protected ModelAndView createEditModelAndView(Activity activity) {
		ModelAndView result;

		result = createEditModelAndView(activity, null);

		return result;
	}
	
	protected ModelAndView createEditModelAndView(Activity activity, String message) {
		ModelAndView result;

		result = new ModelAndView("activity/list");
		result.addObject("activity", activity);
		result.addObject("message", message);

		return result;
	}
	
}
