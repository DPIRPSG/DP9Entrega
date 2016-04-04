package services;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import utilities.InvalidPostTestException;
import utilities.InvalidPreTestException;
import domain.Activity;
import domain.Customer;
import domain.Gym;
import domain.Room;
import domain.ServiceEntity;
import domain.Trainer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ActivityServiceTest extends AbstractTest{
	
	// Service under test -------------------------

	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private ServiceService serviceService;
	
	@Autowired
	private GymService gymService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private TrainerService trainerService;
	
	// Test ---------------------------------------
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 8.1
	 * A user who is authenticated as a customer must be able to:
	 * Book an activity in a gym as long as he or she has paid the corresponding fee, there
	 * is at least one seat available, and he or she has not booked any overlapping activities.
	 * 
	 * Objetivo: Test OK
	 */
	@Test
	public void testBookActivityOk(){
		Activity activity;
		Customer custo;
		Collection<Customer> customers;
		Collection<Gym> gyms;
		
		// Load objects to test
		authenticate("customer1");
		custo = customerService.findByPrincipal();
		activity = null;
		
		gyms = gymService.findAllWithFeePaymentActive();
		for(Gym gym:gyms){
			Collection<Activity> activities;
			authenticate("admin");
			activities = activityService.findAll();
			authenticate("customer1");
			for(Activity a:activities){
				boolean isAcceptable;
				
				isAcceptable = !a.getDeleted(); // Activity not deleted
				isAcceptable = isAcceptable && a.getStartingMoment().after(new Date()); //	Activity in the future
				isAcceptable = isAcceptable && a.getRoom().getGym().getId() == gym.getId(); // Activity belonging to gym
				isAcceptable = isAcceptable && a.getNumberOfSeatsAvailable() - a.getCustomers().size() > 0; // seats available
				isAcceptable = isAcceptable && this.isActivePayGym(a); // Active pay
				isAcceptable = isAcceptable && !activityService.findAllByCustomer().contains(a); // Activity not booked
				isAcceptable = isAcceptable && activityService.compruebaOverlappingCustomer(a); // No Overlapping
								
				if(isAcceptable){
					activity = a;
					break;
				}
			}	
		}
		
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado una actividad con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		customers = activity.getCustomers();
		customers.add(custo);
		
		activity.setCustomers(customers);
		activity = activityService.book(activity);
		
		// Checks results 
		custo = customerService.findByPrincipal();
		Assert.isTrue(activity.getCustomers().contains(custo), "El usuario no ha sido añadido a la actividad");
	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 8.1
	 * A user who is authenticated as a customer must be able to:
	 * Book an activity in a gym as long as he or she has paid the corresponding fee, there
	 * is at least one seat available, and he or she has not booked any overlapping activities.
	 * 
	 * Objetivo: Error al intentar realizarlo como admin
	 */
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testBookActivityErrorAdmin(){
		Activity activity;
		Customer custo;
		Collection<Customer> customers;
		Collection<Gym> gyms;
		
		// Load objects to test
		authenticate("customer1");
		custo = customerService.findByPrincipal();
		activity = null;
		
		
		gyms = gymService.findAllWithFeePaymentActive();
		for(Gym gym:gyms){
			Collection<Activity> activities;
			activities = activityService.findAllByGymId(gym.getId());
			for(Activity a:activities){
				boolean isAcceptable;
				
				isAcceptable = !a.getDeleted(); // Activity not deleted
				isAcceptable = isAcceptable && a.getStartingMoment().after(new Date()); //	Activity in the future
				isAcceptable = isAcceptable && a.getRoom().getGym().getId() == gym.getId(); // Activity belonging to gym
				isAcceptable = isAcceptable && a.getNumberOfSeatsAvailable() - a.getCustomers().size() > 0; // seats available
				isAcceptable = isAcceptable && this.isActivePayGym(a); // Active pay
				isAcceptable = isAcceptable && !activityService.findAllByCustomer().contains(a); // Activity not booked
				isAcceptable = isAcceptable && activityService.compruebaOverlappingCustomer(a); // No Overlapping
				
				if(isAcceptable){
					activity = a;
					break;
				}
			}	
		}
		
		
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado una actividad con la que realizar la comprobación");
		}catch (Exception e) {
			//assertThat(e);
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		authenticate("admin");
		
		customers = activity.getCustomers();
		customers.add(custo);
		
		activity.setCustomers(customers);
		activity = activityService.book(activity);
		
		// Checks results 
		try{
			authenticate("customer1");
			
			custo = customerService.findByPrincipal();
			Assert.isTrue(activity.getCustomers().contains(custo), "El usuario no ha sido añadido a la actividad");
		}catch (Exception e) {
			throw new InvalidPostTestException(e.toString());
		}		Assert.notNull(null, "PreTest - testBookActivityErrorAdmin no está hecho ! ! !");
	}
	
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 8.1
	 * A user who is authenticated as a customer must be able to:
	 * Book an activity in a gym as long as he or she has paid the corresponding fee, there
	 * is at least one seat available, and he or she has not booked any overlapping activities.
	 * 
	 * Objetivo: Error al intentar reservar una actividad ya borrada
	 */
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testBookActivityErrorDeleted(){
		Activity activity;
		Customer custo;
		Collection<Customer> customers;
		Collection<Gym> gyms;
		
		// Load objects to test
		authenticate("customer1");
		custo = customerService.findByPrincipal();
		activity = null;
		
		gyms = gymService.findAllWithFeePaymentActive();
		for(Gym gym:gyms){
			Collection<Activity> activities;
			activities = activityService.findAll();
			for(Activity a:activities){
				boolean isAcceptable;
				
				isAcceptable = a.getDeleted(); // Activity not deleted
				isAcceptable = isAcceptable && a.getStartingMoment().after(new Date()); //	Activity in the future
				isAcceptable = isAcceptable && a.getRoom().getGym().getId() == gym.getId(); // Activity belonging to gym
				isAcceptable = isAcceptable && a.getNumberOfSeatsAvailable() - a.getCustomers().size() > 0; // seats available
				isAcceptable = isAcceptable && this.isActivePayGym(a); // Active pay
				isAcceptable = isAcceptable && !activityService.findAllByCustomer().contains(a); // Activity not booked
				isAcceptable = isAcceptable && activityService.compruebaOverlappingCustomer(a); // No Overlapping
				
				if(isAcceptable){
					activity = a;
					break;
				}
			}	
		}
		
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado una actividad con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		customers = activity.getCustomers();
		customers.add(custo);
		
		activity.setCustomers(customers);
		activity = activityService.book(activity);
		
		// Checks results 
		try{
			custo = customerService.findByPrincipal();
			Assert.isTrue(activity.getCustomers().contains(custo), "El usuario no ha sido añadido a la actividad");
		}catch (Exception e) {
			throw new InvalidPostTestException(e.toString());
		}
	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 8.1
	 * A user who is authenticated as a customer must be able to:
	 * Book an activity in a gym as long as he or she has paid the corresponding fee, there
	 * is at least one seat available, and he or she has not booked any overlapping activities.
	 * 
	 * Objetivo: Error al intentar reservar una actividad para la que no quedan asientos
	 */
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testBookActivityErrorSeats(){
		Activity activity;
		Customer custo;
		Collection<Customer> customers;
		Collection<Gym> gyms;
	
		// Load objects to test
		authenticate("customer1");
		custo = customerService.findByPrincipal();
		
		activity = null;
		
		gyms = gymService.findAllWithFeePaymentActive();
		for(Gym gym:gyms){
			Collection<Activity> activities;
			activities = activityService.findAllByGymId(gym.getId());
			for(Activity a:activities){
				boolean isAcceptable;
				
				isAcceptable = activityService.compruebaOverlappingCustomer(a);
				isAcceptable = isAcceptable && a.getNumberOfSeatsAvailable() - a.getCustomers().size() < 1;
				
				isAcceptable = !a.getDeleted(); // Activity not deleted
				isAcceptable = isAcceptable && a.getStartingMoment().after(new Date()); //	Activity in the future
				isAcceptable = isAcceptable && a.getRoom().getGym().getId() == gym.getId(); // Activity belonging to gym
				isAcceptable = isAcceptable && a.getNumberOfSeatsAvailable() - a.getCustomers().size() < 1; // seats available
				isAcceptable = isAcceptable && this.isActivePayGym(a); // Active pay
				isAcceptable = isAcceptable && !activityService.findAllByCustomer().contains(a); // Activity not booked
				isAcceptable = isAcceptable && activityService.compruebaOverlappingCustomer(a); // No Overlapping
				
				if(isAcceptable){
					activity = a;
					break;
				}
			}	
		}
		
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado una actividad con la que realizar la comprobación");
			Assert.isTrue(activity.getStartingMoment().after(new Date()), "PreTest - La actividad tiene un startingMoment en el pasado");
			Assert.isTrue(activity.getNumberOfSeatsAvailable() - activity.getCustomers().size() < 1 , "PreTest - La actividad tiene asientos disponibles y se quiere probar lo contrario");
			Assert.isTrue(this.isActivePayGym(activity), "PreTest - La actividad no está asociada a un gimnasio con pago activo");
			Assert.isTrue(activityService.compruebaOverlappingCustomer(activity), "PreTest - La actividad coincide con otra actividad ya reservada");
			Assert.isTrue(!activityService.findAllByCustomer().contains(activity), "PreTest - La actividad ha sido reservada por el customer");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
	
		customers = activity.getCustomers();
		customers.add(custo);
		
		activity.setCustomers(customers);
		activity = activityService.book(activity);

		// Checks results 
		try{
			custo = customerService.findByPrincipal();
		
			Assert.isTrue(! activity.getCustomers().contains(custo), "El usuario ha sido añadido a la actividad y no debería haber sido así");
		}catch (Exception e) {
			throw new InvalidPostTestException(e.toString());
		}
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testBookActivityErrorInPast(){
		Activity activity;
		Customer custo;
		Collection<Customer> customers;
		Collection<Gym> gyms;
		
		// Load objects to test
		authenticate("customer2");
		custo = customerService.findByPrincipal();
		activity = null;
		
		gyms = gymService.findAll();
		for(Gym gym:gyms){
			Collection<Activity> activities;
			activities = activityService.findAll();
			for(Activity a:activities){
				boolean isAcceptable;
				
				isAcceptable = !a.getDeleted(); // Activity not deleted
				isAcceptable = isAcceptable && a.getStartingMoment().before(new Date()); //	Activity in the past
				isAcceptable = isAcceptable && a.getRoom().getGym().getId() == gym.getId(); // Activity belonging to gym
				isAcceptable = isAcceptable && a.getNumberOfSeatsAvailable() - a.getCustomers().size() > 0; // seats available
				// isAcceptable = isAcceptable && this.isActivePayGym(a); // Active pay
				isAcceptable = isAcceptable && !activityService.findAllByCustomer().contains(a); // Activity not booked
				// isAcceptable = isAcceptable && activityService.compruebaOverlappingCustomer(a); // No Overlapping
				
				if(isAcceptable){
					activity = a;
					break;
				}
			}	
		}
				
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado una actividad con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		customers = activity.getCustomers();
		customers.add(custo);
		
		activity.setCustomers(customers);
		activity = activityService.book(activity);
		
		// Checks results 
		try{
			custo = customerService.findByPrincipal();
			Assert.isTrue(! activity.getCustomers().contains(custo), "El usuario ha sido añadido a la actividad y no debería haber sido así");			
		}catch (Exception e) {
			throw new InvalidPostTestException(e.toString());
		}
	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 8.1
	 * A user who is authenticated as a customer must be able to:
	 * Book an activity in a gym as long as he or she has paid the corresponding fee, there
	 * is at least one seat available, and he or she has not booked any overlapping activities.
	 * 
	 * Objetivo: Error al intentar reservar un gym sin pago activo
	 */
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testBookActivityErrorGymNotActivePaid(){
		Activity activity;
		Customer custo;
		Collection<Customer> customers;
		Collection<Activity> activities;
		
		// Load objects to test
		authenticate("customer2");
		custo = customerService.findByPrincipal();
		activities = activityService.findAll();
		activity = null;

		
		for (Activity a : activities) {
			boolean isAcceptable;
			
			isAcceptable = !a.getDeleted(); // Activity not deleted
			isAcceptable = isAcceptable && a.getStartingMoment().after(new Date()); //	Activity in the future
			//isAcceptable = isAcceptable && a.getRoom().getGym().getId() == gym.getId(); // Activity belonging to gym
			isAcceptable = isAcceptable && a.getNumberOfSeatsAvailable() - a.getCustomers().size() > 0; // seats available
			isAcceptable = isAcceptable && ! this.isActivePayGym(a); // Active pay
			isAcceptable = isAcceptable && !activityService.findAllByCustomer().contains(a); // Activity not booked
			isAcceptable = isAcceptable && activityService.compruebaOverlappingCustomer(a); // No Overlapping
			
			if (isAcceptable){
				activity = a;
				break;
			}
		}
		
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado una actividad con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
	
		customers = activity.getCustomers();
		customers.add(custo);
		
		activity.setCustomers(customers);
		activity = activityService.book(activity);

		// Checks results 
		try{
			custo = customerService.findByPrincipal();
			
			Assert.isTrue(! activity.getCustomers().contains(custo), "El usuario ha sido añadido a la actividad y no debería haber sido así");
		}catch (Exception e) {
			throw new InvalidPostTestException(e.toString());
		}
		
	}	
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 8.1
	 * A user who is authenticated as a customer must be able to:
	 * Book an activity in a gym as long as he or she has paid the corresponding fee, there
	 * is at least one seat available, and he or she has not booked any overlapping activities.
	 * 
	 * Objetivo: Error al intentar realizarlo como otro usuario
	 */
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testBookActivityErrorOtherCustomer(){
		Activity activity;
		Customer custo;
		Collection<Customer> customers;
		Collection<Gym> gyms;
		
		// Load objects to test
		authenticate("customer1");
		custo = customerService.findByPrincipal();
		activity = null;
		
		gyms = gymService.findAllWithFeePaymentActive();
		for(Gym gym:gyms){
			Collection<Activity> activities;
			activities = activityService.findAll();
			for(Activity a:activities){
				boolean isAcceptable;
				
				isAcceptable = !a.getDeleted(); // Activity not deleted
				isAcceptable = isAcceptable && a.getStartingMoment().after(new Date()); //	Activity in the future
				isAcceptable = isAcceptable && a.getRoom().getGym().getId() == gym.getId(); // Activity belonging to gym
				isAcceptable = isAcceptable && a.getNumberOfSeatsAvailable() - a.getCustomers().size() > 0; // seats available
				
				authenticate("customer1");
				isAcceptable = isAcceptable && this.isActivePayGym(a); // Active pay
				isAcceptable = isAcceptable && !activityService.findAllByCustomer().contains(a); // Activity not booked
				isAcceptable = isAcceptable && activityService.compruebaOverlappingCustomer(a); // No Overlapping
				
				authenticate("customer2");
				isAcceptable = isAcceptable && this.isActivePayGym(a); // Active pay
				isAcceptable = isAcceptable && !activityService.findAllByCustomer().contains(a); // Activity not booked
				isAcceptable = isAcceptable && activityService.compruebaOverlappingCustomer(a); // No Overlapping
								
				if(isAcceptable){
					activity = a;
					break;
				}
			}	
		}
		
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado una actividad con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		authenticate("customer2");
		
		customers = activity.getCustomers();
		customers.add(custo);
		
		activity.setCustomers(customers);
		activity = activityService.book(activity);
		
		// Checks results 

	}
	

	/**
	 * Acme-Six-Pack-2.0 - Level B - 8.2
	 * List the activities that he or she has booked
	 * 
	 * Objetivo: Ok
	 */
	@Test
	public void testListActivitiesBookedCustoOk(){
		
		// Load objects to test
		Collection<Activity> activities;
		
		// Checks basic requirements
	
		// Execution of test

		authenticate("customer1");
		activities = activityService.findAllByCustomer();
		
		// Checks results
		Assert.isTrue(activities.size() == 2, "El número de activities obtenido no es 2, como se esperaba.");
		
	}
	/**
	 * Acme-Six-Pack-2.0 - Level B - 8.2
	 * List the activities that he or she has booked
	 * 
	 * Objetivo: Error al realizarlo un admin
	 */
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testListActivitiesBookedCustoErrorAdmin(){
		
		// Load objects to test
		
		// Checks basic requirements
	
		// Execution of test

		authenticate("admin");
		activityService.findAllByCustomer();
		
		// Checks results 

	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 8.3
	 * Cancel a booking of his or her
	 * 
	 * Objetivo: Ok
	 */
	@Test
	public void testCancelActivityBookedOk(){
		Activity activity;
		
		// Load objects to test
		authenticate("customer1");
		activity = null;
		
		for(Activity a:activityService.findAllByCustomer()){
			boolean isAcceptable;

			isAcceptable = a.getStartingMoment().after(new Date()) && ! a.getDeleted();
			
			if(isAcceptable){
				activity = a;
				break;
			}
		}
		
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado una actividad con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		
		authenticate("customer1");

		activityService.cancel(activity);

		// Checks results 
		
		Assert.isTrue(!activityService.findAllByCustomer().contains(activity), "La actividad no ha sido eliminada");
	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 8.3
	 * Cancel a booking of his or her
	 * 
	 * Objetivo: Error al cancelar una actividad no asignada
	 */
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCancelActivityBookedErrorNotAsigned(){
		Activity activity;
		
		// Load objects to test
		authenticate("customer1");
		activity = null;
		
		for(Activity a:activityService.findAll()){
			boolean isAcceptable;

			isAcceptable = a.getStartingMoment().after(new Date()) && ! a.getDeleted();
			
			isAcceptable = isAcceptable && ! activityService.findAllByCustomer().contains(a);
			
			if(isAcceptable){
				activity = a;
				break;
			}
		}
		
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado una actividad con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		
		authenticate("customer1");

		activityService.cancel(activity);

		// Checks results 
		
	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 8.3
	 * Cancel a booking of his or her
	 * 
	 * Objetivo: Error al cancelar una actividad pasada
	 */
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCancelActivityBookedErrorPast(){
		Activity activity;
		Customer custo;
		Collection<Gym> gyms;
		
		// Load objects to test
		authenticate("customer1");
		
		custo = customerService.findByPrincipal();
		activity = null;
		
		gyms = gymService.findAllWithFeePaymentActive();
		for(Gym gym:gyms){
			Collection<Activity> activities;
			activities = activityService.findAll();
			for(Activity a:activities){
				boolean isAcceptable;

				isAcceptable = !a.getStartingMoment().after(new Date()) && ! a.getDeleted();
				isAcceptable = isAcceptable && a.getCustomers().contains(custo);
				isAcceptable = isAcceptable && gym.getRooms().contains(a.getRoom());
			
				if(isAcceptable){
					activity = a;
					break;
				}
			}
		}
		
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado una actividad con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		
		authenticate("customer1");

		activityService.cancel(activity);

		// Checks results 
	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 10.2.1
	 * List the activities
	 * 
	 * Objetivo: Ok
	 */
	@Test
	public void testListActivitiesAdminOk(){
	
		// Load objects to test
		Collection<Activity> activities;
		
		// Checks basic requirements
		
		// Execution of test
		
		authenticate("admin");
		
		activities = activityService.findAll();
		
		// Checks results 
		Assert.isTrue(activities.size() == 10, "El número de activities obtenido no es 10, como se esperaba.");

	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 10.2.1
	 * List the activities
	 * 
	 * Objetivo: Error al realizarlo como Customer
	 */
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testListActivitiesAdminErrorCusto(){
	
		// Load objects to test
		
		// Checks basic requirements
		
		// Execution of test
		
		authenticate("customer1");
		
		activityService.findAll();

		// Checks results 

	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 10.2.2
	 * Create the activities
	 * 
	 * Objetivo: Ok
	 */
	@Test
	public void testCreateActivitiesAdminOk(){
		Activity activity;
		Gym gym;
		ServiceEntity service;
		Trainer trainer;
		Room room;
		Date startingMoment;
		Integer numberOfActivities;
		Integer newNumberOfActivities;
		
		// Load objects to test
		
		authenticate("admin");
		
		numberOfActivities = activityService.findAll().size();
		
		activity = null;
		gym = null;
		service = null;
		room = null;
		trainer = null;
		
		startingMoment = new Date();
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, +6);
		startingMoment.setTime(c.getTimeInMillis());
		
		for(Gym g:gymService.findAll()){
			for(ServiceEntity s:serviceService.findAllByGym(g.getId())){
				room = null;
				trainer = null;

				for(Room r:g.getRooms()){
						room = r;
				}
				
				for(Trainer t:trainerService.findAllByServiceId(s.getId())){
					if(t.getActivities().size() == 0)
						trainer = t;
				}
				if(room != null && trainer != null){
					gym = g;
					service = s;					
					break;
				}
			}
		}
		
		// Checks basic requirements
		try{
			Assert.notNull(gym, "No se ha encontrado un gym con la que realizar la comprobación");
			Assert.notNull(service, "No se ha encontrado un service con la que realizar la comprobación");
			Assert.notNull(trainer, "No se ha encontrado un trainer con la que realizar la comprobación");
			Assert.notNull(room, "No se ha encontrado un room con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		
		authenticate("admin");
		
		activity = activityService.createWithGym(gym.getId(), service.getId());

		activity.setTrainer(trainer);
		activity.setTitle("activity_test_");
		activity.setNumberOfSeatsAvailable(room.getNumberOfSeats());
		activity.setStartingMoment(startingMoment);
		activity.setDuration(0.5);
		activity.setDescription("Any description");
		activity.setRoom(room);
		
		activityService.saveToEdit(activity);
		
		newNumberOfActivities = activityService.findAll().size();

		// Checks results 
		Assert.isTrue(numberOfActivities + 1 == newNumberOfActivities, "El número de actividades tras añadir una nueva no es igual al de antes de añadirla + 1.");

	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 10.2.2
	 * Create the activities
	 * 
	 * Objetivo: Error debido al número de asientos 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateActivitiesAdminErrorSeats(){
		Activity activity;
		Gym gym;
		ServiceEntity service;
		Trainer trainer;
		Room room;
		Date startingMoment;
		
		// Load objects to test
		
		authenticate("admin");
		
		activity = null;
		gym = null;
		service = null;
		room = null;
		trainer = null;
		
		startingMoment = new Date();
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, +6);
		startingMoment.setTime(c.getTimeInMillis());
		
		for(Gym g:gymService.findAll()){
			for(ServiceEntity s:serviceService.findAllByGym(g.getId())){
				room = null;
				trainer = null;

				for(Room r:g.getRooms()){
						room = r;
				}
				
				for(Trainer t:trainerService.findAllByServiceId(s.getId())){
					if(t.getActivities().size() == 0)
						trainer = t;
				}
				if(room != null && trainer != null){
					gym = g;
					service = s;					
					break;
				}
			}
		}
		
		// Checks basic requirements
		try{
			Assert.notNull(gym, "No se ha encontrado un gym con la que realizar la comprobación");
			Assert.notNull(service, "No se ha encontrado un service con la que realizar la comprobación");
			Assert.notNull(trainer, "No se ha encontrado un trainer con la que realizar la comprobación");
			Assert.notNull(room, "No se ha encontrado un room con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		
		authenticate("admin");
		
		activity = activityService.createWithGym(gym.getId(), service.getId());

		activity.setTrainer(trainer);
		activity.setTitle("activity_test_");
		activity.setNumberOfSeatsAvailable(room.getNumberOfSeats() + 1);
		activity.setStartingMoment(startingMoment);
		activity.setDuration(0.5);
		activity.setDescription("Any description");
		activity.setRoom(room);
		
		activityService.saveToEdit(activity);

		// Checks results 

	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 10.2.2
	 * Create the activities
	 * 
	 * Objetivo: Error debido a una fecha en pasado
	 */
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateActivitiesAdminErrorDateInPast(){
		Activity activity;
		Gym gym;
		ServiceEntity service;
		Trainer trainer;
		Room room;
		Date startingMoment;
		
		// Load objects to test
		
		authenticate("admin");
		
		activity = null;
		gym = null;
		service = null;
		room = null;
		trainer = null;
		
		startingMoment = new Date();
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		startingMoment.setTime(c.getTimeInMillis());
		
		for(Gym g:gymService.findAll()){
			for(ServiceEntity s:serviceService.findAllByGym(g.getId())){
				room = null;
				trainer = null;

				for(Room r:g.getRooms()){
						room = r;
				}
				
				for(Trainer t:trainerService.findAllByServiceId(s.getId())){
					if(t.getActivities().size() == 0)
						trainer = t;
				}
				if(room != null && trainer != null){
					gym = g;
					service = s;					
					break;
				}
			}
		}
		
		// Checks basic requirements
		try{
			Assert.notNull(gym, "No se ha encontrado un gym con la que realizar la comprobación");
			Assert.notNull(service, "No se ha encontrado un service con la que realizar la comprobación");
			Assert.notNull(trainer, "No se ha encontrado un trainer con la que realizar la comprobación");
			Assert.notNull(room, "No se ha encontrado un room con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		
		authenticate("admin");
		
		activity = activityService.createWithGym(gym.getId(), service.getId());

		activity.setTrainer(trainer);
		activity.setTitle("activity_test_");
		activity.setNumberOfSeatsAvailable(room.getNumberOfSeats());
		activity.setStartingMoment(startingMoment);
		activity.setDuration(0.5);
		activity.setDescription("Any description");
		activity.setRoom(room);
		
		activityService.saveToEdit(activity);

		// Checks results 

	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 10.2.3
	 * Modify the activities
	 * 
	 * Objetivo: Ok
	 */
	@Test
	public void testModifyActivitiesAdminOkSeats(){
		Activity activity;
		Integer activityId;
		Activity activityModified;

		// Load objects to test
		
		authenticate("admin");
		
		activity = null;

		for(Activity a:activityService.findAll()){
			boolean isAcceptable;
			
			isAcceptable = a.getStartingMoment().after(new Date()) && a.getRoom().getNumberOfSeats() != a.getNumberOfSeatsAvailable();
			
			if(isAcceptable){
				activity = a;
			}
		}
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado un activity con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		activityId = activity.getId();
		
		// Execution of test
		
		authenticate("admin");
		
		activity.setNumberOfSeatsAvailable(activity.getRoom().getNumberOfSeats());
		activityService.saveToEdit(activity);

		// Checks results
		activityModified = activityService.findOne(activityId);
		Assert.isTrue(activityModified.getNumberOfSeatsAvailable() == activity.getRoom().getNumberOfSeats(), "No se ha editado correctamente el número de asientos disponibles para la actividad.");

	}

	/**
	 * Acme-Six-Pack-2.0 - Level B - 10.2.3
	 * Modify the activities
	 * 
	 * Objetivo: Error modificar asientos por encima de lo permitido
	 */
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testModifyActivitiesAdminErrorSeatsOver(){
		Activity activity;

		// Load objects to test
		
		authenticate("admin");
		
		activity = null;

		for(Activity a:activityService.findAll()){
			boolean isAcceptable;
			
			isAcceptable = a.getStartingMoment().after(new Date()) && a.getRoom().getNumberOfSeats() != a.getNumberOfSeatsAvailable();
			
			if(isAcceptable){
				activity = a;
			}
		}
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado un activity con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		
		authenticate("admin");
		
		activity.setNumberOfSeatsAvailable(activity.getRoom().getNumberOfSeats()+1);
		activityService.saveToEdit(activity);

		// Checks results 

	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 10.2.3
	 * Modify the activities
	 * 
	 * Objetivo: Error modificar asientos por debajo de lo permitido
	 */
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testModifyActivitiesAdminErrorSeatsLower(){
		Activity activity;

		// Load objects to test
		
		authenticate("admin");
		
		activity = null;

		for(Activity a:activityService.findAll()){
			boolean isAcceptable;
			
			isAcceptable = a.getStartingMoment().after(new Date()) && a.getCustomers().size() > 1;
			
			if(isAcceptable){
				activity = a;
			}
		}
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado un activity con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		
		authenticate("admin");
		
		activity.setNumberOfSeatsAvailable(activity.getCustomers().size()-1);
		activityService.saveToEdit(activity);

		// Checks results 

	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 10.2.4
	 * Delete an activities
	 * 
	 * Objetivo: Ok
	 */
	@Test
	public void testDeleteActivitiesAdminOk(){
		Activity activity;
		Integer numberOfActivitiesNotDeleted;
		Integer newNumberOfActivitiesNotDeleted;
		Collection<Activity> activitiesNotDeleted;
		Collection<Activity> newActivitiesNotDeleted;

		// Load objects to test
		
		authenticate("admin");
		
		activity = null;
		
		activitiesNotDeleted = new ArrayList<Activity>();
		
		for(Activity a:activityService.findAll()){
			
			if(a.getDeleted() == false){
				activitiesNotDeleted.add(a);
			}
			
		}

		for(Activity a:activityService.findAll()){
			boolean isAcceptable;
			
			isAcceptable = a.getStartingMoment().after(new Date()) && a.getCustomers().size() < 1;
			
			if(isAcceptable){
				activity = a;
				break;
			}
			
		}
		
		numberOfActivitiesNotDeleted = activitiesNotDeleted.size();
		
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado un activity con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		
		authenticate("admin");
		
		activityService.delete(activity);

		// Checks results
		newActivitiesNotDeleted = new ArrayList<Activity>();
		
		for(Activity a:activityService.findAll()){
			
			if(a.getDeleted() == false){
				newActivitiesNotDeleted.add(a);
			}
			
		}
		
		newNumberOfActivitiesNotDeleted = newActivitiesNotDeleted.size();
				
		Assert.isTrue(numberOfActivitiesNotDeleted - 1 == newNumberOfActivitiesNotDeleted, "El número de actividades tras borrar una de ellas no es igual al numero de actividades que había antes - 1.");

	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 10.2.4
	 * Delete an activities
	 * 
	 * Objetivo: Error delete with customer
	 */
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testDeleteActivitiesAdminErrorContainCustomers(){
		Activity activity;

		// Load objects to test
		
		authenticate("admin");
		
		activity = null;

		for(Activity a:activityService.findAll()){
			boolean isAcceptable;
			
			isAcceptable = a.getStartingMoment().after(new Date()) && a.getCustomers().size() > 0;
			
			if(isAcceptable){
				activity = a;
				break;
			}
		}
		// Checks basic requirements
		try{
			Assert.notNull(activity, "No se ha encontrado un activity con la que realizar la comprobación");
		}catch (Exception e) {
			throw new InvalidPreTestException(e.toString());
		}
		
		// Execution of test
		
		authenticate("admin");
		
		activityService.delete(activity);

		// Checks results 

	}
	
	// Ancillary Methods -----------------------------
	/**
	 * 
	 * @return True if the activity is payed by a FeePayment asociate to the gym
	 */
	private boolean isActivePayGym(Activity input){
		Collection<Gym> gyms;
		Collection<Activity> activitiesPayed;
		
		activitiesPayed = new ArrayList<Activity>();

		gyms = gymService.findAllWithFeePaymentActive();

		for(Gym g:gyms){
			activitiesPayed.addAll(activityService.findAllByGymId(g.getId()));
		}
		
		return activitiesPayed.contains(input);
	}
}
