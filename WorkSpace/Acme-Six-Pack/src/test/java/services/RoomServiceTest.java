package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import domain.Gym;
import domain.Room;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class RoomServiceTest extends AbstractTest{
	
	// Service under test -------------------------
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private GymService gymService;
	
	// Test ---------------------------------------
	
	/**
	 * TEST POSITVO
	 * Description: A user who is not authenticated must be able to navigate (list) throught the rooms of a gym selected.
	 * Precondition: The gym selected must be a gym of the system.
	 * Return: TRUE
  	 * Postcondition: All rooms of a gym previously selected are shown.
	 */
	@Test
	public void testFindAllRoomsByGym1(){
		
		Collection<Room> all;
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
		
		all = roomService.findAllByGymId(gym.getId());
		
		Assert.isTrue(all.size() == 3);
		
		gymService.flush();
		roomService.flush();
		
	}
	
	/**
	 * Test NEGATIVO
	 * Description: A user who is not authenticated must be able to navigate (list) throught the bulletin boards of the gym selected.
	 * Precondition: The gym selected must not be a gym of the system.
	 * Return: FALSE
	 * Postcondition: Nothing is shown.
	 */
	@Test
	public void testFindAllRoomsByGym2(){
		
		Collection<Room> all;
		
		all = roomService.findAllByGymId(1000);
		
		Assert.isTrue(all.size() == 0);
		
		roomService.flush();
		
	}

}
