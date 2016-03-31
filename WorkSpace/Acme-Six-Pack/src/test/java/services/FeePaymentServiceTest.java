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

import domain.FeePayment;
import domain.Gym;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class FeePaymentServiceTest extends AbstractTest{

	// Pendientes a probar ! ! ! ! ! ! - - - - - - - - - - - 
	
		// gymService.findAllWithFeePaymentActive()
	
	// Service under test -------------------------
	
	@Autowired
	private GymService gymService;
	
	@Autowired
	private FeePaymentService feePaymentService;
	
	// Test ---------------------------------------
	
	/**
	 * TEST POSITIVO
	 * Description: A user who is authenticated as a customer must be able to list the gyms in which he or she has an active fee payment and navigate to the details of the corresponding fee payments.
	 * Precondition: The user is a customer. The selected gym is paid.
	 * Return: TRUE
	 * Postcondition: All feePayments in the gym selected.
	 */
	@Test
	public void testFeePaymentPaidGyms1(){
		
		Collection<FeePayment> all;
		Collection<Gym> allGyms;
		Gym gym = null;
		
		authenticate("customer1");
		
		allGyms = gymService.findAllWithFeePaymentActive();
		
		for(Gym i:allGyms){
			if(i.getName().equals("Gym Sevilla")){
				gym = i;
			}
		}
		
		all = feePaymentService.findAllByCustomerAndGym(gym.getId());
		
		Assert.isTrue(all.size() == 4);
		
		feePaymentService.flush();
	}
	
	/**
	 * TEST POSITIVO
	 * Description: A user who is authenticated as a customer must be able to list the gyms in which he or she has an active fee payment and navigate to the details of the corresponding fee payments.
	 * Precondition: The user is a customer. The selected gym is not paid.
	 * Return: TRUe
	 * Postcondition: All feePayments of the gym seleted
	 */
	@Test
	public void testFeePaymentPaidGyms2(){
		
		Collection<FeePayment> all;
		Collection<Gym> allGyms;
		Gym gym = null;
		
		authenticate("customer1");
		
		allGyms = gymService.findAllWithoutFeePaymentActive();
		
		for(Gym i:allGyms){
			if(i.getName().equals("Gym Cádiz")){
				gym = i;
			}
		}
		
		all = feePaymentService.findAllByCustomerAndGym(gym.getId());
		
		Assert.isTrue(all.size() == 0);
		
		feePaymentService.flush();
		
	}

}
