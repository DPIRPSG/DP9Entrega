package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Activity;
import domain.Customer;
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
