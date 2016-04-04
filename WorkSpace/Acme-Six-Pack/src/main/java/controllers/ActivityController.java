package controllers;

import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityService;
import controllers.AbstractController;
import domain.Activity;

@Controller
@RequestMapping(value = "/activity")
public class ActivityController extends AbstractController {

	// Services ----------------------------------------------------------

	@Autowired
	private ActivityService activityService;
	
	// Constructors ----------------------------------------------------------

	public ActivityController() {
		super();
	}

	// Listing ----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int gymId) {
		ModelAndView result;
		Collection<Activity> activities;

		activities = activityService.findAllActivesByGymId(gymId);
		
		result = new ModelAndView("activity/list");
		result.addObject("requestURI", "activity/list.do");
		result.addObject("activities", activities);
		result.addObject("hayGymId", true);

		return result;
	}
}
