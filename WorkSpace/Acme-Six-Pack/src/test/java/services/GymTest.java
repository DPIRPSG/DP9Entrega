package services;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class GymTest extends AbstractTest{

	// Pendientes a probar ! ! ! ! ! ! - - - - - - - - - - - 
	
		// gymService.findAllWithFeePaymentActive()
	
	// Service under test -------------------------

	
	// Test ---------------------------------------
	

}
