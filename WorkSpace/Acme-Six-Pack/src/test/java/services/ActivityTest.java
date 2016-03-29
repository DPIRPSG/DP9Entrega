package services;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.activity.InvalidActivityException;

import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CustomerRepository;

import domain.Activity;
import domain.Customer;
import domain.Gym;
import domain.Room;

import utilities.AbstractTest;
import utilities.InvalidPostTestException;
import utilities.InvalidPreTestException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ActivityTest extends AbstractTest{

	// Pending test 
	
		// activityService.findAllByCustomer()
		// compruebaOverlappingCustomer
		// findAllByGymId
		// Al crear activity que no tenga mas asientos que la room
		// Añadir en los PreTest y los búcles comprobación de no en pasado y de no reservada ya
	
	// Service under test -------------------------

	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private GymService gymService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	// Test ---------------------------------------
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 8.1
	 * A user who is authenticated as a customer must be able to:
	 * Book an activity in a gym as long as he or she has paid the corresponding fee, there
	 * is at least one seat available, and he or she has not booked any overlapping activities.
	 * 
	 * Caso positivo, todo ok
	 * 		 + Seleccionar customer
	 * 		 + Coger salas (de un gimnasio con pago activo) y seleccionar un activity (comprobar asientos libres, comprobar no overlapping, comprobar gimnasio con pago activo)
	 * 		 + Añadir el customer a la activity
	 * 		 + Comprobar que, efectivamente está añadido y no coincide
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
			activities = activityService.findAll();
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
	
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testBookActivityErrorOverlapping(){
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
				isAcceptable = isAcceptable && !activityService.compruebaOverlappingCustomer(a); // No Overlapping
				
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
