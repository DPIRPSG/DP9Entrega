package services;

import java.util.Collection;
import java.util.Date;

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
	 * Acme-Six-Pack - Level A - 3.1
	 * Trainers can manage his curriculum, which includes creating it, displaying it, modifying it, or de-leting it.
	 */
	
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
	
	/**
	 * Acme-Six-Pack - Level A - 3.1
	 * Trainers can manage his curriculum, which includes creating it, displaying it, modifying it, or de-leting it.
	 */
	
	/**
	 * Test que crea un curriculum sin statement
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCurriculumError1() {
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
		//curriculum.setStatement("Hola");
		curriculum.setLikes(likes);
		curriculum.setSkills(skills);
		curriculum.setDislikes(dislikes);
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		//Assert.isTrue(curriculum.getStatement().equals("Hola"));
		Assert.isTrue(curriculum.getSkills().equals("Prueba"));
		Assert.isTrue(curriculum.getLikes().equals("Prueba"));
		Assert.isTrue(curriculum.getDislikes().equals("Prueba"));
		Assert.isTrue(curriculum.getPicture() == trainer.getPicture());
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level A - 3.1
	 * Trainers can manage his curriculum, which includes creating it, displaying it, modifying it, or de-leting it.
	 */
	
	/**
	 * Test que crea un curriculum sin skills
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCurriculumError2() {
		Curriculum curriculum;
		Trainer trainer;
		Collection<Trainer> trainers;
		String likes, dislikes;
		
		trainers = trainerService.findAll();
		trainer = null;
		
//		skills = "Prueba";
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
		//curriculum.setSkills(skills);
		curriculum.setDislikes(dislikes);
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		Assert.isTrue(curriculum.getStatement().equals("Hola"));
		//Assert.isTrue(curriculum.getSkills().equals("Prueba"));
		Assert.isTrue(curriculum.getLikes().equals("Prueba"));
		Assert.isTrue(curriculum.getDislikes().equals("Prueba"));
		Assert.isTrue(curriculum.getPicture() == trainer.getPicture());
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level A - 3.1
	 * Trainers can manage his curriculum, which includes creating it, displaying it, modifying it, or de-leting it.
	 */
	
	/**
	 * Test que crea un curriculum sin likes
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCurriculumError3() {
		Curriculum curriculum;
		Trainer trainer;
		Collection<Trainer> trainers;
		String skills, dislikes;
		
		trainers = trainerService.findAll();
		trainer = null;
		
		skills = "Prueba";
//		likes = "Prueba";
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
		//curriculum.setLikes(likes);
		curriculum.setSkills(skills);
		curriculum.setDislikes(dislikes);
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		Assert.isTrue(curriculum.getStatement().equals("Hola"));
		Assert.isTrue(curriculum.getSkills().equals("Prueba"));
		//Assert.isTrue(curriculum.getLikes().equals("Prueba"));
		Assert.isTrue(curriculum.getDislikes().equals("Prueba"));
		Assert.isTrue(curriculum.getPicture() == trainer.getPicture());
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level A - 3.1
	 * Trainers can manage his curriculum, which includes creating it, displaying it, modifying it, or de-leting it.
	 */
	
	/**
	 * Test que crea un curriculum sin dislikes
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCurriculumError4() {
		Curriculum curriculum;
		Trainer trainer;
		Collection<Trainer> trainers;
		String skills, likes;
		
		trainers = trainerService.findAll();
		trainer = null;
		
		skills = "Prueba";
		likes = "Prueba";
//		dislikes = "Prueba";
		
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
		//curriculum.setDislikes(dislikes);
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		Assert.isTrue(curriculum.getStatement().equals("Hola"));
		Assert.isTrue(curriculum.getSkills().equals("Prueba"));
		Assert.isTrue(curriculum.getLikes().equals("Prueba"));
		//Assert.isTrue(curriculum.getDislikes().equals("Prueba"));
		Assert.isTrue(curriculum.getPicture() == trainer.getPicture());
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level A - 3.1
	 * Trainers can manage his curriculum, which includes creating it, displaying it, modifying it, or de-leting it.
	 */
	
	/**
	 * Test que comprueba que al crear un curriculum sin foto, usa la del profile del Trainer
	 */
	@Test
	public void testCreateCurriculumWithoutPicture() {
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
		
		Assert.isTrue(curriculum.getPicture().equals(trainer.getPicture()));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level A - 3.1
	 * Trainers can manage his curriculum, which includes creating it, displaying it, modifying it, or de-leting it.
	 */
	
	/**
	 * Test que comprueba que se puede editar un curriculum de forma correcta
	 */
	@Test
	public void testEditCurriculum() {
		Trainer trainer;
		Curriculum curriculum;
		Date moment;
		String skills, likes, dislikes;
		
		skills = "Prueba";
		likes = "Prueba";
		dislikes = "Prueba";
			
		authenticate("trainer1");
		
		trainer = trainerService.findByPrincipal();
		curriculum = trainer.getCurriculum();
		
		moment = curriculum.getUpdateMoment();
		
		curriculum.setStatement("Hola");
		curriculum.setLikes(likes);
		curriculum.setSkills(skills);
		curriculum.setDislikes(dislikes);
		curriculum = curriculumService.save(curriculum);
		
		Assert.isTrue(curriculum.getStatement().equals("Hola"));
		Assert.isTrue(curriculum.getSkills().equals("Prueba"));
		Assert.isTrue(curriculum.getLikes().equals("Prueba"));
		Assert.isTrue(curriculum.getDislikes().equals("Prueba"));
		Assert.isTrue(curriculum.getPicture().equals(trainer.getPicture()));
		
		//Comprueba que cambia la fecha de actualizacion
		Assert.isTrue(moment.before(curriculum.getUpdateMoment()));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level A - 3.1
	 * Trainers can manage his curriculum, which includes creating it, displaying it, modifying it, or de-leting it.
	 */
	
	/**
	 * Test que comprueba que al editar un curriculum no se pueden dejar los likes en blanco
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testEditCurriculumError1() {
		Trainer trainer;
		Curriculum curriculum;
		Date moment;
		String skills, likes, dislikes;
		
		skills = "Prueba";
		likes = "Prueba";
		dislikes = "Prueba";
			
		authenticate("trainer1");
		
		trainer = trainerService.findByPrincipal();
		curriculum = trainer.getCurriculum();
		
		moment = curriculum.getUpdateMoment();
		
		curriculum.setStatement("Hola");
		//curriculum.setLikes(likes);
		curriculum.setSkills(skills);
		curriculum.setDislikes(dislikes);
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		Assert.isTrue(curriculum.getStatement().equals("Hola"));
		Assert.isTrue(curriculum.getSkills().equals("Prueba"));
		Assert.isTrue(curriculum.getLikes().equals("Prueba"));
		Assert.isTrue(curriculum.getDislikes().equals("Prueba"));
		Assert.isTrue(curriculum.getPicture().equals(trainer.getPicture()));
		
		//Comprueba que cambia la fecha de actualizacion
		Assert.isTrue(moment.before(curriculum.getUpdateMoment()));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level A - 3.1
	 * Trainers can manage his curriculum, which includes creating it, displaying it, modifying it, or de-leting it.
	 */
	
	/**
	 * Test que comprueba que al editar un curriculum no se pueden dejar los skills en blanco
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testEditCurriculumError2() {
		Trainer trainer;
		Curriculum curriculum;
		Date moment;
		String skills, likes, dislikes;
		
		skills = "Prueba";
		likes = "Prueba";
		dislikes = "Prueba";
			
		authenticate("trainer1");
		
		trainer = trainerService.findByPrincipal();
		curriculum = trainer.getCurriculum();
		
		moment = curriculum.getUpdateMoment();
		
		curriculum.setStatement("Hola");
		curriculum.setLikes(likes);
		//curriculum.setSkills(skills);
		curriculum.setDislikes(dislikes);
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		Assert.isTrue(curriculum.getStatement().equals("Hola"));
		Assert.isTrue(curriculum.getSkills().equals("Prueba"));
		Assert.isTrue(curriculum.getLikes().equals("Prueba"));
		Assert.isTrue(curriculum.getDislikes().equals("Prueba"));
		Assert.isTrue(curriculum.getPicture().equals(trainer.getPicture()));
		
		//Comprueba que cambia la fecha de actualizacion
		Assert.isTrue(moment.before(curriculum.getUpdateMoment()));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level A - 3.1
	 * Trainers can manage his curriculum, which includes creating it, displaying it, modifying it, or de-leting it.
	 */
	
	/**
	 * Test que comprueba que se puede eliminar un curriculum de forma correcta
	 */
	
	@Test
	public void testDeleteCurriculum() {
		Trainer trainer;
		Curriculum curriculum;
		
		authenticate("trainer1");
		
		trainer = trainerService.findByPrincipal();
		curriculum = trainer.getCurriculum();
		
		Assert.isTrue(curriculum != null);
		Assert.isTrue(trainer.getCurriculum() != null);
		
		curriculumService.delete(curriculum);
		
		Assert.isTrue(trainer.getCurriculum() == null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level A - 3.1
	 * Trainers can manage his curriculum, which includes creating it, displaying it, modifying it, or de-leting it.
	 */
	
	/**
	 * Test que comprueba que un usuario no logueado no puede borrar un curriculum
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testDeleteCurriculumError1() {
		Trainer trainer;
		Curriculum curriculum;
		
		authenticate("trainer1");
		
		trainer = trainerService.findByPrincipal();
		curriculum = trainer.getCurriculum();
		
		authenticate(null);
		
		Assert.isTrue(curriculum != null);
		Assert.isTrue(trainer.getCurriculum() != null);
		
		curriculumService.delete(curriculum);
		
		Assert.isTrue(trainer.getCurriculum() == null);
	}
	
	/**
	 * Acme-Six-Pack - Level A - 3.1
	 * Trainers can manage his curriculum, which includes creating it, displaying it, modifying it, or de-leting it.
	 */
	
	/**
	 * Test que comprueba que un trainer no puede borrar el curriculum de otro trainer
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testDeleteCurriculumError2() {
		Trainer trainer;
		Curriculum curriculum;
		
		authenticate("trainer1");
		
		trainer = trainerService.findByPrincipal();
		curriculum = trainer.getCurriculum();
		
		authenticate(null);
		
		authenticate("trainer2");
		
		Assert.isTrue(curriculum != null);
		Assert.isTrue(trainer.getCurriculum() != null);
		
		curriculumService.delete(curriculum);
		
		Assert.isTrue(trainer.getCurriculum() == null);
		
		authenticate(null);
	}
}
