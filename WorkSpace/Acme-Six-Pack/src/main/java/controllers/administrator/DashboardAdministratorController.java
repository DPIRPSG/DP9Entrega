package controllers.administrator;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityService;
import services.ActorService;
import services.CustomerService;
import services.GymService;
import services.ServiceService;
import services.TrainerService;
import controllers.AbstractController;
import domain.Activity;
import domain.Actor;
import domain.Customer;
import domain.Gym;
import domain.ServiceEntity;

@Controller
@RequestMapping("/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {
	
	// Services ----------------------------------------------------------

	@Autowired
	private GymService gymService;
	
	@Autowired
	private ServiceService serviceService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private TrainerService trainerService;
	
	// Constructors --------------------------------------------------------
	
	public DashboardAdministratorController() {
		super();
	}
	
	
	// Listing ------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		
		// Level C 1.0
		Collection<Gym> mostPopularGyms;
		Collection<Gym> leastPopularGyms;
		Collection<ServiceEntity> mostPopularService;
		Collection<ServiceEntity> leastPopularService;
		Collection<Customer> paidMoreFees;
		Collection<Customer> paidLessFees;
		
		// Level C 2.0
		Double averageRoomsPerGym;
		Double standardDeviationRoomsPerGym;
		Collection<Gym> gymsWithMoreRoomsThanAverage;
		Collection<Gym> gymsWithLessRoomsThanAverage;
		Collection<Customer> moreInvoicesIssuedCustomer;
		Collection<Customer> noRequestedInvoicesCustomer;
		
		// Level B 1.0
		Collection<Actor> sendMoreSpam;
		Double averageNumberOfMessages;
		
		// Level B 2.0
		Collection<Activity> activitiesByPopularity;
		Map<String, Double> averageNumberOfActivitiesPerGymByService;
		Double averageNumberOfServiceWithSpecialisedTrainer;
		Collection<ServiceEntity> mostPopularServiceByNumberOfTrainer;
		
		// Level A 1.0
		Collection<Gym> moreCommentedGyms;
		Collection<ServiceEntity> moreCommentedServices;
		Double averageNumberOfComments;
		Double standardDeviationNumberOfComments;
		Double averageNumberOfCommentsPerGym;
		Double averageNumberOfCommentsPerService;
		Collection<Customer> removedMoreComments;
		
		// Level A 2.0
		
		Map<ServiceEntity,Integer> servicesWithTrainesSpecialized;
		Double ratioOfTrainerWithCurriculumUpToDate;
		
		
		// Level C 1.0
		mostPopularGyms = gymService.findMostPopularGyms();
		leastPopularGyms = gymService.findLeastPopularGym();
		mostPopularService = serviceService.findMostPopularService();
		leastPopularService = serviceService.findLeastPopularService();
		paidMoreFees = customerService.findCustomerWhoHasPaidMoreFees();
		paidLessFees = customerService.findCustomerWhoHasPaidLessFees();
		
		// Level C 2.0
		averageRoomsPerGym = gymService.findAverageRoomsPerGym();
		standardDeviationRoomsPerGym = gymService.standardDeviationRoomsPerGym();
		gymsWithMoreRoomsThanAverage = gymService.gymsWithMoreRoomsThanAverage();
		gymsWithLessRoomsThanAverage = gymService.gymsWithLessRoomsThanAverage();
		moreInvoicesIssuedCustomer = customerService.moreInvoicesIssuedCustomer();
		noRequestedInvoicesCustomer = customerService.noRequestedInvoicesCustomer();
		
		// Level B 1.0
		sendMoreSpam = actorService.findActorWhoSendMoreSpam();
		averageNumberOfMessages = actorService.findAverageNumberOfMessagesInActorMessageBox();
		
		// Level B 2.0
		activitiesByPopularity = activityService.activitiesByPopularity();
		averageNumberOfActivitiesPerGymByService = activityService.averageNumberOfActivitiesPerGymByService();
		averageNumberOfServiceWithSpecialisedTrainer = serviceService.averageNumberOfServiceWithSpecialisedTrainer();
		mostPopularServiceByNumberOfTrainer = serviceService.mostPopularServiceByNumberOfTrainer();
		
		// Level A 1.0
		moreCommentedGyms = gymService.findMostCommented();
		moreCommentedServices = serviceService.findMostCommented();
		averageNumberOfComments = actorService.findAverageNumberOfCommentWrittenByAnActor();
		standardDeviationNumberOfComments = actorService.findStandardDeviationNumberOfCommentWrittenByAnActor();
		averageNumberOfCommentsPerGym = gymService.findAverageNumberOfCommentsPerGym();
		averageNumberOfCommentsPerService = serviceService.findAverageNumberOfCommentsPerService();
		removedMoreComments = customerService.findCustomerWhoHaveBeenRemovedMoreComments();
		
		// Level A 2.0
		servicesWithTrainesSpecialized = serviceService.servicesWithTrainesSpecialized();
		ratioOfTrainerWithCurriculumUpToDate = trainerService.ratioOfTrainerWithCurriculumUpToDate();
		
		result = new ModelAndView("administrator/list");
		
		// Level C 1.0
		result.addObject("mostPopularGyms", mostPopularGyms);
		result.addObject("leastPopularGyms", leastPopularGyms);
		result.addObject("mostPopularService", mostPopularService);
		result.addObject("leastPopularService", leastPopularService);
		result.addObject("paidMoreFees", paidMoreFees);
		result.addObject("paidLessFees", paidLessFees);
		
		// Level C 2.0
		result.addObject("averageRoomsPerGym", averageRoomsPerGym);
		result.addObject("standardDeviationRoomsPerGym", standardDeviationRoomsPerGym);
		result.addObject("gymsWithMoreRoomsThanAverage", gymsWithMoreRoomsThanAverage);
		result.addObject("gymsWithLessRoomsThanAverage", gymsWithLessRoomsThanAverage);
		result.addObject("moreInvoicesIssuedCustomer", moreInvoicesIssuedCustomer);
		result.addObject("noRequestedInvoicesCustomer", noRequestedInvoicesCustomer);
		
		// Level B 1.0
		result.addObject("sendMoreSpam", sendMoreSpam);
		result.addObject("averageNumberOfMessages", averageNumberOfMessages);
		
		// Level B 2.0
		result.addObject("activitiesByPopularity", activitiesByPopularity);
		result.addObject("averageNumberOfActivitiesPerGymByService", averageNumberOfActivitiesPerGymByService);
		result.addObject("averageNumberOfServiceWithSpecialisedTrainer", averageNumberOfServiceWithSpecialisedTrainer);
		result.addObject("mostPopularServiceByNumberOfTrainer", mostPopularServiceByNumberOfTrainer);
		
		// Level A 1.0
		result.addObject("moreCommentedGyms", moreCommentedGyms);
		result.addObject("moreCommentedServices", moreCommentedServices);
		result.addObject("averageNumberOfComments", averageNumberOfComments);
		result.addObject("standardDeviationNumberOfComments", standardDeviationNumberOfComments);
		result.addObject("averageNumberOfCommentsPerGym", averageNumberOfCommentsPerGym);
		result.addObject("averageNumberOfCommentsPerService", averageNumberOfCommentsPerService);
		result.addObject("removedMoreComments", removedMoreComments);
		
		// Level A 2.0
		result.addObject("servicesWithTrainesSpecialized", servicesWithTrainesSpecialized);
		result.addObject("ratioOfTrainerWithCurriculumUpToDate", ratioOfTrainerWithCurriculumUpToDate);
		
		result.addObject("requestURI", "dashboard/administrator/list.do");
		
		return result;
	}	
}
