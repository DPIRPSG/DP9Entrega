package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.RoomRepository;
import domain.Room;

@Service
@Transactional
public class RoomService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private RoomRepository roomRepository;
	
	// Supporting services ----------------------------------------------------

	
	
	// Constructors -----------------------------------------------------------
	
	public RoomService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------

	
	// Other business methods -------------------------------------------------
	
	public Collection<Room> findAllByGymId(int gymId) {
		Collection<Room> result;
		
		result = roomRepository.findAllByGymId(gymId);
		
		return result;
	}
}
