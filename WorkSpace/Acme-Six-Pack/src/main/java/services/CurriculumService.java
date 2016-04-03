package services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CurriculumRepository;
import domain.Curriculum;
import domain.Trainer;

@Service
@Transactional
public class CurriculumService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CurriculumRepository curriculumRepository;
	
	// Supporting services ----------------------------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private TrainerService trainerService;

	// Constructors -----------------------------------------------------------

	public CurriculumService() {
		super();
	}


	// Simple CRUD methods ----------------------------------------------------
	
	public Curriculum create(){
		Assert.isTrue(actorService.checkAuthority("TRAINER"), "Only a Trainer can manage his Curriculum.");
		
		Curriculum result;		
		
		result = new Curriculum();
		
		result.setUpdateMoment(new Date()); // Se crea una fecha en este momento porque no puede ser null, pero la fecha real se fijará en el método "save"
		
		return result;
	}
	
	public Curriculum save(Curriculum curriculum){
		Assert.isTrue(actorService.checkAuthority("TRAINER"), "Only a Trainer can manage his Curriculum.");
		Assert.notNull(curriculum);
		if(curriculum.getId() != 0){ // Si está editando y no creando
			checkCurriculum(curriculum);
		}
		
		
		Trainer trainer;
		String profilePicture;
		int curriculumId;
		
		curriculum.setUpdateMoment(new Date());
		trainer = trainerService.findByPrincipal();
		curriculumId = curriculum.getId();
		
		if(curriculum.getPicture() == null || curriculum.getPicture() == ""){
			if(trainer.getPicture() != null || trainer.getPicture() != ""){
				profilePicture = trainer.getPicture();
				curriculum.setPicture(profilePicture);
			}
		}
		

		curriculum = curriculumRepository.save(curriculum);
		if(curriculumId == 0){
			trainer.setCurriculum(curriculum);
			trainerService.save(trainer);
		}
		
		return curriculum;
	}
	
	public void delete(Curriculum curriculum){
		Assert.isTrue(actorService.checkAuthority("TRAINER"), "Only a Trainer can manage his Curriculum.");
		Assert.isTrue(curriculum.getId() != 0, "You cant delete a Curriculum that isn't still created.");
		checkCurriculum(curriculum);
		
		Trainer trainer;
		
		trainer = trainerService.findByPrincipal();
		trainer.setCurriculum(null);
		curriculumRepository.delete(curriculum);
		
	}
	
	public Curriculum findOne(int curriculumId) {
		Assert.isTrue(actorService.checkAuthority("TRAINER"), "Only a Trainer can manage his Curriculum.");
		
		Curriculum result;
		
		result = curriculumRepository.findOne(curriculumId);
		
		return result;
	}
	
	private void checkCurriculum(Curriculum curriculum) {
		Trainer trainerPrincipal;
		Trainer trainerOwner;
		int curriculumId;
		
		curriculumId = curriculum.getId();
		trainerPrincipal = trainerService.findByPrincipal();
		trainerOwner = trainerService.findByCurriculumId(curriculumId);
		
		Assert.isTrue(trainerPrincipal == trainerOwner, "You can't manage a Curriculum of other Trainer.");
	}
	
	public void flush() {
		curriculumRepository.flush();
	}
}
