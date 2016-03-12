package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Activity;
import domain.Customer;
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
	private RoomService roomService;

	// Constructors -----------------------------------------------------------

	public ActivityService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	// Other business methods -------------------------------------------------

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
	
	public Activity findOne(int activityId){
		
		Assert.notNull(activityId);
		Assert.isTrue(activityId != 0);
		
		Activity activity;
		
		activity = activityRepository.findOne(activityId);
		
		return activity;
	}
	
	public Collection<Activity> findAll(){
		Collection<Activity> result;
		
		result = activityRepository.findAll();
		
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
				
		if(activity.getId() != 0){
			
			Activity activityPreSave;
			ServiceEntity service;
			Room room;
			Trainer trainer;
			
			Collection<Activity> roomActivities;
			
			activityPreSave = activityRepository.findOne(activity.getId());
			
			service = activityPreSave.getService();
			room = activityPreSave.getRoom();
			trainer = activityPreSave.getTrainer();
			
			Assert.isTrue(service.getGyms().contains(room.getGym()));
			Assert.isTrue(trainer.getServices().contains(service));
						
			roomActivities = room.getActivities();
			roomActivities.remove(activity);
			room.setActivities(roomActivities);
			roomService.save(room);
		}
		
		this.save(activity);
	}
}
