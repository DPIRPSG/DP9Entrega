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

import domain.Trainer;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class TrainerServiceTest extends AbstractTest{
	
	// Service under test -------------------------
	
	@Autowired
	private TrainerService trainerService;
	
	// Test ---------------------------------------
	
	/**
	 * Description: A user who is not authenticated must be able to search for a trainer using a single keyword that must appear in his or name, surname, or curriculum.
	 * Precondition: The given key word is found in a name.
	 * Return: TRUE
	 * Postcondition: All trainer that contains the given key word in its name, surname or curriculum are shown.
	 */
	@Test
	public void findAllTrainersByGivenKeyword1(){
		
		Collection<Trainer> all;
		
		all = trainerService.findBySingleKeyword("Pablo");
		
		Assert.isTrue(all.size() == 1);
		
		trainerService.flush();
		
	}
	
	/**
	 * Description: A user who is not authenticated must be able to search for a trainer using a single keyword that must appear in his or name, surname, or curriculum.
	 * Precondition: The given key word is found in a surname
	 * Return: TRUE
	 * Postcondition: All trainer that contains the given key word in its name, surname or curriculum are shown.
	 */
	@Test
	public void findAllTrainersByGivenKeyword2(){
		
		Collection<Trainer> all;
		
		all = trainerService.findBySingleKeyword("Gil");
		
		Assert.isTrue(all.size() == 1);
		
		trainerService.flush();
	}
	
	/**
	 * Description: A user who is not authenticated must be able to search for a trainer using a single keyword that must appear in his or name, surname, or curriculum.
	 * Precondition: The given key word is found in a surname
	 * Return: TRUE
	 * Postcondition: All trainer that contains the given key word in its name, surname or curriculum are shown.
	 */
	@Test
	public void findAllTrainersByGivenKeyword3(){
		
		Collection<Trainer> all;
		
		all = trainerService.findBySingleKeyword("Mata");
				
		Assert.isTrue(all.size() == 1);
		
		trainerService.flush();
	}
	
	/**
	 * Description: A user who is not authenticated must be able to search for a trainer using a single keyword that must appear in his or name, surname, or curriculum.
	 * Precondition: The given key word is found in a curriculum.
	 * Return: TRUE
	 * Postcondition: All trainer that contains the given key word in its name, surname or curriculum are shown.
	 */
	@Test
	public void findAllTrainersByGivenKeyword4(){
		
		Collection<Trainer> all;
		
		all = trainerService.findBySingleKeyword("No");
				
		Assert.isTrue(all.size() == 5);
		
		trainerService.flush();
	}
	
	/**
	 * Description: A user who is not authenticated must be able to search for a trainer using a single keyword that must appear in his or name, surname, or curriculum.
	 * Precondition: The given key word is found in a curriculum.
	 * Return: TRUE
	 * Postcondition: All trainer that contains the given key word in its name, surname or curriculum are shown.
	 */
	@Test
	public void findAllTrainersByGivenKeyword5(){
		
		Collection<Trainer> all;
		
		all = trainerService.findBySingleKeyword("Hololens");
				
		Assert.isTrue(all.size() == 0);
		
		trainerService.flush();
	}
}
