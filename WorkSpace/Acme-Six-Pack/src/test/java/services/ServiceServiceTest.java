package services;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Gym;
import domain.ServiceEntity;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ServiceServiceTest extends AbstractTest{

	// Pendientes a probar ! ! ! ! ! ! - - - - - - - - - - - 
	
		// gymService.findAllWithFeePaymentActive()
	
	// Service under test -------------------------
	
	@Autowired
	private ServiceService serviceService;
	
	@Autowired
	private GymService gymService;
	
	// Test ---------------------------------------
	
	/**
	 * Description: A user who is authenticated as a customer must be able to list the catalogue of gyms and navigate to the services that they offer.
	 * Precondition: -
	 * Return: TRUE
	 * Postcondition: All services are shown.
	 */
	@Test
	public void testFindAllServices1(){
		
		Collection<ServiceEntity> all;
		
		all = serviceService.findAll();
		
		Assert.isTrue(all.size() == 4);
		
		serviceService.flush();
	}

	/**
	 * Description: A user who is authenticated as a customer must be able to list the catalogue of gyms and navigate to the services that they offer.
	 * Precondition: The gym selected must be a gym from the system.
	 * Return: TRUE
	 * Postcondition: All services from a gym are shown.
	 */
	@Test
	public void testFindAllServiceByGym1(){
		
		Collection<ServiceEntity> all;
		Collection<Gym> allGyms;
		Gym gym = null;
		
		allGyms = gymService.findAll();
		
		for(Gym i:allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		all = serviceService.findAllByGym(gym.getId());
		
		Assert.isTrue(all.size() == 2);
		
		serviceService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to list the catalogue of services and navigate to the gyms that offer them.
	 * Precondition: -
	 * Return: TRUE
	 * Postcondition: All services from a gym are shown.
	 */
	@Test
	public void testFindAllServiceAndGym1(){
		
		Collection<ServiceEntity> all;
		ServiceEntity service = null;
		Collection<Gym> allGymsByService;
		
		all = serviceService.findAll();
		
		for(ServiceEntity i:all){
			if(i.getName().equals("Karate")){
				service = i;
			}
		}
		
		allGymsByService = gymService.findAllByService(service.getId());
		
		Assert.isTrue(allGymsByService.size() == 1);
		
		serviceService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to list the catalogue of gyms and navigate to the services that they offer.
	 * Precondition: The user is a customer.
	 * Return: TRUE
	 * Postcondition: All services are shown.
	 */
	@Test
	public void testFindAllServicesCustomer1(){
		
		Collection<ServiceEntity> all;
		
		authenticate("customer1");
		
		all = serviceService.findAll();
		
		Assert.isTrue(all.size() == 4);
		
		authenticate(null);
		serviceService.flush();
	}

	/**
	 * Description: A user who is authenticated as a customer must be able to list the catalogue of gyms and navigate to the services that they offer.
	 * Precondition: The user is a customer. The gym selected must be a gym from the system.
	 * Return: TRUE
	 * Postcondition: All services from a gym are shown.
	 */
	@Test
	public void testFindAllServiceByGymCustomer1(){
		
		Collection<ServiceEntity> all;
		Collection<Gym> allGyms;
		Gym gym = null;
		
		authenticate("customer1");
		
		allGyms = gymService.findAll();
		
		for(Gym i:allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		all = serviceService.findAllByGym(gym.getId());
		
		Assert.isTrue(all.size() == 2);
		
		authenticate(null);
		serviceService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to list the catalogue of services and navigate to the gyms that offer them.
	 * Precondition: The user is a customer.
	 * Return: TRUE
	 * Postcondition: All services from a gym are shown.
	 */
	@Test
	public void testFindAllServiceAndGymCustomer1(){
		
		Collection<ServiceEntity> all;
		ServiceEntity service = null;
		Collection<Gym> allGymsByService;
		
		authenticate("customer1");
		
		all = serviceService.findAll();
		
		for(ServiceEntity i:all){
			if(i.getName().equals("Karate")){
				service = i;
			}
		}
		
		allGymsByService = gymService.findAllByService(service.getId());
		
		Assert.isTrue(allGymsByService.size() == 1);
		
		authenticate(null);
		serviceService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to list the catalogue of gyms and navigate to the services that they offer.
	 * Precondition: The user is an admin.
	 * Return: TRUE
	 * Postcondition: All services are shown.
	 */
	@Test
	public void testFindAllServicesAdmin1(){
		
		Collection<ServiceEntity> all;
		
		authenticate("admin");
		
		all = serviceService.findAll();
		
		Assert.isTrue(all.size() == 4);
		
		authenticate(null);
		serviceService.flush();
	}

	/**
	 * Description: A user who is authenticated as a customer must be able to list the catalogue of gyms and navigate to the services that they offer.
	 * Precondition: The user is an admin. The gym selected must be a gym from the system.
	 * Return: TRUE
	 * Postcondition: All services from a gym are shown.
	 */
	@Test
	public void testFindAllServiceByGymAdmin1(){
		
		Collection<ServiceEntity> all;
		Collection<Gym> allGyms;
		Gym gym = null;
		
		authenticate("admin");
		
		allGyms = gymService.findAll();
		
		for(Gym i:allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		all = serviceService.findAllByGym(gym.getId());
		
		Assert.isTrue(all.size() == 2);
		
		authenticate(null);
		serviceService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to list the catalogue of services and navigate to the gyms that offer them.
	 * Precondition: The user is an admin.
	 * Return: TRUE
	 * Postcondition: All services from a gym are shown.
	 */
	@Test
	public void testFindAllServiceAndGymAdmin1(){
		
		Collection<ServiceEntity> all;
		ServiceEntity service = null;
		Collection<Gym> allGymsByService;
		
		authenticate("admin");
		
		all = serviceService.findAll();
		
		for(ServiceEntity i:all){
			if(i.getName().equals("Karate")){
				service = i;
			}
		}
		
		allGymsByService = gymService.findAllByService(service.getId());
		
		Assert.isTrue(allGymsByService.size() == 1);
		
		authenticate(null);
		serviceService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to list the catalogue of gyms and navigate to the services that they offer.
	 * Precondition: The user is an trainer.
	 * Return: TRUE
	 * Postcondition: All services are shown.
	 */
	@Test
	public void testFindAllServicesTrainer1(){
		
		Collection<ServiceEntity> all;
		
		authenticate("trainer1");
		
		all = serviceService.findAll();
		
		Assert.isTrue(all.size() == 4);
		
		authenticate(null);
		serviceService.flush();
	}

	/**
	 * Description: A user who is authenticated as a customer must be able to list the catalogue of gyms and navigate to the services that they offer.
	 * Precondition: The user is an trainer. The gym selected must be a gym from the system.
	 * Return: TRUE
	 * Postcondition: All services from a gym are shown.
	 */
	@Test
	public void testFindAllServiceByGymTrainer1(){
		
		Collection<ServiceEntity> all;
		Collection<Gym> allGyms;
		Gym gym = null;
		
		authenticate("trainer1");
		
		allGyms = gymService.findAll();
		
		for(Gym i:allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		all = serviceService.findAllByGym(gym.getId());
		
		Assert.isTrue(all.size() == 2);
		
		authenticate(null);
		serviceService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to list the catalogue of services and navigate to the gyms that offer them.
	 * Precondition: The user is an trainer.
	 * Return: TRUE
	 * Postcondition: All services from a gym are shown.
	 */
	@Test
	public void testFindAllServiceAndGymTrainer1(){
		
		Collection<ServiceEntity> all;
		ServiceEntity service = null;
		Collection<Gym> allGymsByService;
		
		authenticate("trainer1");
		
		all = serviceService.findAll();
		
		for(ServiceEntity i:all){
			if(i.getName().equals("Karate")){
				service = i;
			}
		}
		
		allGymsByService = gymService.findAllByService(service.getId());
		
		Assert.isTrue(allGymsByService.size() == 1);
		
		authenticate(null);
		serviceService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the services, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The data is valid.
	 * Return: TRUE
	 * Postcondition: The service is created.
	 */
	@Test
	public void testCreateServiceAdmin1(){
		
		Collection<ServiceEntity> all;
		ServiceEntity service;
		
		authenticate("admin");
		
		all = serviceService.findAll();
		Assert.isTrue(all.size() == 4);
		
		service = serviceService.create();
		service.setName("Nuevo service");
		service.setDescription("Nueva descripción");
		serviceService.saveToEdit(service);
		
		all = serviceService.findAll();
		Assert.isTrue(all.size() == 5);
		
		authenticate(null);
		serviceService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the services, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is not an admin. The data is valid.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateServiceAdmin2(){
		
		Collection<ServiceEntity> all;
		ServiceEntity service;
		
		all = serviceService.findAll();
		Assert.isTrue(all.size() == 4);
		
		service = serviceService.create();
		service.setName("Nuevo service");
		service.setDescription("Nueva descripción");
		serviceService.saveToEdit(service);
		
		all = serviceService.findAll();
		Assert.isTrue(all.size() == 5);
		
		serviceService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the services, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The data is not valid.
	 * Return: FALSE
	 * Postcondition: -
	 * Notes: This must be done with every attribute.
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateServiceAdmin3(){
		
		Collection<ServiceEntity> all;
		ServiceEntity service;
		
		authenticate("admin");
		
		all = serviceService.findAll();
		Assert.isTrue(all.size() == 4);
		
		service = serviceService.create();
//		service.setName("Nuevo service");
//		service.setDescription("Nueva descripción");
		serviceService.saveToEdit(service);
		
		all = serviceService.findAll();
		Assert.isTrue(all.size() == 5);
		
		authenticate(null);
		serviceService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the services, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The data is valid.
	 * Return: TRUE
	 * Postcondition: The service is modify.
	 */
	@Test
	public void testModifyServiceAdmin1(){
		
		Collection<ServiceEntity> all;
		ServiceEntity serviceToModify = null;
		
		authenticate("admin");
		
		all = serviceService.findAll();
		
		for(ServiceEntity i:all){
			if(i.getName().equals("Karate")){
				serviceToModify = i;
				break;
			}
		}
		
		serviceToModify.setName("Judo");
		serviceService.saveToEdit(serviceToModify);
		
		all = serviceService.findAll();
		
		for(ServiceEntity i:all){
			if(i.getName().equals("Judo")){
				serviceToModify = i;
				break;
			}
		}
		
		Assert.isTrue(serviceToModify.getName().equals("Judo"));
		
		authenticate(null);
		serviceService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the services, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is not an admin. The data is valid.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testModifyServiceAdmin2(){
		
		Collection<ServiceEntity> all;
		ServiceEntity serviceToModify = null;
				
		all = serviceService.findAll();
		
		for(ServiceEntity i:all){
			if(i.getName().equals("Karate")){
				serviceToModify = i;
				break;
			}
		}
		
		serviceToModify.setName("Judo");
		serviceService.saveToEdit(serviceToModify);
		
		all = serviceService.findAll();
		
		for(ServiceEntity i:all){
			if(i.getName().equals("Judo")){
				serviceToModify = i;
				break;
			}
		}
		
		Assert.isTrue(serviceToModify.getName().equals("Judo"));
		
		serviceService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the services, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The service has no activities attached.
	 * Return: TRUE
	 * Postcondition: The service is deleted.
	 */
	@Test
	public void testDeleteServiceAdmin1(){
		
		Collection<ServiceEntity> all;
		ServiceEntity service;
		
		authenticate("admin");
		
		all = serviceService.findAll();
		Assert.isTrue(all.size() == 4);
		
		service = serviceService.create();
		service.setName("Nuevo service");
		service.setDescription("Nueva descripción");
		serviceService.saveToEdit(service);
		
		all = serviceService.findAll();
		Assert.isTrue(all.size() == 5);
		
		for(ServiceEntity i:all){
			if(i.getName().equals("Nuevo service")){
				service = i;
				break;
			}
		}
		
		serviceService.delete(service);
		
		all = serviceService.findAll();
		Assert.isTrue(all.size() == 4);
		
		authenticate(null);
		serviceService.flush();
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteServiceAdmin2(){
		
		Collection<ServiceEntity> all;
		ServiceEntity service;
				
		all = serviceService.findAll();
		Assert.isTrue(all.size() == 4);
		
		service = serviceService.create();
		service.setName("Nuevo service");
		service.setDescription("Nueva descripción");
		serviceService.saveToEdit(service);
		
		all = serviceService.findAll();
		Assert.isTrue(all.size() == 5);
		
		for(ServiceEntity i:all){
			if(i.getName().equals("Nuevo service")){
				service = i;
				break;
			}
		}
		
		serviceService.delete(service);
		
		all = serviceService.findAll();
		Assert.isTrue(all.size() == 4);
		
		serviceService.flush();
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteServiceAdmin3(){
		
		Collection<ServiceEntity> all;
		ServiceEntity service = null;
		
		authenticate("admin");
				
		all = serviceService.findAll();
		Assert.isTrue(all.size() == 4);
		
		for(ServiceEntity i:all){
			if(i.getName().equals("Karate")){
				service = i;
				break;
			}
		}
		
		serviceService.delete(service);
		
		all = serviceService.findAll();
		Assert.isTrue(all.size() == 4);
		
		authenticate(null);
		serviceService.flush();
		
	}
}
