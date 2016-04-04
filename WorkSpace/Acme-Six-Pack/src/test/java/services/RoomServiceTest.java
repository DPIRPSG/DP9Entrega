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
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the rooms of a gym, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The selected gym must be a gym of the system.
	 * Return: TRUE
	 * Postcondition: All room of the selected gym are shown.
	 */
	@Test
	public void testFindAllRoomsByGym3(){
		
		Collection<Room> all;
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
		
		all = roomService.findAllByGymId(gym.getId());
		
		Assert.isTrue(all.size() == 3);
		
		authenticate(null);
		gymService.flush();
		roomService.flush();
		
	}
	
	/**
	 * Test NEGATIVO
	 * Description: A user who is authenticated as an administrator must be able to manage the rooms of a gym, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The selected gym must not be a gym of the system.
	 * Return: FALSE
	 * Postcondition: - 
	 */
	@Test
	public void testFindAllRoomsByGym4(){
		
		Collection<Room> all;
		
		authenticate("admin");
		
		all = roomService.findAllByGymId(1000);
		
		Assert.isTrue(all.size() == 0);
		
		authenticate(null);
		roomService.flush();
		
	}
	
	/**
	 * TEST POSITIVO
	 * Description: A user who is authenticated as an administrator must be able to manage the rooms of a gym, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The data of the new room must be valid. The selected gym must be a gym of the system.
	 * Return: TRUE
	 * Postcondition: A new room is created.
	 */
	@Test
	public void testCreateRoomAdmin1(){
		
		Collection<Room> all;
		Room newRoom;
		Collection<Gym> allGyms;
		Gym gym = null;
		
		authenticate("admin");
		
		all = roomService.findAll();
		Assert.isTrue(all.size() == 5);
		
		allGyms = gymService.findAll();
		
		for(Gym i:allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		newRoom = roomService.create(gym.getId());
		newRoom.setName("Nueva habitación");
		newRoom.setDescription("Esta es la nueva descripción");
		newRoom.setNumberOfSeats(20);
		roomService.saveToEdit(newRoom);

		all = roomService.findAll();
		Assert.isTrue(all.size() == 6);
		
		authenticate(null);
		roomService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the rooms of a gym, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is not an admin. The data of the new room must be valid. The selected gym must be a gym of the system.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateRoomAdmin2(){
		
		Collection<Room> all;
		Room newRoom;
		Collection<Gym> allGyms;
		Gym gym = null;
				
		all = roomService.findAll();
		Assert.isTrue(all.size() == 5);
		
		allGyms = gymService.findAll();
		
		for(Gym i:allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		newRoom = roomService.create(gym.getId());
		newRoom.setName("Nueva habitación");
		newRoom.setDescription("Esta es la nueva descripción");
		newRoom.setNumberOfSeats(20);
		roomService.saveToEdit(newRoom);

		all = roomService.findAll();
		Assert.isTrue(all.size() == 6);
		
		roomService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the rooms of a gym, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The data of the new room must not be valid. The selected gym must be a gym of the system.
	 * Return: FALSE
	 * Postcondition: -
	 * Notes: This test must be done for each data of the new room.
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateRoomAdmin3(){
		
		Collection<Room> all;
		Room newRoom;
		Collection<Gym> allGyms;
		Gym gym = null;
		
		authenticate("admin");
		
		all = roomService.findAll();
		Assert.isTrue(all.size() == 5);
		
		allGyms = gymService.findAll();
		
		for(Gym i:allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		newRoom = roomService.create(gym.getId());
//		newRoom.setName("Nueva habitación");
//		newRoom.setDescription("Esta es la nueva descripción");
//		newRoom.setNumberOfSeats(20);
		roomService.saveToEdit(newRoom);

		all = roomService.findAll();
		Assert.isTrue(all.size() == 6);
		
		authenticate(null);
		roomService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the rooms of a gym, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The data of the new room must not be valid. The selected gym must be a gym of the system.
	 * Return: FALSE
	 * Postcondition: -
	 * Notes: This test must be done for each data of the new room.
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateRoomAdmin4(){
		
		Collection<Room> all;
		Room newRoom;
		Collection<Gym> allGyms;
		Gym gym = null;
		
		authenticate("admin");
		
		all = roomService.findAll();
		Assert.isTrue(all.size() == 5);
		
		allGyms = gymService.findAll();
		
		for(Gym i:allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
				break;
			}
		}
		
		newRoom = roomService.create(gym.getId());
		newRoom.setName("Nueva habitación");
		newRoom.setDescription("Esta es la nueva descripción");
		newRoom.setNumberOfSeats(-20);
		roomService.saveToEdit(newRoom);

		all = roomService.findAll();
		Assert.isTrue(all.size() == 6);
		
		authenticate(null);
		roomService.flush();
	}
	
	/**
	 * TEST POSITIVO
	 * Description: A user who is authenticated as an administrator must be able to manage the rooms of a gym, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The data use to modify the room must be valid.
	 * Return: TRUE
	 * Postcondition: A room is modify.
	 */
	@Test
	public void testModifyRoom1(){
		
		Collection<Room> all;
		Room roomToModify = null;
		
		authenticate("admin");
		
		all = roomService.findAll();
		
		for(Room i:all){
			if(i.getName().equals("Habitación general")){
				roomToModify = i;
			}
		}
		
		roomToModify.setDescription("Descripción modificada");
		roomService.saveToEdit(roomToModify);
		
		all = roomService.findAll();
		
		for(Room i:all){
			if(i.getName().equals("Habitación general")){
				roomToModify = i;
			}
		}
		
		Assert.isTrue(roomToModify.getDescription().equals("Descripción modificada"));
		
		authenticate(null);
		roomService.flush();
		
	}
	
	/**
	 * TEST POSITIVO
	 * Description: A user who is authenticated as an administrator must be able to manage the rooms of a gym, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The data use to modify the room must be valid.
	 * Return: TRUE
	 * Postcondition: A room is modify.
	 */
	@Test
	public void testModifyRoom2(){
		
		Collection<Room> all;
		Room roomToModify = null;
		
		authenticate("admin");
		
		all = roomService.findAll();
		
		for(Room i:all){
			if(i.getName().equals("Habitación general")){
				roomToModify = i;
			}
		}
		
		roomToModify.setNumberOfSeats(40);
		roomService.saveToEdit(roomToModify);
		
		all = roomService.findAll();
		
		for(Room i:all){
			if(i.getName().equals("Habitación general")){
				roomToModify = i;
			}
		}
		
		Assert.isTrue(roomToModify.getNumberOfSeats() == 40);
		
		authenticate(null);
		roomService.flush();
		
	}
	
	/**
	 * TEST NEGATIVO
	 * Description: A user who is authenticated as an administrator must be able to manage the rooms of a gym, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The data use to modify the room must not be valid.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testModifyRoom3(){
		
		Collection<Room> all;
		Room roomToModify = null;
		
		authenticate("admin");
		
		all = roomService.findAll();
		
		for(Room i:all){
			if(i.getName().equals("Piscina")){
				roomToModify = i;
			}
		}
		
		roomToModify.setNumberOfSeats(2);
		roomService.saveToEdit(roomToModify);
		
		all = roomService.findAll();
		
		for(Room i:all){
			if(i.getName().equals("Piscina")){
				roomToModify = i;
			}
		}
		
		Assert.isTrue(roomToModify.getNumberOfSeats() == 40);
		
		authenticate(null);
		roomService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the rooms of a gym, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is not an admin. The data use to modify the room must be valid. The selected gym must be a gym of the system.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testModifyRoom4(){
		
		Collection<Room> all;
		Room roomToModify = null;
				
		all = roomService.findAll();
		
		for(Room i:all){
			if(i.getName().equals("Habitación general")){
				roomToModify = i;
			}
		}
		
		roomToModify.setDescription("Descripción modificada");
		roomService.saveToEdit(roomToModify);
		
		all = roomService.findAll();
		
		for(Room i:all){
			if(i.getName().equals("Habitación general")){
				roomToModify = i;
			}
		}
		
		Assert.isTrue(roomToModify.getDescription().equals("Descripción modificada"));
		
		roomService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the rooms of a gym, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The data use to modify the room must not be valid. The selected gym must be a gym of the system.
	 * Return: FALSE
	 * Postcondition: -
	 * Notes: This test must be done for each data of the modify room.
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value = true)
	public void testModifyRoom5(){
		
		Collection<Room> all;
		Room roomToModify = null;
		
		authenticate("admin");
		
		all = roomService.findAll();
		
		for(Room i:all){
			if(i.getName().equals("Habitación general")){
				roomToModify = i;
			}
		}
		
		roomToModify.setNumberOfSeats(-40);
		roomService.saveToEdit(roomToModify);
		
		all = roomService.findAll();
		
		for(Room i:all){
			if(i.getName().equals("Habitación general")){
				roomToModify = i;
			}
		}
		
		Assert.isTrue(roomToModify.getNumberOfSeats() == -40);
		
		authenticate(null);
		roomService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the rooms of a gym, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The room to delete must be a room of the system.
	 * Return: TRUE
	 * Postcondition: The room is deleted.
	 */
	@Test
	public void testDeleteRoom1(){
		
		Collection<Room> all;
		Room roomToDelete = null;
		
		authenticate("admin");
		
		all = roomService.findAll();
		Assert.isTrue(all.size() == 5);
		
		for(Room i:all){
			if(i.getName().equals("Habitación general")){
				roomToDelete = i;
			}
		}
		
		roomService.delete(roomToDelete);
		
		all = roomService.findAll();
		Assert.isTrue(all.size() == 4);
		
		authenticate(null);
		roomService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the rooms of a gym, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is not an admin. The room to delete must be a room of the system.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteRoom2(){
		
		Collection<Room> all;
		Room roomToDelete = null;
				
		all = roomService.findAll();
		Assert.isTrue(all.size() == 5);
		
		for(Room i:all){
			if(i.getName().equals("Habitación general")){
				roomToDelete = i;
			}
		}
		
		roomService.delete(roomToDelete);
		
		all = roomService.findAll();
		Assert.isTrue(all.size() == 4);
		
		roomService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as an administrator must be able to manage the rooms of a gym, which includes listing, creating, modifying, and deleting them.
	 * Precondition: The user is an admin. The room to deleted must be a room of the system. The room must be attached to an activity.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteRoom3(){
		
		Collection<Room> all;
		Room roomToDelete = null;
		
		authenticate("admin");
		
		all = roomService.findAll();
		Assert.isTrue(all.size() == 5);
		
		for(Room i:all){
			if(i.getName().equals("Piscina")){
				roomToDelete = i;
			}
		}
		
		roomService.delete(roomToDelete);
		
		all = roomService.findAll();
		Assert.isTrue(all.size() == 4);
		
		authenticate(null);
		roomService.flush();
	}

}
