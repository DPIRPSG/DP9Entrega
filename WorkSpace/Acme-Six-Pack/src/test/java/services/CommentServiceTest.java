package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Comment;
import domain.Gym;

import utilities.AbstractTest;
import utilities.InvalidPreTestException;

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
	
	// Test ---------------------------------------

	/**
	 * Test que comprueba que un comentario sobre un Gym se crea correctamente
	 */
	@Test
	public void testCreateCommentGymOk() {
		System.out.println("Start TestCreateCommentGymOk ---------------");
		
		authenticate("customer1");
		
		Comment comment;
		Collection<Comment> comments;
		Collection<Gym> gyms;
		Gym gym;
		int numOfCommentsBefore;
		int numberOfCommentsAfter;
		
		gyms = gymService.findAll();
		gym = null;
		
		for(Gym gymAux : gyms) {
			if(gymAux.getName().equals("Gym Sevilla")) {
				gym = gymAux;
			}
		}
		
		comments = commentService.findAllByCommentedEntityId(gym.getId());
		numOfCommentsBefore = comments.size();
		
		System.out.println("Number of comments of Gym before create other: " + numOfCommentsBefore + " --> " + comments);
		
		comment = commentService.create(gym.getId());
		comment.setText("test");
		comment.setStarRating(2);
		commentService.save(comment);
		
		comments = commentService.findAllByCommentedEntityId(gym.getId());
		numberOfCommentsAfter = comments.size();
		
		System.out.println("Number of comments of Gym before create other: " + numberOfCommentsAfter + " --> " + comments);
		
		Assert.isTrue(numOfCommentsBefore + 1 == numberOfCommentsAfter);
		
		authenticate(null);
		
		System.out.println("End TestCreateCommentGymOk ---------------");
	}
	
	@Test(expected=IllegalArgumentException.class)
	@Transactional(noRollbackFor=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCommentGymWithoutActor() throws InvalidPreTestException {
		System.out.println("Start testCreateCommentGymWithoutActor ---------------");
				
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
		
		System.out.println("End testCreateCommentGymWithoutActor ---------------");
	}
}
