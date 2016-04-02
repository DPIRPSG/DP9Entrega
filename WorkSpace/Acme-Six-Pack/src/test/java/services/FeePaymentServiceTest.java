package services;

import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.CreditCard;
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
	private FeePaymentService feePaymentService;
	
	@Autowired
	private GymService gymService;
	
	@Autowired
	private CustomerService customerService;
	
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
	
	/**
	 * Description: A user who is authenticated as a customer must be able to pay a gym fee.
	 * Precondition: The user is a customer. The data is valid.
	 * Return: TRUE
	 * Postcondition: The gym selected is paid.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testPayFeeCustomer1(){
		
		Collection<FeePayment> all;
		FeePayment newFeePayment;
		Collection<Gym> allPaid;
		Gym paidGym = null;
		Date activationMoment;
		
		authenticate("customer1");
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 4);
		
		allPaid = gymService.findAllWithFeePaymentActive();
		Assert.isTrue(allPaid.size() == 1);
		
		for(Gym i:allPaid){
			if(i.getName().equals("Gym Sevilla")){
				paidGym = i;
				break;
			}
		}
		
		
		newFeePayment = feePaymentService.create(paidGym.getId());
		
		activationMoment = new Date(116, 9, 1, 00, 00);
		newFeePayment.setActiveMoment(activationMoment);
		newFeePayment.setCreditCard(customerService.findByPrincipal().getCreditCards().iterator().next());
		
		feePaymentService.saveToEdit(newFeePayment);
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 5);
		
		authenticate(null);
		feePaymentService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to pay a gym fee.
	 * Precondition: The user is a customer. The data is valid.
	 * Return: TRUE
	 * Postcondition: The gym selected is paid.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testPayFeeCustomer2(){
		
		Collection<FeePayment> all;
		FeePayment newFeePayment;
		Collection<Gym> allPaid;
		Gym paidGym = null;
		Date activationMoment;
		
		authenticate("customer1");
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 4);
		
		allPaid = gymService.findAllWithoutFeePaymentActive();
		Assert.isTrue(allPaid.size() == 2);
		
		for(Gym i:allPaid){
			if(i.getName().equals("Gym Cádiz")){
				paidGym = i;
				break;
			}
		}
		
		
		newFeePayment = feePaymentService.create(paidGym.getId());
		
		activationMoment = new Date(116, 9, 1, 00, 00);
		newFeePayment.setActiveMoment(activationMoment);
		newFeePayment.setCreditCard(customerService.findByPrincipal().getCreditCards().iterator().next());
		
		feePaymentService.saveToEdit(newFeePayment);
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 5);

		authenticate(null);
		feePaymentService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to pay a gym fee.
	 * Precondition: The user is a customer. The data is valid.
	 * Return: TRUE
	 * Postcondition: The gym selected is paid.
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testPayFeeCustomer3(){
		
		Collection<FeePayment> all;
		FeePayment newFeePayment;
		Collection<Gym> allPaid;
		Gym paidGym = null;
		Date activationMoment;
				
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 4);
		
		allPaid = gymService.findAllWithFeePaymentActive();
		Assert.isTrue(allPaid.size() == 1);
		
		for(Gym i:allPaid){
			if(i.getName().equals("Gym Sevilla")){
				paidGym = i;
				break;
			}
		}
		
		
		newFeePayment = feePaymentService.create(paidGym.getId());
		
		activationMoment = new Date(116, 9, 1, 00, 00);
		newFeePayment.setActiveMoment(activationMoment);
		newFeePayment.setCreditCard(customerService.findByPrincipal().getCreditCards().iterator().next());
		
		feePaymentService.saveToEdit(newFeePayment);
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 5);

		authenticate(null);
		feePaymentService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to pay a gym fee.
	 * Precondition: The user is a customer. The data is valid.
	 * Return: TRUE
	 * Postcondition: The gym selected is paid.
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testPayFeeCustomer4(){
		
		Collection<FeePayment> all;
		FeePayment newFeePayment;
		Collection<Gym> allPaid;
		Gym paidGym = null;
		Date activationMoment;
				
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 4);
		
		allPaid = gymService.findAllWithoutFeePaymentActive();
		Assert.isTrue(allPaid.size() == 2);
		
		for(Gym i:allPaid){
			if(i.getName().equals("Gym Cádiz")){
				paidGym = i;
				break;
			}
		}
		
		
		newFeePayment = feePaymentService.create(paidGym.getId());
		
		activationMoment = new Date(116, 9, 1, 00, 00);
		newFeePayment.setActiveMoment(activationMoment);
		newFeePayment.setCreditCard(customerService.findByPrincipal().getCreditCards().iterator().next());
		
		feePaymentService.saveToEdit(newFeePayment);
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 5);

		authenticate(null);
		feePaymentService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to pay a gym fee.
	 * Precondition: The user is a customer. The data is not valid.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testPayFeeCustomer5(){
		
		Collection<FeePayment> all;
		FeePayment newFeePayment;
		Collection<Gym> allPaid;
		Gym paidGym = null;
		Date activationMoment;
		
		authenticate("customer1");
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 4);
		
		allPaid = gymService.findAllWithFeePaymentActive();
		Assert.isTrue(allPaid.size() == 1);
		
		for(Gym i:allPaid){
			if(i.getName().equals("Gym Sevilla")){
				paidGym = i;
				break;
			}
		}
		
		
		newFeePayment = feePaymentService.create(paidGym.getId());
		
		activationMoment = new Date(2116, 9, 1, 00, 00);
		newFeePayment.setActiveMoment(activationMoment);
		newFeePayment.setCreditCard(customerService.findByPrincipal().getCreditCards().iterator().next());
		
		feePaymentService.saveToEdit(newFeePayment);
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 5);
		
		authenticate(null);
		feePaymentService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to pay a gym fee.
	 * Precondition: The user is a customer. The data is not valid.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testPayFeeCustomer6(){
		
		Collection<FeePayment> all;
		FeePayment newFeePayment;
		Collection<Gym> allPaid;
		Gym paidGym = null;
		Date activationMoment;
		
		authenticate("customer1");
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 4);
		
		allPaid = gymService.findAllWithoutFeePaymentActive();
		Assert.isTrue(allPaid.size() == 2);
		
		for(Gym i:allPaid){
			if(i.getName().equals("Gym Cádiz")){
				paidGym = i;
				break;
			}
		}
		
		
		newFeePayment = feePaymentService.create(paidGym.getId());
		
		activationMoment = new Date(2116, 9, 1, 00, 00);
		newFeePayment.setActiveMoment(activationMoment);
		newFeePayment.setCreditCard(customerService.findByPrincipal().getCreditCards().iterator().next());
		
		feePaymentService.saveToEdit(newFeePayment);
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 5);

		authenticate(null);
		feePaymentService.flush();
	}

	/**
	 * Description: A user who is authenticated as a customer must be able to pay a gym fee.
	 * Precondition: The user is a customer. The data is not valid.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testPayFeeCustomer7(){
		
		Collection<FeePayment> all;
		FeePayment newFeePayment;
		Collection<Gym> allPaid;
		Gym paidGym = null;
		Date activationMoment;
		
		authenticate("customer1");
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 4);
		
		allPaid = gymService.findAllWithFeePaymentActive();
		Assert.isTrue(allPaid.size() == 1);
		
		for(Gym i:allPaid){
			if(i.getName().equals("Gym Sevilla")){
				paidGym = i;
				break;
			}
		}
		
		
		newFeePayment = feePaymentService.create(paidGym.getId());
		
		activationMoment = new Date(0, 9, 1, 00, 00);
		newFeePayment.setActiveMoment(activationMoment);
		newFeePayment.setCreditCard(customerService.findByPrincipal().getCreditCards().iterator().next());
		
		feePaymentService.saveToEdit(newFeePayment);
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 5);
		
		authenticate(null);
		feePaymentService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to pay a gym fee.
	 * Precondition: The user is a customer. The data is not valid.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testPayFeeCustomer8(){
		
		Collection<FeePayment> all;
		FeePayment newFeePayment;
		Collection<Gym> allPaid;
		Gym paidGym = null;
		Date activationMoment;
		
		authenticate("customer1");
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 4);
		
		allPaid = gymService.findAllWithoutFeePaymentActive();
		Assert.isTrue(allPaid.size() == 2);
		
		for(Gym i:allPaid){
			if(i.getName().equals("Gym Cádiz")){
				paidGym = i;
				break;
			}
		}
		
		
		newFeePayment = feePaymentService.create(paidGym.getId());
		
		activationMoment = new Date(0, 9, 1, 00, 00);
		newFeePayment.setActiveMoment(activationMoment);
		newFeePayment.setCreditCard(customerService.findByPrincipal().getCreditCards().iterator().next());
		
		feePaymentService.saveToEdit(newFeePayment);
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 5);

		authenticate(null);
		feePaymentService.flush();
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to pay a gym fee.
	 * Precondition: The user is a customer. The data is not valid.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testPayFeeCustomer9(){
		
		Collection<FeePayment> all;
		FeePayment newFeePayment;
		Collection<Gym> allPaid;
		Gym paidGym = null;
		Date activationMoment;
		CreditCard creditCard;
		
		authenticate("customer1");
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 4);
		
		allPaid = gymService.findAllWithFeePaymentActive();
		Assert.isTrue(allPaid.size() == 1);
		
		for(Gym i:allPaid){
			if(i.getName().equals("Gym Sevilla")){
				paidGym = i;
				break;
			}
		}
		
		
		newFeePayment = feePaymentService.create(paidGym.getId());
		
		activationMoment = new Date(116, 9, 1, 00, 00);
		newFeePayment.setActiveMoment(activationMoment);
		
		creditCard = customerService.findByPrincipal().getCreditCards().iterator().next();
		creditCard.setExpirationYear(2015);
		newFeePayment.setCreditCard(creditCard);
		
		feePaymentService.saveToEdit(newFeePayment);
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 5);
		
		authenticate(null);
		feePaymentService.flush();
		
	}
	
	/**
	 * Description: A user who is authenticated as a customer must be able to pay a gym fee.
	 * Precondition: The user is a customer. The data is not valid.
	 * Return: FALSE
	 * Postcondition: -
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testPayFeeCustomer10(){
		
		Collection<FeePayment> all;
		FeePayment newFeePayment;
		Collection<Gym> allPaid;
		Gym paidGym = null;
		Date activationMoment;
		CreditCard creditCard;
		
		authenticate("customer1");
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 4);
		
		allPaid = gymService.findAllWithoutFeePaymentActive();
		Assert.isTrue(allPaid.size() == 2);
		
		for(Gym i:allPaid){
			if(i.getName().equals("Gym Cádiz")){
				paidGym = i;
				break;
			}
		}
		
		
		newFeePayment = feePaymentService.create(paidGym.getId());
		
		activationMoment = new Date(116, 9, 1, 00, 00);
		newFeePayment.setActiveMoment(activationMoment);

		creditCard = customerService.findByPrincipal().getCreditCards().iterator().next();
		creditCard.setExpirationYear(2015);
		newFeePayment.setCreditCard(creditCard);
		
		feePaymentService.saveToEdit(newFeePayment);
		
		all = feePaymentService.findAllByCustomer();
		Assert.isTrue(all.size() == 5);

		authenticate(null);
		feePaymentService.flush();
	}
}
