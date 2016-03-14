package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Activity;
import domain.Customer;
import domain.FeePayment;
import domain.Gym;
import domain.Room;
import domain.ServiceEntity;
import domain.Trainer;
import repositories.ActivityRepository;

@Service
@Transactional
public class ActivityService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ActivityRepository activityRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private ServiceService serviceService;
	
	@Autowired
	private TrainerService trainerService;
	
	@Autowired
	private RoomService roomService;

	// Constructors -----------------------------------------------------------

	public ActivityService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	
	public Activity createWithGym(int gymId, int serviceId){
		
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can create an activity");
		
		Activity result;
		Collection<Customer> customers;
		Trainer trainer;
		ServiceEntity service;
		Room room;
		
		customers = new ArrayList<>();
		trainer = new Trainer();
		service = serviceService.findOne(serviceId);
		room = new Room();
		
		result = new Activity();
		
		result.setCustomers(customers);
		result.setTrainer(trainer);
		result.setService(service);
		result.setRoom(room);
		
		return result;
	}
	
public Activity createWithoutGym(){
		
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can create an activity");
		
		Activity result;
		Collection<Customer> customers;
		
		customers = new ArrayList<>();
		
		result = new Activity();
		
		result.setCustomers(customers);
		
		return result;
	}

	public Activity save(Activity activity){
		
		Assert.notNull(activity);
		Activity result;
		
		result = activityRepository.save(activity);
		
		return result;
	}
	
	public void saveToEdit(Activity activity){
		
		Assert.notNull(activity);
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can save an Activity");
		
		if(activity.getId() == 0) {	
		Assert.isTrue(activity.getRoom().getGym().getServices().contains(activity.getService()), "The Gym must include the Service you want");
		Assert.isTrue(activity.getTrainer().getServices().contains(activity.getService()), "The Trainer must be specialized in the Service you ask for");
		Assert.isTrue(activity.getNumberOfSeatsAvailable() <= activity.getRoom().getNumberOfSeats(), "The number os seats of the activity can be less than the number of seats of the Room");
		
		Collection<Customer> customers;
		Room room;
		Trainer trainer;
		ServiceEntity service;
		
		customers = new ArrayList<Customer>();
		
		activity.setDeleted(false);
		activity.setCustomers(customers);
		
		activity = this.save(activity);
		
		room = activity.getRoom();
		trainer = activity.getTrainer();
		service = activity.getService();
		
		room.addActivity(activity);
		trainer.addActivity(activity);
		service.addActivity(activity);
		
		roomService.save(room);
		trainerService.save(trainer);
		serviceService.save(service);
		} else {
			this.save(activity);
		}
		
	}
	
	public void book(Activity activity){
		
		Assert.notNull(activity);
		Assert.isTrue(activity.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("CUSTOMER"), "Only a customer can book an activity");
		Assert.isTrue(activity.getDeleted() == false, "This activity is already deleted by the administrator");
		
		Customer customer;
		Collection<FeePayment> feePayments;
		Collection<Gym> gyms = new HashSet<>();
		Collection<Activity> activities;
		boolean overlap = false;
		
		customer = customerService.findByPrincipal();
		feePayments = customer.getFeePayments();
		activities = customer.getActivities();
		
		for(FeePayment f:feePayments){
			if(f.getActiveMoment().before(new Date()) && f.getInactiveMoment().after(new Date())){
				gyms.add(f.getGym());
			}
		}

		// Falta el overlapping
		
		Assert.isTrue(!customer.getActivities().contains(activity), "You have already book this activity");
		Assert.isTrue(gyms.contains(activity.getRoom().getGym()), "This activity does not belongs to a paid gym");
		Assert.isTrue((activity.getNumberOfSeatsAvailable() - activity.getCustomers().size()) >= 1, "There are not a single seats available");
		
		// Falta el overlapping
		
		activity.getCustomers().add(customer);
		activities.add(activity);
		customer.setActivities(activities);
		customerService.save(customer);
		this.save(activity);
	}
	
	public void cancel(Activity activity){
		
		Assert.notNull(activity);
		Assert.isTrue(activity.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("CUSTOMER"), "Only a customer can cancel an activity");
		Assert.isTrue(activity.getDeleted() == false, "This activity is already deleted by the administrator");
		
		Customer customer;
		
		customer = customerService.findByPrincipal();
		
		Assert.isTrue(activity.getCustomers().contains(customer));
		Assert.isTrue(customer.getActivities().contains(activity));
		
		activity.getCustomers().remove(customer);
		customer.getActivities().remove(activity);
		
		Assert.isTrue(!activity.getCustomers().contains(customer), "The booking was not canceled 1");
		Assert.isTrue(!customer.getActivities().contains(activity), "The booking was not canceled 2");
		
		activityRepository.save(activity);
		
	}
	
	public void delete(Activity activity){
		
		 Assert.notNull(activity);
		 Assert.isTrue(activity.getId() != 0);
		 Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only a admin can deleted an activity");
		 Assert.isTrue(activity.getDeleted() == false, "This activity is already deleted");
		 
		 activity.setDeleted(true);
		 activityRepository.save(activity);
	}
	
	// Other business methods -------------------------------------------------
	
	public Collection<Activity> findAll(){
		Collection<Activity> result;
		
		result = activityRepository.findAll();
		
		return result;
	}
	
	public Activity findOne(int activityId){
		
		Assert.notNull(activityId);
		Assert.isTrue(activityId != 0);
		
		Activity activity;
		
		activity = activityRepository.findOne(activityId);
		
		return activity;
	}
	
	/**
	 * 
	 * @return Devuelve todos los activities de un customer en concreto
	 */
	public Collection<Activity> findAllByCustomer(){
		Collection<Activity> result;
		Customer customer;
		
		customer = customerService.findByPrincipal();
		Assert.notNull(customer);
		
		result = activityRepository.findAllByCustomer(customer.getId());
		
		return result;
	}
	
	/**
	 * 
	 * @return Devuelve todos los activities de un gym en concreto
	 */
	public Collection<Activity> findAllByGymId(int gymId){
		Collection<Activity> result;
		
		result = activityRepository.findAllByGymId(gymId);
		
		return result;		
	}
	
}
