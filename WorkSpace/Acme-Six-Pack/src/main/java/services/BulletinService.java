package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
	
	public Collection<Bulletin> findBySingleKeyword(String keyword, int gymId){
		Assert.notNull(keyword);
		Assert.isTrue(!keyword.isEmpty());
		
		Collection<Bulletin> result;

		result = bulletinRepository.findBySingleKeyword(keyword, gymId);
		
		return result;
	}
}
