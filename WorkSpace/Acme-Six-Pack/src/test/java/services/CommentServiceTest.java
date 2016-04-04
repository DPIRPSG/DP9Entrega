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

import utilities.AbstractTest;
import domain.Comment;
import domain.Customer;
import domain.Gym;
import domain.ServiceEntity;
import domain.Trainer;

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
	
	@Autowired
	private ServiceService serviceService;
	
	@Autowired
	private TrainerService trainerService;
	
	// Test ---------------------------------------

	//Test para comentar Gym
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */
	
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
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */
	
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
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */
	
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
		commentService.flush();
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */
	
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
		commentService.flush();
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario sin puntuación no falla
	 */
	@Test//(expected =javax.validation.ConstraintViolationException.class)
	//@Rollback(value=true)
	public void testCreateCommentGymWithoutStarRating() {			
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
		
		Assert.isTrue(comment.getStarRating() == 0);
				
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

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
		commentService.flush();
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */
	
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
		commentService.flush();
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */
	
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
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */
	
	/**
	 * Test que comprueba que al crear un comentario con el atributo deleted a True falla
	 */
	@Test(expected =IllegalArgumentException.class)
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
		commentService.flush();
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */
	
	/**
	 * Test que comprueba que al crear un comentario cambiando el usuario que realiza el comentario falla
	 */
	@Test(expected =IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCommentGymError8() {				
		Comment comment;
		Collection<Gym> gyms;
		Gym gym;
		Customer customer;
		
		authenticate("customer2");
		customer = customerService.findByPrincipal();
		authenticate(null);
		
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
		comment.setDeleted(false);
		comment.setActor(customer);
		commentService.save(comment);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 23.1
	 * A user who is authenticated as an administrator must be able to delete a comment that he or she considers is inappropriate.
	 */
	
	/**
	 * Test que comprueba que se puede borrar un comentario en condiciones normales
	 */
	@Test
	public void testDeleteCommentGymOk() {
		Comment comment;
		Gym gym;
		
		gym = gymService.findAll().iterator().next();
		comment = gym.getComments().iterator().next();
		
		authenticate("admin");
		
		Assert.isTrue(gym.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == false);
		
		commentService.delete(comment);
		
		gym = gymService.findOne(gym.getId());
		
		Assert.isTrue(gym.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == true);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 23.1
	 * A user who is authenticated as an administrator must be able to delete a comment that he or she considers is inappropriate.
	 */
	
	/**
	 * Test que comprueba que al intentar borrar un comentario sin ser admin falla
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteCommentGymError1() {
		Comment comment;
		Gym gym;
		
		gym = gymService.findAll().iterator().next();
		comment = gym.getComments().iterator().next();
		
		//authenticate("admin");
		
		Assert.isTrue(gym.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == false);
		
		commentService.delete(comment);
		
		gym = gymService.findOne(gym.getId());
		
		Assert.isTrue(gym.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == true);
		
		//authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 23.1
	 * A user who is authenticated as an administrator must be able to delete a comment that he or she considers is inappropriate.
	 */
	
	/**
	 * Test que comprueba que al intentar borrar un comentario sin ser admin falla
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteCommentGymError2() {
		Comment comment;
		Gym gym;
		
		gym = gymService.findAll().iterator().next();
		comment = gym.getComments().iterator().next();
		
		authenticate("customer1");
		
		Assert.isTrue(gym.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == false);
		
		commentService.delete(comment);
		
		gym = gymService.findOne(gym.getId());
		
		Assert.isTrue(gym.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == true);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 23.1
	 * A user who is authenticated as an administrator must be able to delete a comment that he or she considers is inappropriate.
	 */
	
	/**
	 * Test que comprueba que al intentar borrar un comentario sin ser admin falla
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteCommentGymError3() {
		Comment comment;
		Gym gym;
		
		gym = gymService.findAll().iterator().next();
		comment = gym.getComments().iterator().next();
		
		authenticate("trainer1");
		
		Assert.isTrue(gym.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == false);
		
		commentService.delete(comment);
		
		gym = gymService.findOne(gym.getId());
		
		Assert.isTrue(gym.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == true);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 21.1
	 * Every user, including users who are not authenticated, must be able to list the comments that are associated with the gyms and the services.
	 */
	
	/**
	 * Test que comprueba el correcto funcionamiento de findAllByCommentedEntityId
	 */
	@Test
	public void testListCommentsGym() {
		Collection<Comment> comments;
		Gym gym;
		
		gym = gymService.findAll().iterator().next();
		
		comments = commentService.findAllByCommentedEntityId(gym.getId());
		
		Assert.isTrue(comments.size() == 3);
	}
	
	// Test para comentar Service
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */
	
	/**
	 * Test que comprueba que un comentario sobre un Service se crea correctamente
	 */
	@Test
	public void testCreateCommentServiceOk() {
		authenticate("customer1");

		Customer customer;
		Comment comment;
		Collection<Comment> comments;
		Collection<ServiceEntity> services;
		ServiceEntity service;
		int numOfCommentsBefore;
		int numberOfCommentsAfter;

		customer = customerService.findByPrincipal();
		services = serviceService.findAll();
		service = null;

		for (ServiceEntity serviceAux : services) {
			if (serviceAux.getName().equals("Fitness")) {
				service = serviceAux;
			}
		}

		comments = commentService.findAllByCommentedEntityId(service.getId());
		numOfCommentsBefore = comments.size();

		comment = commentService.create(service.getId());
		comment.setText("test");
		comment.setStarRating(2);
		commentService.save(comment);

		comments = commentService.findAllByCommentedEntityId(service.getId());
		numberOfCommentsAfter = comments.size();

		Assert.isTrue(numOfCommentsBefore + 1 == numberOfCommentsAfter);
		Assert.isTrue(comment.getActor().getId() == customer.getId());
		Assert.isTrue(comment.getStarRating() == 2);
		Assert.isTrue(comment.getText().equals("test"));
		Assert.isTrue(comment.getCommentedEntity().getId() == service.getId());
		Assert.isTrue(comment.getDeleted() == false);

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que si se intenta crear un comentario sin estar
	 * logueado falla
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateCommentServiceWithoutActor() {
		Comment comment;
		Collection<ServiceEntity> services;
		ServiceEntity service;

		services = serviceService.findAll();
		service = null;

		for (ServiceEntity serviceAux : services) {
			if (serviceAux.getName().equals("Fitness")) {
				service = serviceAux;
			}
		}

		comment = commentService.create(service.getId());
		commentService.save(comment);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario con una puntuación negativa
	 * falla
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateCommentServiceError1() {
		Comment comment;
		Collection<ServiceEntity> services;
		ServiceEntity service;

		authenticate("customer1");

		services = serviceService.findAll();
		service = null;

		for (ServiceEntity serviceAux : services) {
			if (serviceAux.getName().equals("Fitness")) {
				service = serviceAux;
			}
		}

		comment = commentService.create(service.getId());
		comment.setStarRating(-1);
		comment.setText("prueba");
		commentService.save(comment);
		commentService.flush();

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario con una puntuación mayor
	 * que 3 falla
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateCommentServiceError2() {
		Comment comment;
		Collection<ServiceEntity> services;
		ServiceEntity service;

		authenticate("customer1");

		services = serviceService.findAll();
		service = null;

		for (ServiceEntity serviceAux : services) {
			if (serviceAux.getName().equals("Fitness")) {
				service = serviceAux;
			}
		}

		comment = commentService.create(service.getId());
		comment.setStarRating(4);
		comment.setText("prueba");
		commentService.save(comment);
		commentService.flush();

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario sin puntuación no falla
	 */
	@Test
	// (expected =javax.validation.ConstraintViolationException.class)
	// @Rollback(value=true)
	public void testCreateCommentServiceWithoutStarRating() {
		Comment comment;
		Collection<ServiceEntity> services;
		ServiceEntity service;

		authenticate("customer1");

		services = serviceService.findAll();
		service = null;

		for (ServiceEntity serviceAux : services) {
			if (serviceAux.getName().equals("Fitness")) {
				service = serviceAux;
			}
		}
		comment = commentService.create(service.getId());
		// comment.setStarRating();
		comment.setText("prueba");
		commentService.save(comment);

		Assert.isTrue(comment.getStarRating() == 0);

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario sin texto falla
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateCommentServiceError3() {
		Comment comment;
		Collection<ServiceEntity> services;
		ServiceEntity service;

		authenticate("customer1");

		services = serviceService.findAll();
		service = null;

		for (ServiceEntity serviceAux : services) {
			if (serviceAux.getName().equals("Fitness")) {
				service = serviceAux;
			}
		}

		comment = commentService.create(service.getId());
		comment.setStarRating(2);
		// comment.setText("prueba");
		commentService.save(comment);
		commentService.flush();

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario sin Service falla
	 */
	@Test(expected = DataIntegrityViolationException.class)
	@Rollback(value = true)
	public void testCreateCommentServiceError5() {
		Comment comment;
		Collection<ServiceEntity> services;
		ServiceEntity service;

		authenticate("customer1");

		services = serviceService.findAll();
		service = null;

		for (ServiceEntity serviceAux : services) {
			if (serviceAux.getName().equals("Fitness")) {
				service = serviceAux;
			}
		}

		comment = commentService.create(service.getId());
		comment.setStarRating(1);
		comment.setText("prueba");
		comment.setCommentedEntity(null);
		commentService.save(comment);
		commentService.flush();

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario sin customer falla
	 */
	@Test(expected = NullPointerException.class)
	@Rollback(value = true)
	public void testCreateCommentServiceError6() {
		Comment comment;
		Collection<ServiceEntity> services;
		ServiceEntity service;

		authenticate("customer1");

		services = serviceService.findAll();
		service = null;

		for (ServiceEntity serviceAux : services) {
			if (serviceAux.getName().equals("Fitness")) {
				service = serviceAux;
			}
		}

		comment = commentService.create(service.getId());
		comment.setStarRating(1);
		comment.setText("prueba");
		comment.setActor(null);
		commentService.save(comment);

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario con el atributo deleted a
	 * True falla
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateCommentServiceError7() {
		Comment comment;
		Collection<ServiceEntity> services;
		ServiceEntity service;

		authenticate("customer1");

		services = serviceService.findAll();
		service = null;

		for (ServiceEntity serviceAux : services) {
			if (serviceAux.getName().equals("Fitness")) {
				service = serviceAux;
			}
		}

		comment = commentService.create(service.getId());
		comment.setStarRating(1);
		comment.setText("prueba");
		comment.setDeleted(true);
		commentService.save(comment);
		commentService.flush();

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */
	
	/**
	 * Test que comprueba que al crear un comentario cambiando el usuario que realiza el comentario falla
	 */
	@Test(expected =IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCommentServiceError8() {				
		Comment comment;
		Collection<ServiceEntity> services;
		ServiceEntity service;
		Customer customer;
		
		authenticate("customer2");
		customer = customerService.findByPrincipal();
		authenticate(null);
		
		authenticate("customer1");
		
		services = serviceService.findAll();
		service = null;
		
		for(ServiceEntity serviceAux : services) {
			if(serviceAux.getName().equals("Fitness")) {
				service = serviceAux;
			}
		}
		
		comment = commentService.create(service.getId());
		comment.setStarRating(1);
		comment.setText("prueba");
		comment.setDeleted(false);
		comment.setActor(customer);
		commentService.save(comment);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 23.1
	 * A user who is authenticated as an administrator must be able to delete a comment that he or she considers is inappropriate.
	 */

	/**
	 * Test que comprueba que se puede borrar un comentario en condiciones
	 * normales
	 */
	@Test
	public void testDeleteCommentServiceOk() {
		Comment comment;
		ServiceEntity service;

		service = serviceService.findAll().iterator().next();
		comment = service.getComments().iterator().next();

		authenticate("admin");

		Assert.isTrue(service.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == false);

		commentService.delete(comment);

		service = serviceService.findOne(service.getId());

		Assert.isTrue(service.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == true);

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 23.1
	 * A user who is authenticated as an administrator must be able to delete a comment that he or she considers is inappropriate.
	 */

	/**
	 * Test que comprueba que al intentar borrar un comentario sin ser admin
	 * falla
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteCommentServiceError1() {
		Comment comment;
		ServiceEntity service;

		service = serviceService.findAll().iterator().next();
		comment = service.getComments().iterator().next();

		// authenticate("admin");

		Assert.isTrue(service.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == false);

		commentService.delete(comment);

		service = serviceService.findOne(service.getId());

		Assert.isTrue(service.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == true);

		// authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 23.1
	 * A user who is authenticated as an administrator must be able to delete a comment that he or she considers is inappropriate.
	 */

	/**
	 * Test que comprueba que al intentar borrar un comentario sin ser admin
	 * falla
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteCommentServiceError2() {
		Comment comment;
		ServiceEntity service;

		service = serviceService.findAll().iterator().next();
		comment = service.getComments().iterator().next();

		authenticate("customer1");

		Assert.isTrue(service.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == false);

		commentService.delete(comment);

		service = serviceService.findOne(service.getId());

		Assert.isTrue(service.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == true);

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 23.1
	 * A user who is authenticated as an administrator must be able to delete a comment that he or she considers is inappropriate.
	 */

	/**
	 * Test que comprueba que al intentar borrar un comentario sin ser admin
	 * falla
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteCommentServiceError3() {
		Comment comment;
		ServiceEntity service;

		service = serviceService.findAll().iterator().next();
		comment = service.getComments().iterator().next();

		authenticate("trainer1");

		Assert.isTrue(service.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == false);

		commentService.delete(comment);

		service = serviceService.findOne(service.getId());

		Assert.isTrue(service.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == true);

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 21.1
	 * Every user, including users who are not authenticated, must be able to list the comments that are associated with the gyms and the services.
	 */

	/**
	 * Test que comprueba el correcto funcionamiento de
	 * findAllByCommentedEntityId
	 */
	@Test
	public void testListCommentsService() {
		Collection<Comment> comments;
		ServiceEntity service;

		service = serviceService.findAll().iterator().next();

		comments = commentService.findAllByCommentedEntityId(service.getId());
		
		Assert.isTrue(comments.size() == 1);
	}
	
	//Test para comentar Trainer
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */
	
	/**
	 * Test que comprueba que un comentario sobre un Trainer se crea correctamente
	 */
	@Test
	public void testCreateCommentTrainerOk() {
		authenticate("customer1");

		Customer customer;
		Comment comment;
		Collection<Comment> comments;
		Collection<Trainer> trainers;
		Trainer trainer;
		int numOfCommentsBefore;
		int numberOfCommentsAfter;

		customer = customerService.findByPrincipal();
		trainers = trainerService.findAll();
		trainer = null;

		for (Trainer trainerAux : trainers) {
			if (trainerAux.getName().equals("Pablo")) {
				trainer = trainerAux;
			}
		}

		comments = commentService.findAllByCommentedEntityId(trainer.getId());
		numOfCommentsBefore = comments.size();

		comment = commentService.create(trainer.getId());
		comment.setText("test");
		comment.setStarRating(2);
		commentService.save(comment);

		comments = commentService.findAllByCommentedEntityId(trainer.getId());
		numberOfCommentsAfter = comments.size();

		Assert.isTrue(numOfCommentsBefore + 1 == numberOfCommentsAfter);
		Assert.isTrue(comment.getActor().getId() == customer.getId());
		Assert.isTrue(comment.getStarRating() == 2);
		Assert.isTrue(comment.getText().equals("test"));
		Assert.isTrue(comment.getCommentedEntity().getId() == trainer.getId());
		Assert.isTrue(comment.getDeleted() == false);

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que si se intenta crear un comentario sin estar
	 * logueado falla
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateCommentTrainerWithoutActor() {
		Comment comment;
		Collection<Trainer> trainers;
		Trainer trainer;

		trainers = trainerService.findAll();
		trainer = null;

		for (Trainer trainerAux : trainers) {
			if (trainerAux.getName().equals("Pablo")) {
				trainer = trainerAux;
			}
		}

		comment = commentService.create(trainer.getId());
		commentService.save(comment);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario con una puntuación negativa
	 * falla
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateCommentTrainerError1() {
		Comment comment;
		Collection<Trainer> trainers;
		Trainer trainer;

		authenticate("customer1");

		trainers = trainerService.findAll();
		trainer = null;

		for (Trainer trainerAux : trainers) {
			if (trainerAux.getName().equals("Pablo")) {
				trainer = trainerAux;
			}
		}

		comment = commentService.create(trainer.getId());
		comment.setStarRating(-1);
		comment.setText("prueba");
		commentService.save(comment);
		commentService.flush();

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario con una puntuación mayor
	 * que 3 falla
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateCommentTrainerError2() {
		Comment comment;
		Collection<Trainer> trainers;
		Trainer trainer;

		authenticate("customer1");

		trainers = trainerService.findAll();
		trainer = null;

		for (Trainer trainerAux : trainers) {
			if (trainerAux.getName().equals("Pablo")) {
				trainer = trainerAux;
			}
		}

		comment = commentService.create(trainer.getId());
		comment.setStarRating(4);
		comment.setText("prueba");
		commentService.save(comment);
		commentService.flush();

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario sin puntuación no falla
	 */
	@Test
	// (expected =javax.validation.ConstraintViolationException.class)
	// @Rollback(value=true)
	public void testCreateCommentTrainerWithoutStarRating() {
		Comment comment;
		Collection<Trainer> trainers;
		Trainer trainer;

		authenticate("customer1");

		trainers = trainerService.findAll();
		trainer = null;

		for (Trainer trainerAux : trainers) {
			if (trainerAux.getName().equals("Pablo")) {
				trainer = trainerAux;
			}
		}
		comment = commentService.create(trainer.getId());
		// comment.setStarRating();
		comment.setText("prueba");
		commentService.save(comment);

		Assert.isTrue(comment.getStarRating() == 0);

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario sin texto falla
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateCommentTrainerError3() {
		Comment comment;
		Collection<Trainer> trainers;
		Trainer trainer;

		authenticate("customer1");

		trainers = trainerService.findAll();
		trainer = null;

		for (Trainer trainerAux : trainers) {
			if (trainerAux.getName().equals("Pablo")) {
				trainer = trainerAux;
			}
		}

		comment = commentService.create(trainer.getId());
		comment.setStarRating(2);
		// comment.setText("prueba");
		commentService.save(comment);
		commentService.flush();

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario sin Trainer falla
	 */
	@Test(expected = DataIntegrityViolationException.class)
	@Rollback(value = true)
	public void testCreateCommentTrainerError5() {
		Comment comment;
		Collection<Trainer> trainers;
		Trainer trainer;

		authenticate("customer1");

		trainers = trainerService.findAll();
		trainer = null;

		for (Trainer trainerAux : trainers) {
			if (trainerAux.getName().equals("Pablo")) {
				trainer = trainerAux;
			}
		}

		comment = commentService.create(trainer.getId());
		comment.setStarRating(1);
		comment.setText("prueba");
		comment.setCommentedEntity(null);
		commentService.save(comment);
		commentService.flush();

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario sin customer falla
	 */
	@Test(expected = NullPointerException.class)
	@Rollback(value = true)
	public void testCreateCommentTrainerError6() {
		Comment comment;
		Collection<Trainer> trainers;
		Trainer trainer;

		authenticate("customer1");

		trainers = trainerService.findAll();
		trainer = null;

		for (Trainer trainerAux : trainers) {
			if (trainerAux.getName().equals("Pablo")) {
				trainer = trainerAux;
			}
		}

		comment = commentService.create(trainer.getId());
		comment.setStarRating(1);
		comment.setText("prueba");
		comment.setActor(null);
		commentService.save(comment);

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario con el atributo deleted a
	 * True falla
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateCommentTrainerError7() {
		Comment comment;
		Collection<Trainer> trainers;
		Trainer trainer;

		authenticate("customer1");

		trainers = trainerService.findAll();
		trainer = null;

		for (Trainer trainerAux : trainers) {
			if (trainerAux.getName().equals("Pablo")) {
				trainer = trainerAux;
			}
		}

		comment = commentService.create(trainer.getId());
		comment.setStarRating(1);
		comment.setText("prueba");
		comment.setDeleted(true);
		commentService.save(comment);
		commentService.flush();

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */
	
	/**
	 * Test que comprueba que al crear un comentario cambiando el usuario que realiza el comentario falla
	 */
	@Test(expected =IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCommentTrainerError8() {				
		Comment comment;
		Collection<Trainer> trainers;
		Trainer trainer;
		Customer customer;
		
		authenticate("customer2");
		customer = customerService.findByPrincipal();
		authenticate(null);
		
		authenticate("customer1");
		
		trainers = trainerService.findAll();
		trainer = null;
		
		for(Trainer trainerAux : trainers) {
			if(trainerAux.getName().equals("Pablo")) {
				trainer = trainerAux;
			}
		}
		
		comment = commentService.create(trainer.getId());
		comment.setStarRating(1);
		comment.setText("prueba");
		comment.setDeleted(false);
		comment.setActor(customer);
		commentService.save(comment);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 23.1
	 * A user who is authenticated as an administrator must be able to delete a comment that he or she considers is inappropriate.
	 */

	/**
	 * Test que comprueba que se puede borrar un comentario en condiciones
	 * normales
	 */
	@Test
	public void testDeleteCommentTrainerOk() {
		Comment comment;
		Trainer trainer;

		trainer = trainerService.findAll().iterator().next();
		comment = trainer.getComments().iterator().next();

		authenticate("admin");

		Assert.isTrue(trainer.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == false);

		commentService.delete(comment);

		trainer = trainerService.findOne(trainer.getId());

		Assert.isTrue(trainer.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == true);

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 23.1
	 * A user who is authenticated as an administrator must be able to delete a comment that he or she considers is inappropriate.
	 */

	/**
	 * Test que comprueba que al intentar borrar un comentario sin ser admin
	 * falla
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteCommentTrainerError1() {
		Comment comment;
		Trainer trainer;

		trainer = trainerService.findAll().iterator().next();
		comment = trainer.getComments().iterator().next();

		//authenticate("admin");

		Assert.isTrue(trainer.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == false);

		commentService.delete(comment);

		trainer = trainerService.findOne(trainer.getId());

		Assert.isTrue(trainer.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == true);

		//authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 23.1
	 * A user who is authenticated as an administrator must be able to delete a comment that he or she considers is inappropriate.
	 */

	/**
	 * Test que comprueba que al intentar borrar un comentario sin ser admin
	 * falla
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteCommentTrainerError2() {
		Comment comment;
		Trainer trainer;

		trainer = trainerService.findAll().iterator().next();
		comment = trainer.getComments().iterator().next();

		authenticate("customer1");

		Assert.isTrue(trainer.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == false);

		commentService.delete(comment);

		trainer = trainerService.findOne(trainer.getId());

		Assert.isTrue(trainer.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == true);

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 23.1
	 * A user who is authenticated as an administrator must be able to delete a comment that he or she considers is inappropriate.
	 */

	/**
	 * Test que comprueba que al intentar borrar un comentario sin ser admin
	 * falla
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteCommentTraienrError3() {
		Comment comment;
		Trainer trainer;

		trainer = trainerService.findAll().iterator().next();
		comment = trainer.getComments().iterator().next();

		authenticate("trainer1");

		Assert.isTrue(trainer.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == false);

		commentService.delete(comment);

		trainer = trainerService.findOne(trainer.getId());

		Assert.isTrue(trainer.getComments().contains(comment));
		Assert.isTrue(comment.getDeleted() == true);

		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack1 - Level A - 21.1
	 * Every user, including users who are not authenticated, must be able to list the comments that are associated with the gyms and the services.
	 */

	/**
	 * Test que comprueba el correcto funcionamiento de
	 * findAllByCommentedEntityId
	 */
	@Test
	public void testListCommentsTrainer() {
		Collection<Comment> comments;
		Trainer trainer;

		trainer = trainerService.findAll().iterator().next();

		comments = commentService.findAllByCommentedEntityId(trainer.getId());
		
		Assert.isTrue(comments.size() == 1);
	}
	
	//Test generales
	
	/**
	 * Acme-Six-Pack1 - Level A - 22.1
	 * Every authenticated user must be able to write a comment and associate it with a gym or a service
	 */

	/**
	 * Test que comprueba que al crear un comentario sobre una entidad no
	 * comentable falla
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateCommentError() {
		Comment comment;

		authenticate("customer1");

		comment = commentService.create(9);
		comment.setStarRating(1);
		comment.setText("prueba");
		comment.setDeleted(true);
		commentService.save(comment);

		authenticate(null);
	}

	/**
	 * Acme-Six-Pack1 - Level A - 21.1
	 * Every user, including users who are not authenticated, must be able to list the comments that are associated with the gyms and the services.
	 */
	@Test
	public void testListComments() {
		Collection<Comment> comments;

		comments = commentService.findAll();

		Assert.isTrue(comments.size() == 10);
	}

}
