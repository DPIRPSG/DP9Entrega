package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityService;
import services.ServiceService;

import controllers.AbstractController;
import domain.Activity;
import domain.Gym;
import domain.Room;
import domain.ServiceEntity;
import domain.Trainer;

@Controller
@RequestMapping(value = "/activity/administrator")
public class ActivityAdministratorController extends AbstractController{

	// Services ----------------------------------------------------------
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private ServiceService serviceService;
	
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
	
	// Edition ----------------------------------------------------------
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView result;
		Activity activity;
		
		activity = activityService.createWithoutGym();
		
		result = createEditModelAndViewCreate(activity);
		
		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView saveOfCreate(Activity activity, BindingResult binding){
		ModelAndView result;
		
		if(binding.hasErrors()){
			result = createEditModelAndView(activity);
		}else{
			try{
				activity.setTitle(activity.getService().getName());
				activity.setPictures(activity.getService().getPictures());
				
				result = createEditModelAndView(activity);
			}catch(Throwable oops){
				result = createEditModelAndView(activity, "activity.commit.error");
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int activityId) {
		ModelAndView result;
		Activity activity;
		
		activity = activityService.findOne(activityId);
		
		result = createEditModelAndView(activity);

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Activity activity, BindingResult binding){
		ModelAndView result;
		
		if(binding.hasErrors()){
			result = createEditModelAndView(activity);
		}else{
			try{
				activityService.saveToEdit(activity);
				result = new ModelAndView("redirect:list.do");
			}catch(Throwable oops){
				result = createEditModelAndView(activity, "booking.commit.error");
			}
		}
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
		Collection<Room> rooms;
		Collection<Trainer> trainers;
		
		rooms = new ArrayList<Room>();
		
		for(Gym gym : activity.getService().getGyms()) {
			rooms.addAll(gym.getRooms());
		}
		trainers = activity.getService().getTrainers();

		result = new ModelAndView("activity/edit");
		result.addObject("activity", activity);
		result.addObject("message", message);
		result.addObject("rooms", rooms);
		result.addObject("trainers", trainers);

		return result;
	}
	
	protected ModelAndView createEditModelAndViewCreate(Activity activity) {
		ModelAndView result;

		result = createEditModelAndViewCreate(activity, null);

		return result;
	}
	
	protected ModelAndView createEditModelAndViewCreate(Activity activity, String message) {
		ModelAndView result;
		Collection<ServiceEntity> services;
		
		services = serviceService.findAll();

		result = new ModelAndView("activity/create");
		result.addObject("activity", activity);
		result.addObject("message", message);

		result.addObject("services", services);

		return result;
	}
	
}