package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.BulletinRepository;
import domain.Bulletin;

@Service
@Transactional
public class BulletinService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private BulletinRepository bulletinRepository;
	
	// Supporting services ----------------------------------------------------

	
	
	// Constructors -----------------------------------------------------------
	
	public BulletinService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------

	
	// Other business methods -------------------------------------------------
	
	public Collection<Bulletin> findAllByGymId(int gymId) {
		Collection<Bulletin> result;
		
		result = bulletinRepository.findAllByGymId(gymId);
		
		return result;
	}
}
