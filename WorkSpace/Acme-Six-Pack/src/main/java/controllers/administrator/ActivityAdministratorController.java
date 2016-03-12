package controllers.administrator;

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
@RequestMapping(value = "/activity/administrator")
public class ActivityAdministratorController extends AbstractController{

	// Services ----------------------------------------------------------
	@Autowired
	private ActivityService activityService;
	
	// Constructors ----------------------------------------------------------
	public ActivityAdministratorController(){
		super();
	}
	
	// Listing ----------------------------------------------------------
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(){
		
		ModelAndView result;
		Collection<Activity> activities;
		
		activities = activityService.findAll();
		
		result = new ModelAndView("activity/list");
		result.addObject("requestURI", "activity/administrator/list.do");
		result.addObject("activities", activities);
		
		return result;
	}
	
	@RequestMapping(value="/delete", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam int activityId){
		
		ModelAndView result;
		Activity activity;
		
		activity = activityService.findOne(activityId);
		Assert.notNull(activity);
		
		try{
			activityService.delete(activity);
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "activity.delete.ok");
		}catch(Throwable oops){
			result = new ModelAndView("redirect:list.do");
			result = createEditModelAndView(activity, "booking.delete.error");
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
