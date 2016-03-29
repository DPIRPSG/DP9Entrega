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

import domain.Gym;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class GymServiceTest extends AbstractTest{

	// Pendientes a probar ! ! ! ! ! ! - - - - - - - - - - - 
	
		// gymService.findAllWithFeePaymentActive()
	
	// Service under test -------------------------
	
	@Autowired
	private GymService gymService;
	
	// Test ---------------------------------------
	
	/**
	 * TEST POSITVO
	 * Description: A user who is not authenticated must be able to navigate (list) through the gyms.
	 * Precondition: -
	 * Return: TRUE
  	 * Postcondition: All gyms in system are shown.
	 */
	@Test
	public void testFindAllGyms1(){
		
		Collection<Gym> all;
		
		all = gymService.findAll();
		Assert.isTrue(all.size() == 3);
		
		gymService.flush();
		
	}
	
	

}
