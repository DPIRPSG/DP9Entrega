package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CommentedEntityRepository;
import domain.Actor;
import domain.CommentedEntity;

@Service
@Transactional
public class CommentedEntityService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CommentedEntityRepository commentedEntityRepository;
	
	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private TrainerService trainerService;
	
	// Constructors -----------------------------------------------------------
	
	public CommentedEntityService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------

	public CommentedEntity findOne(int commentedEntityId){
		CommentedEntity result;
		Actor actorCommented;
		
		result = commentedEntityRepository.findOneById(commentedEntityId);
		
		actorCommented = actorService.findOne(commentedEntityId);
		
		if(actorCommented != null) {
			actorCommented = trainerService.findOne(commentedEntityId);
			Assert.isTrue(actorCommented != null, "Only a gym, service or trainer can be commented");
		}
		
		return result;
	}
	
	// Other business methods -------------------------------------------------
	
}
