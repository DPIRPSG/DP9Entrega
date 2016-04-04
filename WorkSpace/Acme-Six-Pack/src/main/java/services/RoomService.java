package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RoomRepository;
import domain.Activity;
import domain.Gym;
import domain.Room;

@Service
@Transactional
public class RoomService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private RoomRepository roomRepository;
	
	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private GymService gymService;
	
	// Constructors -----------------------------------------------------------
	
	public RoomService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------

	public Room create() {
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can create rooms");
		
		Room result;
		Collection<String> pictures;
		Collection<Activity> activities;
		
		result = new Room();
		
		pictures = new ArrayList<String>();
		activities = new ArrayList<Activity>();
		
		result.setPictures(pictures);
		result.setActivities(activities);
		
		return result;
	}
	public Room create(int gymId) {
		Room result;
		Gym gym;
		
		gym = gymService.findOne(gymId);
		result = this.create();
		result.setGym(gym);
		
		return result;
	}
	
	public Room save(Room room) {
		Assert.notNull(room);
		Room result;
		//Assert.isTrue(actorService.checkAuthority("ADMIN"),
			//	"Only an admin can save rooms");
		
		result = roomRepository.save(room);
		
		return result;
	}
	
	public void saveToEdit(Room room) {
		Assert.notNull(room);
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can save rooms");
		
		
		if(room.getId() == 0) {
			Gym gym;
			
			room = this.save(room);
			
			gym = room.getGym();
			
			gym.addRoom(room);
			
			gymService.save(gym);
		} else {
			Room roomPreSave;
			
			roomPreSave = this.findOne(room.getId());
			
			for(Activity activity : roomPreSave.getActivities()) {
				Assert.isTrue(room.getNumberOfSeats() >= activity.getNumberOfSeatsAvailable());
			}
			
			Assert.isTrue(roomPreSave.getGym().getId() == room.getGym().getId());
			
			this.save(room);
		}
	}
	
	public void delete(Room room) {
		Assert.notNull(room);
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can delete rooms");
		Assert.isTrue(room.getActivities().isEmpty());
		
		Gym gym;
		
		gym = room.getGym();
		
		gym.removeRoom(room);
		
		gymService.save(gym);
		roomRepository.delete(room);
		
	}
	
	public Collection<Room> findAll(){
		Collection<Room> result;
		
		result = roomRepository.findAll();
		
		return result;
	}
	
	public Room findOne(int roomId) {
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can save rooms");
		
		Room result;
		
		result = roomRepository.findOne(roomId);
		
		return result;
	}
	
	// Other business methods -------------------------------------------------
	
	public Collection<Room> findAllByGymId(int gymId) {
		Collection<Room> result;
		
		result = roomRepository.findAllByGymId(gymId);
		
		return result;
	}
	
	public void flush(){
		
		roomRepository.flush();
		
	}

	public Collection<Room> findAllByServiceId(int id) {
		Collection<Room> result;
		
		result = roomRepository.findAllByServiceId(id);
		
		return result;
	}
	
}
