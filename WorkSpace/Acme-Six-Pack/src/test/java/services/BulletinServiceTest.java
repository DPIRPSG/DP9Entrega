package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Bulletin;
import domain.Gym;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class BulletinServiceTest extends AbstractTest{

	// Service under test -------------------------

	@Autowired
	private BulletinService bulletinService;
	
	@Autowired
	private GymService gymService;
	
	// Test ---------------------------------------
	
	/**
	 * TEST POSITVO
	 * Description: A user who is not authenticated must be able to navigate (list) through the bulletin boards of the gym selected.
	 * Precondition: The gym selected must be a gym of the system.
	 * Return: TRUE
  	 * Postcondition: All bulletin of the gym previosly selected are shown.
	 */
	@Test
	public void testFindAllBulletinsByGym1(){
		
		Collection<Bulletin> all;
		Collection<Gym> allGyms;
		Gym gym = null;
		Double counter = 1.0;
		
		allGyms = gymService.findAll();
		
		for(Gym i: allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				counter = 0.0;
				break;
			}
		}
		
		Assert.isTrue(counter == 0.0, "There wasn't any gym named as specified in the test");
		Assert.notNull(gym);
		
		all = bulletinService.findAllByGymId(gym.getId());
		
		Assert.isTrue(all.size() == 3);
		
		bulletinService.flush();
		
	}
	
	/**
	 * Test NEGATIVO
	 * Description: A user who is not authenticated must be able to navigate (list) through the bulletin boards of the gym selected.
	 * Precondition: The gym selected must not be a gym of the system.
	 * Return: FALSE
	 * Postcondition: Nothing is shown.
	 */
	@Test
	public void testFindAllBulletinsByGym2(){
		
		Collection<Bulletin> all;
		
		all = bulletinService.findAllByGymId(1000);
		
		Assert.isTrue(all.size() == 0);
		
		bulletinService.flush();
		
	}
	
	/**
	 * Test POSITIVO
	 * Description: A user who is not authenticated must be able to search for the bulletins that contain a given key word in their titles or descriptions.
	 * Precondition: The gym selected must be a gym of the system. The given key word is found in a title.
	 * Return: TRUE
	 * Postcondition: All bulletin, that contains the given key word in its title or description, of the gym previously selected are shown.
	 */
	@Test
	public void testFindAllBulletinsByGivenKeyword1(){
		
		Collection<Bulletin> all;
		Collection<Gym> allGyms;
		Gym gym = null;
		Double counter = 0.0;
		
		allGyms = gymService.findAll();
		
		for(Gym i: allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				counter = 0.0;
				break;
			}
		}
		
		Assert.isTrue(counter == 0.0, "There wasn't any gym named as specified in the test");
		Assert.notNull(gym);
		
		all = bulletinService.findBySingleKeyword("empleados", gym.getId());
		
		Assert.isTrue(all.size() == 1);
		
		bulletinService.flush();
		
	}

	/**
	 * Test POSITIVO
	 * Description: A user who is not authenticated must be able to search for the bulletins that contain a given key word in their titles or descriptions.
	 * Precondition: The gym selected must be a gym of the system. The given key word is found in a title.
	 * Return: TRUE
	 * Postcondition: All bulletin, that contains the given key word in its title or description, of the gym previously selected are shown.
	 */
	@Test
	public void testFindAllBulletinsByGivenKeyword2(){
		
		Collection<Bulletin> all;
		Collection<Gym> allGyms;
		Gym gym = null;
		Double counter = 0.0;
		
		allGyms = gymService.findAll();
		
		for(Gym i: allGyms){
			if(i.getName().equals("Gym Cádiz")){
				gym = i;
				counter = 0.0;
				break;
			}
		}
		
		Assert.isTrue(counter == 0.0, "There wasn't any gym named as specified in the test");
		Assert.notNull(gym);
		
		all = bulletinService.findBySingleKeyword("empleados", gym.getId());
		
		Assert.isTrue(all.size() == 1);
		
		bulletinService.flush();
		
	}
	
	/**
	 * Test POSITIVO
	 * Description: A user who is not authenticated must be able to search for the bulletins that contain a given key word in their titles or descriptions.
	 * Precondition: The gym selected must be a gym of the system. The given key word is found in a title.
	 * Return: TRUE
	 * Postcondition: All bulletin, that contains the given key word in its title or description, of the gym previously selected are shown.
	 */
	@Test
	public void testFindAllBulletinsByGivenKeyword3(){
		
		Collection<Bulletin> all;
		Collection<Gym> allGyms;
		Gym gym = null;
		Double counter = 0.0;
		
		allGyms = gymService.findAll();
		
		for(Gym i: allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				counter = 0.0;
				break;
			}
		}
		
		Assert.isTrue(counter == 0.0, "There wasn't any gym named as specified in the test");
		Assert.notNull(gym);
		
		all = bulletinService.findBySingleKeyword("Mañana", gym.getId());
		
		Assert.isTrue(all.size() == 2);
		
		bulletinService.flush();
		
	}
	
	/**
	 * Test POSITIVO
	 * Description: A user who is not authenticated must be able to search for the bulletins that contain a given key word in their titles or descriptions.
	 * Precondition: The given key word is not found anywhere.
	 * Return: TRUE
	 * Postcondition: The given key word is not found anywhere.
	 */
	@Test
	public void testFindAllBulletinsByGivenKeyword4(){
		
		Collection<Bulletin> all;
		Collection<Gym> allGyms;
		Gym gym = null;
		Double counter = 0.0;
		
		allGyms = gymService.findAll();
		
		for(Gym i: allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				counter = 0.0;
				break;
			}
		}
		
		Assert.isTrue(counter == 0.0, "There wasn't any gym named as specified in the test");
		Assert.notNull(gym);
		
		all = bulletinService.findBySingleKeyword("Peregrinaje", gym.getId());
		
		Assert.isTrue(all.size() == 0);
		
		bulletinService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the bulletins of a gym, which includes listing and creating them. Bulletins cannot be modified or deleted. Before publishing a bulletin, the system must ask for confirmation.
	 * Precondition: The user is an admin. The selected gym must be a gym of the system.
	 * Return: TRUE
	 * Postcondition: All bulletin of the selected gym are shown.
	 */
	@Test
	public void testFindAllBulletinsByGymAdmin1(){
		
		Collection<Bulletin> all;
		Collection<Gym> allGyms;
		Gym gym = null;
		Double counter = 1.0;
		
		authenticate("admin");
		
		allGyms = gymService.findAll();
		
		for(Gym i: allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				counter = 0.0;
				break;
			}
		}
		
		Assert.isTrue(counter == 0.0, "There wasn't any gym named as specified in the test");
		Assert.notNull(gym);
		
		all = bulletinService.findAllByGymId(gym.getId());
		
		Assert.isTrue(all.size() == 3);
		
		authenticate(null);
		bulletinService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the bulletins of a gym, which includes listing and creating them. Bulletins cannot be modified or deleted. Before publishing a bulletin, the system must ask for confirmation.
	 * Precondition: The user is an admin. The selected gym must not be a gym of the system.
	 * Return: FALSE
	 * Postcondition: - 
	 */
	@Test
	public void testFindAllBulletinsByGymAdmin2(){
		
		Collection<Bulletin> all;
		
		all = bulletinService.findAllByGymId(1000);
		
		Assert.isTrue(all.size() == 0);
		
		bulletinService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the bulletins of a gym, which includes listing and creating them. Bulletins cannot be modified or deleted. Before publishing a bulletin, the system must ask for confirmation.
	 * Precondition: The user is an admin. The data of the new bulletin must be valid. The selected gym must be a gym of the system.
	 * Return: TRUE
	 * Postcondition: A new bulletin is created.
	 */
	@Test
	public void testCreateBulletin1(){
		
		Collection<Bulletin> all;
		Bulletin newBulletin = null;
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
		
		all = bulletinService.findAllByGymId(gym.getId());
		Assert.isTrue(all.size() == 3);
		
		newBulletin = bulletinService.create(gym.getId());
		newBulletin.setTitle("Nuevo bulletin");
		newBulletin.setDescription("Este es un nuevo bulletin");
		bulletinService.save(newBulletin);
		
		all = bulletinService.findAllByGymId(gym.getId());
		Assert.isTrue(all.size() == 4);
		
		authenticate(null);
		bulletinService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the bulletins of a gym, which includes listing and creating them. Bulletins cannot be modified or deleted. Before publishing a bulletin, the system must ask for confirmation.
	 * Precondition: The user is not an admin. The data of the new room must be valid. The selected gym must be a gym of the system.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateBulletin2(){
	
		Collection<Bulletin> all;
		Bulletin newBulletin = null;
		Collection<Gym> allGyms;
		Gym gym = null;
				
		allGyms = gymService.findAll();
		
		for(Gym i:allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		all = bulletinService.findAllByGymId(gym.getId());
		Assert.isTrue(all.size() == 3);
		
		newBulletin = bulletinService.create(gym.getId());
		newBulletin.setTitle("Nuevo bulletin");
		newBulletin.setDescription("Este es un nuevo bulletin");
		bulletinService.save(newBulletin);
		
		all = bulletinService.findAllByGymId(gym.getId());
		Assert.isTrue(all.size() == 4);
		
		bulletinService.flush();
	}
}
