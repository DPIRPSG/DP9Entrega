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

import domain.Curriculum;
import domain.Trainer;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class CurriculumServiceTest extends AbstractTest {

	// Service under test -------------------------
	
	@Autowired
	private CurriculumService curriculumService;
	
	@Autowired
	private TrainerService trainerService;
	
	// Test ---------------------------------------
	
	/**
	 * Test que crea un curriculum en condiciones normales
	 */
	@Test
	public void testCreateCurriculumOk() {
		Curriculum curriculum;
		Trainer trainer;
		Collection<Trainer> trainers;
		String skills, likes, dislikes;
		
		trainers = trainerService.findAll();
		trainer = null;
		
		skills = "Prueba";
		likes = "Prueba";
		dislikes = "Prueba";
		
		for(Trainer trainerAux : trainers) {
			if(trainerAux.getCurriculum() == null) {
				trainer = trainerAux;
				break;
			}
		}
		
		authenticate(trainer.getUserAccount().getUsername());
		
		curriculum = curriculumService.create();
		curriculum.setStatement("Hola");
		curriculum.setLikes(likes);
		curriculum.setSkills(skills);
		curriculum.setDislikes(dislikes);
		curriculum = curriculumService.save(curriculum);
		
		Assert.isTrue(curriculum.getStatement().equals("Hola"));
		Assert.isTrue(curriculum.getSkills().equals("Prueba"));
		Assert.isTrue(curriculum.getLikes().equals("Prueba"));
		Assert.isTrue(curriculum.getDislikes().equals("Prueba"));
		Assert.isTrue(curriculum.getPicture() == trainer.getPicture());
		
		authenticate(null);
	}
	
	@Test
	public void testCreateCurriculumError1() {
		Curriculum curriculum;
		Trainer trainer;
		String skills, likes, dislikes;
		
		skills = "Prueba";
		likes = "Prueba";
		dislikes = "Prueba";
		
		authenticate("trainer1");
		
		trainer = trainerService.findByPrincipal();
		
		curriculum = curriculumService.create();
		curriculum.setStatement("Hola");
		curriculum.setLikes(likes);
		curriculum.setSkills(skills);
		curriculum.setDislikes(dislikes);
		curriculum = curriculumService.save(curriculum);
		
		Assert.isTrue(curriculum.getStatement().equals("Hola"));
		Assert.isTrue(curriculum.getSkills().equals("Prueba"));
		Assert.isTrue(curriculum.getLikes().equals("Prueba"));
		Assert.isTrue(curriculum.getDislikes().equals("Prueba"));
		System.out.println(curriculum.getPicture());
		
		Assert.isTrue(curriculum.getPicture().equals(trainer.getPicture()));
		
		authenticate(null);
	}
}
