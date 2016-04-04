package controllers;

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
import domain.ServiceEntity;
import domain.Trainer;

@Controller
@RequestMapping(value = "/trainer")
public class TrainerController extends AbstractController {

	// Services ----------------------------------------------------------

	@Autowired
	private TrainerService trainerService;
	
	@Autowired
	private ServiceService serviceService;

	// Constructors ----------------------------------------------------------

	public TrainerController() {
		super();
	}

	// Listing ----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required=false, defaultValue="") String keyword) {
		ModelAndView result;
		Collection<Trainer> trainers;
		String keywordToFind;
		
		trainers = trainerService.findAll();
		
		if (!keyword.equals("")) {
			String[] keywordComoArray = keyword.split(" ");
			for (int i = 0; i < keywordComoArray.length; i++) {
				if (!keywordComoArray[i].equals("")) {
					keywordToFind = keywordComoArray[i];
					trainers = trainerService.findBySingleKeyword(keywordToFind);
					break;
				}
			}
		}
				
		result = new ModelAndView("trainer/list");
		result.addObject("requestURI", "trainer/list.do");
		
		result.addObject("trainers", trainers);

		return result;
	}
	
	@RequestMapping(value = "/specialities", method = RequestMethod.GET)
	public ModelAndView specialised(@RequestParam(required=true) int trainerId) {
		ModelAndView result;
		Trainer actTrainer;
		Collection<ServiceEntity> services;
		Collection<ArrayList<Integer>> customers;
				
		actTrainer = trainerService.findOne(trainerId);
		
		result = new ModelAndView("service/list/specialised");
		result.addObject("requestURI", "trainer/specialised.do");

		services = actTrainer.getServices();
		
		customers = serviceService.numbersOfCustomersByService(services);
		
		result.addObject("services", services);
		result.addObject("customers", customers);
		/*try{
			int id;
			
			id = actorService.findByPrincipal().getId();
			
			if(id == trainerId){
				result.addObject("addService", false);				
			}else{
				result.addObject("addService", null);				
			}
		} catch (Exception e) {
			result.addObject("addService", null);
		}*/

		return result;
	}
}
