package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Comment;
import domain.Customer;
import domain.Gym;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class CommentServiceTest extends AbstractTest {
	
	// Service under test -------------------------
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private GymService gymService;
	
	@Autowired
	private CustomerService customerService;
	
	// Test ---------------------------------------

	//Test para comentar Gym
	/**
	 * Test que comprueba que un comentario sobre un Gym se crea correctamente
	 */
	@Test
	public void testCreateCommentGymOk() {		
		authenticate("customer1");
		
		Customer customer;
		Comment comment;
		Collection<Comment> comments;
		Collection<Gym> gyms;
		Gym gym;
		int numOfCommentsBefore;
		int numberOfCommentsAfter;
		
		customer = customerService.findByPrincipal();
		gyms = gymService.findAll();
		gym = null;
		
		for(Gym gymAux : gyms) {
			if(gymAux.getName().equals("Gym Sevilla")) {
				gym = gymAux;
			}
		}
		
		comments = commentService.findAllByCommentedEntityId(gym.getId());
		numOfCommentsBefore = comments.size();
				
		comment = commentService.create(gym.getId());
		comment.setText("test");
		comment.setStarRating(2);
		commentService.save(comment);
		
		comments = commentService.findAllByCommentedEntityId(gym.getId());
		numberOfCommentsAfter = comments.size();
				
		Assert.isTrue(numOfCommentsBefore + 1 == numberOfCommentsAfter);
		Assert.isTrue(comment.getActor().getId() == customer.getId());
		Assert.isTrue(comment.getStarRating() == 2);
		Assert.isTrue(comment.getText().equals("test"));
		Assert.isTrue(comment.getCommentedEntity().getId() == gym.getId());
		Assert.isTrue(comment.getDeleted() == false);
		
		authenticate(null);
	}
	
	/**
	 * Test que comprueba que si se intenta crear un comentario sin estar logueado falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCommentGymWithoutActor() {				
		Comment comment;
		Collection<Gym> gyms;
		Gym gym;
		
		gyms = gymService.findAll();
		gym = null;
		
		for(Gym gymAux : gyms) {
			if(gymAux.getName().equals("Gym Sevilla")) {
				gym = gymAux;
			}
		}
		
		comment = commentService.create(gym.getId());
		commentService.save(comment);
	}
	
	/**
	 * Test que comprueba que al crear un comentario con una puntuación negativa falla
	 */
	@Test(expected =javax.validation.ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCommentGymError1() {				
		Comment comment;
		Collection<Gym> gyms;
		Gym gym;		
		
		authenticate("customer1");
		
		gyms = gymService.findAll();
		gym = null;
		
		for(Gym gymAux : gyms) {
			if(gymAux.getName().equals("Gym Sevilla")) {
				gym = gymAux;
			}
		}
		
		comment = commentService.create(gym.getId());
		comment.setStarRating(-1);
		comment.setText("prueba");
		commentService.save(comment);
		
		authenticate(null);
	}
	
	/**
	 * Test que comprueba que al crear un comentario con una puntuación mayor que 3 falla
	 */
	@Test(expected =javax.validation.ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCommentGymError2() {				
		Comment comment;
		Collection<Gym> gyms;
		Gym gym;		
		
		authenticate("customer1");
		
		gyms = gymService.findAll();
		gym = null;
		
		for(Gym gymAux : gyms) {
			if(gymAux.getName().equals("Gym Sevilla")) {
				gym = gymAux;
			}
		}
		
		comment = commentService.create(gym.getId());
		comment.setStarRating(4);
		comment.setText("prueba");
		commentService.save(comment);
		
		authenticate(null);
	}

	/**
	 * Test que comprueba que al crear un comentario sin puntuación no falla
	 */
	@Test//(expected =javax.validation.ConstraintViolationException.class)
	//@Rollback(value=true)
	public void testCreateCommentWithoutStarRating() {			
		Comment comment;
		Collection<Gym> gyms;
		Gym gym;		
		
		authenticate("customer1");
		
		gyms = gymService.findAll();
		gym = null;
		
		for(Gym gymAux : gyms) {
			if(gymAux.getName().equals("Gym Sevilla")) {
				gym = gymAux;
			}
		}
		comment = commentService.create(gym.getId());
		//comment.setStarRating();
		comment.setText("prueba");
		commentService.save(comment);
				
		authenticate(null);
	}

	/**
	 * Test que comprueba que al crear un comentario sin texto falla
	 */
	@Test(expected =javax.validation.ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCommentGymError3() {				
		Comment comment;
		Collection<Gym> gyms;
		Gym gym;
		
		authenticate("customer1");
		
		gyms = gymService.findAll();
		gym = null;
		
		for(Gym gymAux : gyms) {
			if(gymAux.getName().equals("Gym Sevilla")) {
				gym = gymAux;
			}
		}
		
		comment = commentService.create(gym.getId());
		comment.setStarRating(2);
		//comment.setText("prueba");
		commentService.save(comment);
		
		authenticate(null);
	}
	
	/**
	 * Test que comprueba que al crear un comentario sin Gym falla
	 */
	@Test(expected =DataIntegrityViolationException.class)
	@Rollback(value=true)
	public void testCreateCommentGymError5() {				
		Comment comment;
		Collection<Gym> gyms;
		Gym gym;		
		
		authenticate("customer1");
		
		gyms = gymService.findAll();
		gym = null;
		
		for(Gym gymAux : gyms) {
			if(gymAux.getName().equals("Gym Sevilla")) {
				gym = gymAux;
			}
		}
		
		comment = commentService.create(gym.getId());
		comment.setStarRating(1);
		comment.setText("prueba");
		comment.setCommentedEntity(null);
		commentService.save(comment);
		
		authenticate(null);
	}
	
	/**
	 * Test que comprueba que al crear un comentario sin customer falla
	 */
	@Test(expected =NullPointerException.class)
	@Rollback(value=true)
	public void testCreateCommentGymError6() {				
		Comment comment;
		Collection<Gym> gyms;
		Gym gym;		
		
		authenticate("customer1");
		
		gyms = gymService.findAll();
		gym = null;
		
		for(Gym gymAux : gyms) {
			if(gymAux.getName().equals("Gym Sevilla")) {
				gym = gymAux;
			}
		}
		
		comment = commentService.create(gym.getId());
		comment.setStarRating(1);
		comment.setText("prueba");
		comment.setActor(null);
		commentService.save(comment);
		
		authenticate(null);
	}
	
	/**
	 * Test que comprueba que al crear un comentario con el atributo deleted a True falla
	 */
	@Test(expected =javax.validation.ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCommentGymError7() {				
		Comment comment;
		Collection<Gym> gyms;
		Gym gym;		
		
		authenticate("customer1");
		
		gyms = gymService.findAll();
		gym = null;
		
		for(Gym gymAux : gyms) {
			if(gymAux.getName().equals("Gym Sevilla")) {
				gym = gymAux;
			}
		}
		
		comment = commentService.create(gym.getId());
		comment.setStarRating(1);
		comment.setText("prueba");
		comment.setDeleted(true);
		commentService.save(comment);
		
		authenticate(null);
	}

}
