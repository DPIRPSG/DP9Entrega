package controllers.trainer;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ServiceService;
import services.TrainerService;
import controllers.AbstractController;
import domain.Activity;
import domain.ServiceEntity;
import domain.Trainer;

@Controller
@RequestMapping("/service/trainer")
public class ServiceTrainerController extends AbstractController {
	
	// Services ----------------------------------------------------------
	@Autowired
	private TrainerService trainerService;
	
	@Autowired
	private ServiceService serviceService;
	
	// Constructors --------------------------------------------------------
	
	public ServiceTrainerController() {
		super();
	}
	
	// Listing --------------------------------------------------------------
	
	@RequestMapping(value = "/specialised", method = RequestMethod.GET)
	public ModelAndView specialised() {
		ModelAndView result;
		Trainer actTrainer;
		Collection<ServiceEntity> services;
		Collection<ArrayList<Integer>> customers;
		Collection<Activity> activities;
		Collection<Integer> servicesId;
		boolean sinActivity;
		
		servicesId = new ArrayList<Integer>();
				
		actTrainer = trainerService.findByPrincipal();
		
		activities = actTrainer.getActivities();
		for(Activity activity : activities) {
			if(!servicesId.contains(activity.getService().getId())) {
				servicesId.add(activity.getService().getId());
			}
		}
		
		result = new ModelAndView("service/list/specialised");
		result.addObject("requestURI", "service/trainer/list.do");

		services = actTrainer.getServices();
		
		if(actTrainer.getActivities().isEmpty()) {
			sinActivity = true;
		} else {
			sinActivity = false;
		}
		
		customers = serviceService.numbersOfCustomersByService(services);
		
		result.addObject("services", services);
		result.addObject("customers", customers);
		result.addObject("addService", false);
		result.addObject("servicesId", servicesId);
		result.addObject("sinActivity",sinActivity);

		return result;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Trainer actTrainer;
		Collection<ServiceEntity> services;
		Collection<ArrayList<Integer>> customers;
				
		actTrainer = trainerService.findByPrincipal();
		
		result = new ModelAndView("service/list");
		result.addObject("requestURI", "service/trainer/list.do");

		services = serviceService.findAll();
		services.removeAll(actTrainer.getServices());
		
		customers = serviceService.numbersOfCustomersByService(services);
		
		result.addObject("services", services);
		result.addObject("customers", customers);
		result.addObject("addService", true);

		return result;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(@RequestParam(required=true) Integer serviceId) {
		ModelAndView result;
		ServiceEntity serv;
				
		serv = serviceService.findOne(serviceId);
		trainerService.addService(serv);

		result = new ModelAndView("redirect:specialised.do");
				
		return result;
	} 
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView remove(@RequestParam(required=true) Integer serviceId) {
		ModelAndView result;
		ServiceEntity serv;
				
		serv = serviceService.findOne(serviceId);
		trainerService.removeService(serv);

		result = new ModelAndView("redirect:specialised.do");
				
		return result;
	}
		
	// Ancillary methods ---------------------------------------------------
	
}
