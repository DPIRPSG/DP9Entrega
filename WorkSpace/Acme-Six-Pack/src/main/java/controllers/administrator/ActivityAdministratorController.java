<<<<<<< HEAD
package controllers.administrator;

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
import services.RoomService;
import services.ServiceService;
import services.TrainerService;

import controllers.AbstractController;
import domain.Activity;
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
	private TrainerService trainerService;
	
	@Autowired
	private RoomService roomService;
	
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
	public ModelAndView create(@RequestParam int gymId, @RequestParam int serviceId){
		ModelAndView result;
		Activity activity;
		
		Collection<Trainer> trainers;
		Collection<Room> rooms;
		Collection<ServiceEntity> services;
		
		activity = activityService.createWithGym(gymId, serviceId);
		trainers = trainerService.findAllByServiceId(serviceId);
		rooms = roomService.findAllByGymId(gymId);
		services = serviceService.findAllByGym(gymId);
		
		result = createEditModelAndView2(activity);
		result.addObject("trainers", trainers);
		result.addObject("rooms", rooms);
		result.addObject("services", services);
		
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int activityId) {
		ModelAndView result;
		Activity activity;
		
		Collection<Trainer> trainers;	
		Collection<Room> rooms;
		Collection<ServiceEntity> services;

		activity = activityService.findOne(activityId);
		trainers = trainerService.findAll();
		rooms = roomService.findAllByGymId(activity.getRoom().getGym().getId());
		services = serviceService.findAllByGym(activity.getRoom().getGym().getId());
		
		result = createEditModelAndView2(activity);
		result.addObject("trainers", trainers);
		result.addObject("rooms", rooms);
		result.addObject("services", services);

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Activity activity, BindingResult binding){
		ModelAndView result;
		
		Collection<Trainer> trainers;	
		Collection<Room> rooms;
		Collection<ServiceEntity> services;
		
		trainers = trainerService.findAll();
		rooms = roomService.findAllByGymId(activity.getRoom().getGym().getId());
		services = serviceService.findAllByGym(activity.getRoom().getGym().getId());
		
		if(binding.hasErrors()){
			result = createEditModelAndView2(activity);
		}else{
			try{
				activityService.saveToEdit(activity);
				result = new ModelAndView("redirect:list.do");
			}catch(Throwable oops){
				result = createEditModelAndView2(activity, "activity.commit.error");
				result.addObject("trainers", trainers);
				result.addObject("rooms", rooms);
				result.addObject("services", services);
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

		result = new ModelAndView("activity/list");
		result.addObject("activity", activity);
		result.addObject("message", message);

		return result;
	}
	
	protected ModelAndView createEditModelAndView2(Activity activity) {
		ModelAndView result;

		result = createEditModelAndView2(activity, null);

		return result;
	}
	
	protected ModelAndView createEditModelAndView2(Activity activity, String message) {
		ModelAndView result;

		result = new ModelAndView("activity/edit");
		result.addObject("activity", activity);
		result.addObject("message", message);

		return result;
	}
	
}