package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.BulletinRepository;
import domain.Bulletin;
import domain.Gym;

@Service
@Transactional
public class BulletinService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private BulletinRepository bulletinRepository;
	
	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private GymService gymService;
	
	// Constructors -----------------------------------------------------------
	
	public BulletinService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------

	public Bulletin create(int gymId) {
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can create bulletins");
		
		Bulletin result;
		Gym gym;
		Date moment;
		
		result = new Bulletin();
		
		gym = gymService.findOne(gymId);
		moment = new Date();
	
		result.setGym(gym);
		result.setPublishMoment(moment);
		
		return result;
	}
	
	public void save(Bulletin bulletin) {
		Assert.notNull(bulletin);
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can save bulletins");
		Gym gym;
		Date moment;
		
		gym = bulletin.getGym();
		moment = new Date();
		
		bulletin.setPublishMoment(moment);
		
		bulletinRepository.save(bulletin);
		
		gym.addBulletin(bulletin);
		
		gymService.save(gym);
	}
	
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
	
	public void flush(){
		
		bulletinRepository.flush();
		
	}

}
