package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ServiceRepository;
import domain.Activity;
import domain.Comment;
import domain.FeePayment;
import domain.Gym;
import domain.ServiceEntity;
import domain.Trainer;

@Service
@Transactional
public class ServiceService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ServiceRepository serviceRepository;

	// Supporting services ----------------------------------------------------
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private FeePaymentService feePaymentService;
	
	@Autowired
	private ActivityService activityService;
	
	// Constructors -----------------------------------------------------------

	public ServiceService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public ServiceEntity create() {
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can create services");
		
		ServiceEntity result;
		Collection<Gym> gyms;
		Collection<Comment> comments;
		Collection<String> pictures;
		Collection<Activity> activities;
		Collection<Trainer> trainers;
		
		gyms = new ArrayList<Gym>();
		pictures = new ArrayList<String>();
		comments = new ArrayList<Comment>();
		activities = new ArrayList<Activity>();
		trainers = new ArrayList<Trainer>();
		
		
		result = new ServiceEntity();
		
		result.setGyms(gyms);
		result.setPictures(pictures);
		result.setComments(comments);
		result.setActivities(activities);
		result.setTrainers(trainers);
		
		return result;
	}
	
	public void save(ServiceEntity service) {
		Assert.notNull(service);
		
		serviceRepository.save(service);		
	}
	
	public void saveToEdit(ServiceEntity service) {
		Assert.notNull(service);
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can save services");
		
		if(service.getId() != 0) {			
			ServiceEntity servicePreSave;
			Collection<Gym> gyms;
			Collection<Comment> comments;
			
			servicePreSave = serviceRepository.findOne(service.getId());
			
			if(servicePreSave.getName().equals("Fitness")) {
				Assert.isTrue(service.getName().equals("Fitness"), "El servicio siempre debe llamarse Fitness");
			}
			
			gyms = servicePreSave.getGyms();
			comments = servicePreSave.getComments();
			
			Assert.isTrue(service.getGyms().containsAll(gyms) && service.getGyms().size() == gyms.size());
			Assert.isTrue(service.getComments().containsAll(comments) && service.getComments().size() == comments.size());
		} else {
			Collection<Activity> activities;
			Collection<Trainer> trainers;
			
			activities = new ArrayList<Activity>();
			trainers = new ArrayList<Trainer>();
			
			service.setActivities(activities);
			service.setTrainers(trainers);
		}
		
		serviceRepository.save(service);
	}
	
	public void delete(ServiceEntity service) {
		Assert.notNull(service);
		Assert.isTrue(service.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can delete services");
		Assert.isTrue(service.getGyms().isEmpty());
		Assert.isTrue(service.getComments().isEmpty());
		Assert.isTrue(service.getName() != "Fitness", "El servicio de Fitness lo pueden tener todos los gimnasios, luego no se deberá borrar");
		Assert.isTrue(service.getActivities().isEmpty());
		
		serviceRepository.delete(service);
	}
	
	// Other business methods -------------------------------------------------
	
	/**
	 * Lista un Service concreto
	 */

	public ServiceEntity findOne(int serviceId) {
		ServiceEntity result;
		
		result = serviceRepository.findOne(serviceId);
		Assert.notNull(result, "There is no service with the id: " + serviceId);
		
		return result;
		
	}
	
	/**
	 * Lista todos los Services
	 */
	
	public Collection<ServiceEntity> findAll() {
		Collection<ServiceEntity> result;

		result = serviceRepository.findAll();

		return result;
	}
	
	/**
	 * Lista los Services de un Gym dado
	 */
	
	public Collection<ServiceEntity> findAllByGym(int gymId) {
		Collection<ServiceEntity> result;

		result = serviceRepository.findAllByGym(gymId);

		return result;
	}
	
	public Collection<ServiceEntity> findMostCommented() {
		Assert.isTrue(actorService.checkAuthority("ADMIN"));
		
		Collection<ServiceEntity> result;

		result = serviceRepository.findMostCommented();

		return result;
	}
	
	public Collection<ArrayList<Integer>> numbersOfCustomersByService(Collection<ServiceEntity> services) {
		Collection<ArrayList<Integer>> result;
		Integer customerNumber;
		ArrayList<Integer> result2;
		
		result = new ArrayList<ArrayList<Integer>>();
		
		for(ServiceEntity service : services) {
			result2 = new ArrayList<Integer>();
			customerNumber = customerService.numbersOfCustomersByService(service.getId());
			result2.add(service.getId());
			result2.add(customerNumber);
			result.add(result2);
		}
		
		return result;
	}

	public ServiceEntity findOneByName(String name) {
		Assert.notNull(name);
		
		ServiceEntity result;
		
		result = serviceRepository.findOneByName(name);
		
		return result;
	}
	
	public Collection<ServiceEntity> findAllWithoutFitness() {
		Collection<ServiceEntity> result;
		
		result = serviceRepository.findAllWithoutFitness();
		
		return result;
	}
	
	public Collection<ServiceEntity> findAllPaidAndNotBookedByCustomerId() {
		Collection<ServiceEntity> result;
		Collection<ServiceEntity> services;
		Collection<FeePayment> fees;
		Collection<Activity> activities;

		result = new ArrayList<ServiceEntity>();
		fees = feePaymentService.findAllActiveByCustomer();
		for(FeePayment fee : fees) {
			services = fee.getGym().getServices();
			for(ServiceEntity service : services) {
				if(!result.contains(service)) {
					result.add(service);
				}
			}
		}
		
		activities = activityService.findAllByCustomer();
		for(Activity activity : activities) {
			if(result.contains(activity.getService())) {
				result.remove(activity.getService());
			}
		}
		
		return result;
	}
	
	/* Query 3 */
	public Collection<ServiceEntity> findMostPopularService(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can open the dashboard");
		
		Collection<ServiceEntity> result;
		
		result = serviceRepository.findMostPopularService();
		
		return result;
	}
	
	/* Query 4 */
	public Collection<ServiceEntity> findLeastPopularService(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can open the dashboard");
		
		Collection<ServiceEntity> result;
		
		result = serviceRepository.findLeastPopularService();
		
		return result;
	}
	
	/* Query 13 */
	public Double findAverageNumberOfCommentsPerService(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can open the dashboard");
		
		Double result;
		
		result = serviceRepository.findAverageNumberOfCommentsPerService();
		
		return result;
	}
	
	
	public Double averageNumberOfServiceWithSpecialisedTrainer(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can open the dashboard");

		Double result;
		
		result = serviceRepository.averageNumberOfServiceWithSpecialisedTrainer();
		
		return result;		
	}
	
	public Collection<ServiceEntity> mostPopularServiceByNumberOfTrainer(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can open the dashboard");

		Collection<ServiceEntity> result;
		
		result = serviceRepository.mostPopularServiceByNumberOfTrainer();
		
		return result;			
	}
	
	public Map<ServiceEntity,Integer> servicesWithTrainesSpecialized(){
		
		Map<ServiceEntity,Integer> result;
		Collection<ServiceEntity> listOfServices;
		
		result = new HashMap<>();
		listOfServices = findAll();
		
		for(ServiceEntity s:listOfServices){
			result.put(s, s.getTrainers().size());
		}
		
		return result;
	}
	
	public void flush(){
		serviceRepository.flush();
	}
}
