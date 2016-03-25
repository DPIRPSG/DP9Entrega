package services;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.activity.InvalidActivityException;

import org.junit.Test;
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
import utilities.InvalidPreTestException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ActivityTest extends AbstractTest{

	// Pendientes a probar ! ! ! ! ! ! - - - - - - - - - - - 
	
		// activityService.findAllByCustomer()
		// compruebaOverlappingCustomer
		// findAllByGymId
		// Al crear activity que no tenga mas asientos que la room
	
	// Service under test -------------------------

	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private GymService gymService;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private CustomerService customerService;
	
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
		
		// Carga de objetos
		authenticate("customer1");
		custo = customerService.findByPrincipal();
		activity = null;
		
		gyms = gymService.findAllWithFeePaymentActive();
		for(Gym gym:gyms){
			Collection<Activity> activities;
			activities = activityService.findAllByGymId(gym.getId());
			for(Activity a:activities){
				boolean isAcceptable;
				
				isAcceptable = ! activityService.compruebaOverlappingCustomer(a);
				isAcceptable = isAcceptable && a.getNumberOfSeatsAvailable() - a.getCustomers().size() > 0;
				
				if(isAcceptable){
					activity = a;
					break;
				}
			}	
		}
		
		// Comprobaciones básicas de requisitos
		
		Assert.notNull(activity, "No se ha encontrado una actividad con la que realizar la comprobación");
		Assert.isTrue(activity.getStartingMoment().after(new Date()), "PreTest - La actividad tiene un startingMoment en el pasado");
		Assert.isTrue(activity.getNumberOfSeatsAvailable() - activity.getCustomers().size() > 0 , "PreTest - La actividad no tiene asientos disponibles");
		Assert.isTrue(activityService.findAllByCustomer().contains(activity), "PreTest - La actividad no está asociada a un gimnasio con pago activo");
		
		// Ejecución del test
		customers = activity.getCustomers();
		customers.add(custo);
		
		activity.setCustomers(customers);
		activity = activityService.save(activity);
		
		// Comprobación resultados 
		custo = customerService.findByPrincipal();
		
		Assert.isTrue(activity.getCustomers().contains(custo), "El usuario no ha sido añadido a la actividad");
		
	}
	
	@Test
	public void testBookActivityErrorOverlapping(){
		Assert.notNull(null, "PreTest - testBookActivityErrorOverlapping no está hecho ! ! !");
	}
	
	@Test
	public void testBookActivityErrorDeleted(){
		Assert.notNull(null, "PreTest - testBookActivityErrorDeleted no está hecho ! ! !");
	}
	
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testBookActivityErrorSeats() throws InvalidPreTestException{
		Activity activity;
		Customer custo;
		Collection<Customer> customers;
		Collection<Gym> gyms;
		
		authenticate("admin");
		
		/**
		 * Ignorar este párrafo comentado
		 */
		// quito el customer 65 de la actividad 59
		// cambiando la actividad 59 a un asiento
//		Room tempRoom;
//		activity = activityService.findOne(59);
//		
//		customers = activity.getCustomers();
//		customers.remove(customerRepository.findOne(65));
//		activity.setCustomers(customers);
//		activity.setNumberOfSeatsAvailable(1);	
//		activityService.save(activity);
	
	
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
				
				isAcceptable = ! activityService.compruebaOverlappingCustomer(a);
				isAcceptable = isAcceptable && a.getNumberOfSeatsAvailable() - a.getCustomers().size() < 1;
				
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
			Assert.isTrue(activityService.findAllByCustomer().contains(activity), "PreTest - La actividad no está asociada a un gimnasio con pago activo");
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
		
		Assert.isTrue(! activity.getCustomers().contains(custo), "El usuario ha sido añadido a la actividad y no debería haber sido así");		
	}
	
	@Test
	public void testBookActivityErrorInPast(){
		Activity activity;
		Customer custo;
		Collection<Customer> customers;
		
		// Carga de objetos
		authenticate("customer4"); // <-- CAMBIAR !!! #76
		custo = customerService.findByPrincipal();
		
		activity = activityService.findOne(56); // <-- CAMBIAR !!! #76
		
		// Comprobaciones básicas de requisitos
		Assert.isTrue(activity.getStartingMoment().before(new Date()), "PreTest - La actividad tiene un startingMoment en el futuro y se quiere probar lo contrario");
		Assert.isTrue(activity.getNumberOfSeatsAvailable() - activity.getCustomers().size() > 0 , "PreTest - La actividad no tiene asientos disponibles");
		Assert.isTrue(activityService.findAllByCustomer().contains(activity), "PreTest - La actividad no está asociada a un gimnasio con pago activo");
		
		// Ejecución del test
		customers = activity.getCustomers();
		customers.add(custo);
		
		activity.setCustomers(customers);
		activity = activityService.save(activity);
		
		// Comprobación resultados 
		custo = customerService.findByPrincipal();
		
		Assert.isTrue(! activity.getCustomers().contains(custo), "El usuario ha sido añadido a la actividad y no debería haber sido así");			
	}
	
	@Test
	public void testBookActivityErrorGymNotActivePaid(){
		Activity activity;
		Customer custo;
		Collection<Customer> customers;
		
		// Carga de objetos
		authenticate("customer4");
		custo = customerService.findByPrincipal();
		
		activity = activityService.findOne(56);
		
		// Comprobaciones básicas de requisitos
		Assert.isTrue(activity.getStartingMoment().after(new Date()), "PreTest - La actividad tiene un startingMoment en el pasado");
		Assert.isTrue(activity.getNumberOfSeatsAvailable() - activity.getCustomers().size() > 0 , "PreTest - La actividad no tiene asientos disponibles");
		Assert.isTrue(!activityService.findAllByCustomer().contains(activity), "PreTest - La actividad está asociada a un gimnasio con pago activo");
		
		// Ejecución del test
		customers = activity.getCustomers();
		customers.add(custo);
		
		activity.setCustomers(customers);
		activity = activityService.save(activity);
		
		// Comprobación resultados 
		custo = customerService.findByPrincipal();
		
		Assert.isTrue(! activity.getCustomers().contains(custo), "El usuario ha sido añadido a la actividad y no debería haber sido así");			
	}	
}
