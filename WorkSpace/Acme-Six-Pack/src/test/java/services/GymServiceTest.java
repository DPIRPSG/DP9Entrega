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

import domain.Activity;
import domain.Gym;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class GymServiceTest extends AbstractTest{

	// Pendientes a probar ! ! ! ! ! ! - - - - - - - - - - - 
	
		// gymService.findAllWithFeePaymentActive()
	
	// Service under test -------------------------
	
	@Autowired
	private GymService gymService;
	
	@Autowired
	private ActivityService activityService;
	
	// Test ---------------------------------------
	
	/**
	 * TEST POSITVO
	 * Description: A user who is not authenticated must be able to navigate (list) through the gyms.
	 * Precondition: -
	 * Return: TRUE
  	 * Postcondition: All gyms in system are shown.
	 */
	@Test
	public void testFindAllGyms1(){
		
		Collection<Gym> all;
		
		all = gymService.findAll();
		Assert.isTrue(all.size() == 3);
		
		gymService.flush();
		
	}
	
	@Test
	public void testFindAllGymsAndActivityPlan1(){
		
		Collection<Gym> all;
		Gym gym = null;
		Collection<Activity> allActivities;
		
		all = gymService.findAll();
		
		for(Gym i:all){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
			}
		}
		
		allActivities = activityService.findAllActivesByGymId(gym.getId());
		
		Assert.isTrue(allActivities.size() == 5);
	}
		
	/**
	 * TEST POSITIVO
	 * Description: A user who is authenticated as a customer must be able to list the gyms in which he or she has an active fee payment and navigate to the details of the corresponding fee payments.
	 * Precondition: The user is a customer.
	 * Return: TRUE
	 * Postcondition: All gyms in which the customer has a paid fee.
	 */
	@Test
	public void testPaidGyms1(){
		
		Collection<Gym> all;
		
		authenticate("customer1");
		
		all = gymService.findAllWithFeePaymentActive();
		Assert.isTrue(all.size() == 1);
		
		authenticate(null);
		gymService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to list the gyms in which he or she has an active fee payment and navigate to the details of the corresponding fee payments.
	 * Precondition: The user is not a customer.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testPaidGyms2(){
		
		Collection<Gym> all;
				
		all = gymService.findAllWithFeePaymentActive();
		Assert.isTrue(all.size() == 1);
		
		gymService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the gyms, which includes listing, creating, modifying, or deleting them.
	 * Precondition: The user is an admin
	 * Return: TRUE
	 * Postcondition: All gyms of the system are shown. 
	 */
	@Test
	public void testFindAllGymsAdmin1(){
		
		Collection<Gym> all;
		
		authenticate("admin");
		
		all = gymService.findAll();
		Assert.isTrue(all.size() == 3);
		
		authenticate(null);
		gymService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the gyms, which includes listing, creating, modifying, or deleting them.
	 * Precondition: The user is an admin. The data of the new gym must be valid.
	 * Return: TRUE
	 * Postcondition: A new gym is creted.
	 */
	@Test
	public void testCreateGymAdmin1(){
		
		Collection<Gym> all;
		Gym newGym;
		
		authenticate("admin");
		
		all = gymService.findAll();
		Assert.isTrue(all.size() == 3);
		
		newGym = gymService.create();
		newGym.setName("Nuevo Gym");
		newGym.setDescription("Este es el nuevo gym");
		newGym.setPostalAddress("41001");
		newGym.setPhone("954954954");
		newGym.setFee(20.0);
		
		gymService.saveToEdit(newGym);
		
		all = gymService.findAll();
		Assert.isTrue(all.size() == 4);
		
		authenticate(null);
		gymService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the gyms, which includes listing, creating, modifying, or deleting them.
	 * Precondition: The user is not an admin. The data of the new gym must be valid.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateGymAdmin2(){
		
		Collection<Gym> all;
		Gym newGym;
				
		all = gymService.findAll();
		Assert.isTrue(all.size() == 3);
		
		newGym = gymService.create();
		newGym.setName("Nuevo Gym");
		newGym.setDescription("Este es el nuevo gym");
		newGym.setPostalAddress("41001");
		newGym.setPhone("954954954");
		newGym.setFee(20.0);
		
		gymService.saveToEdit(newGym);
		
		all = gymService.findAll();
		Assert.isTrue(all.size() == 4);
		
		gymService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the gyms, which includes listing, creating, modifying, or deleting them.
	 * Precondition: The user is an admin. The data of the new gym must not be valid.
	 * Return: FALSE
	 * Postcondition: -
	 * Notes: This test must be done for each data of the new gym.
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateGymAdmin3(){
		
		Collection<Gym> all;
		Gym newGym;
		
		authenticate("admin");
		
		all = gymService.findAll();
		Assert.isTrue(all.size() == 3);
		
		newGym = gymService.create();
//		newGym.setName("Nuevo Gym");
//		newGym.setDescription("Este es el nuevo gym");
//		newGym.setPostalAddress("41001");
//		newGym.setPhone("954954954");
//		newGym.setFee(20.0);
		
		gymService.saveToEdit(newGym);
		
		all = gymService.findAll();
		Assert.isTrue(all.size() == 4);
		
		authenticate(null);
		gymService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the gyms, which includes listing, creating, modifying, or deleting them.
	 * Precondition: The user is an admin. The data of the new gym must not be valid.
	 * Return: FALSE
	 * Postcondition: -
	 * Notes: This test must be done for each data of the new gym.
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateGymAdmin4(){
		
		Collection<Gym> all;
		Gym newGym;
		
		authenticate("admin");
		
		all = gymService.findAll();
		Assert.isTrue(all.size() == 3);
		
		newGym = gymService.create();
		newGym.setName("Nuevo Gym");
		newGym.setDescription("Este es el nuevo gym");
		newGym.setPostalAddress("41001");
		newGym.setPhone("954954954");
		newGym.setFee(-20.0);
		
		gymService.saveToEdit(newGym);
		
		all = gymService.findAll();
		Assert.isTrue(all.size() == 4);
		
		authenticate(null);
		gymService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the gyms, which includes listing, creating, modifying, or deleting them.
	 * Precondition: The user is an admin. The data use to modify the gym must be valid.
	 * Return: TRUE
	 * Postcondition: A gym is modify.
	 */
	@Test
	public void testModifyGym1(){
		
		Collection<Gym> all;
		Gym gym = null;
		
		authenticate("admin");
		
		all = gymService.findAll();
		
		for(Gym i:all){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		gym.setFee(30.0);
		gymService.saveToEdit(gym);
		
		all = gymService.findAll();
		
		for(Gym i:all){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		Assert.isTrue(gym.getFee() == 30.0);
		
		authenticate(null);
		gymService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the gyms, which includes listing, creating, modifying, or deleting them.
	 * Precondition: The user is not an admin. The data use to modify the gym must be valid.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testModifyGym2(){
		
		Collection<Gym> all;
		Gym gym = null;
				
		all = gymService.findAll();
		
		for(Gym i:all){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		gym.setFee(30.0);
		gymService.saveToEdit(gym);
		
		all = gymService.findAll();
		
		for(Gym i:all){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		Assert.isTrue(gym.getFee() == 30.0);
		
		gymService.flush();
	}

	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the gyms, which includes listing, creating, modifying, or deleting them.
	 * Precondition: The user is an admin. The data use to modify the gym must not be valid.
	 * Return: FALSE
	 * Postcondition: -
	 * Notes: This test must be done for each data of the modify gym.
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value = true)
	public void testModifyGym3(){
		
		Collection<Gym> all;
		Gym gym = null;
		
		authenticate("admin");
		
		all = gymService.findAll();
		
		for(Gym i:all){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		gym.setFee(-20);
		gymService.saveToEdit(gym);
		
		authenticate(null);
		gymService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the gyms, which includes listing, creating, modifying, or deleting them.
	 * Precondition: The user is an admin. The gym to delete must be a gym of the system.
	 * Return: TRUE
	 * Postcondition: The gym is deleted.
	 */
	@Test
	public void testDeleteGym1(){
		
		Collection<Gym> all;
		Gym newGym;
		
		authenticate("admin");
		
		all = gymService.findAll();
		
		Assert.isTrue(all.size() == 3);
		
		newGym = gymService.create();
		newGym.setName("Nuevo Gym");
		newGym.setDescription("Este es el nuevo gym");
		newGym.setPostalAddress("41001");
		newGym.setPhone("954954954");
		newGym.setFee(20.0);
		
		gymService.saveToEdit(newGym);
		
		all = gymService.findAll();
		Assert.isTrue(all.size() == 4);
		
		for(Gym i:all){
			if(i.getName().equals("Nuevo Gym")){
				newGym = i;
				break;
			}
		}
		
		gymService.delete(newGym);
		
		all = gymService.findAll();
		Assert.isTrue(all.size() == 3);
		
		authenticate(null);
		gymService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the gyms, which includes listing, creating, modifying, or deleting them.
	 * Precondition: The user is not an admin. The gym to delete must be a gym of the system.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteGym2(){
		
		Collection<Gym> all;
		Gym newGym;
				
		all = gymService.findAll();
		
		Assert.isTrue(all.size() == 3);
		
		newGym = gymService.create();
		newGym.setName("Nuevo Gym");
		newGym.setDescription("Este es el nuevo gym");
		newGym.setPostalAddress("41001");
		newGym.setPhone("954954954");
		newGym.setFee(20.0);
		
		gymService.saveToEdit(newGym);
		
		all = gymService.findAll();
		Assert.isTrue(all.size() == 4);
		
		for(Gym i:all){
			if(i.getName().equals("Nuevo Gym")){
				newGym = i;
				break;
			}
		}
		
		gymService.delete(newGym);
		
		all = gymService.findAll();
		Assert.isTrue(all.size() == 3);
		
		gymService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the gyms, which includes listing, creating, modifying, or deleting them.
	 * Precondition: The user is an admin. The gym to deleted could not be deleted due to constraints.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteGym3(){
		
		Collection<Gym> all;
		Gym gym = null;
		
		authenticate("admin");
		
		all = gymService.findAll();
		
		Assert.isTrue(all.size() == 3);
		
		for(Gym i:all){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		gymService.delete(gym);
		
		all = gymService.findAll();
		
		Assert.isTrue(all.size() == 2);
		
		authenticate(null);
		gymService.flush();
	}
	
}
