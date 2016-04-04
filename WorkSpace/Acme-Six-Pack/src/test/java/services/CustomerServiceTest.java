package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.UserAccount;
import security.UserAccountService;
import utilities.AbstractTest;
import domain.Actor;
import domain.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class CustomerServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private CustomerService customerService;
	
	// Other services needed -----------------------
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private ActorService actorService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Six-Pack - Level C - 8.1
	 * Register to the system as a customer.
	 */
	
	/**
	 * Positive test case: Registrarse como Customer
	 * 		- Acción
	 * 		+ Entrar el registro como anónimo
	 * 		+ Rellenar los campos
	 * 		+ Presionar en registrarse
	 * 		- Comprobación
	 * 		+ Entrar al sistema con privilegios de administrador
	 * 		+ Comprobar que existe ese nuevo usuario entre los usuarios registrados
	 * 		+ Cerrar su sesión
	 * 		+ Comprobar que puede loguearse con el nuevo usuario creado
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testNewCustomer() {
		// Declare variables
		Customer customer;
		UserAccount userAccount;
		Customer customerRegistered;
		Actor authenticatedCustomer;
		
		// Load objects to test
		
		
		// Checks basic requirements
		
		
		// Execution of test
		customer = customerService.create();
		
		customer.setName("Nuevo");
		customer.setSurname("Customer");
		customer.setPhone("123456789");

		userAccount = userAccountService.create("CUSTOMER");
		
		userAccount.setUsername("nuevoCustomer");
		userAccount.setPassword("nuevoCustomer");
		
		customer.setUserAccount(userAccount);
		
		customerRegistered = customerService.save(customer);
		
		// Checks results
		authenticate("admin");
		Assert.isTrue(customerService.findAll().contains(customerRegistered), "El customer nuevo registrado no se encuentra entre los customers registrados en el sistema."); // First check
		unauthenticate();
		
		authenticate("customer1");
		
		authenticatedCustomer = actorService.findByPrincipal();
		
		Assert.notNull(authenticatedCustomer, "No se ha podido loguear al customer que se acaba de registrar."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Registrarse como Customer con contraseña a null
	 * 		- Acción
	 * 		+ Entrar el registro como anónimo
	 * 		+ Rellenar los campos y la contraseña a null
	 * 		+ Presionar en registrarse
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testNewCustomerNullPassword() {
		// Declare variables
		Customer customer;
		UserAccount userAccount;
//		Customer customerRegistered;
//		Actor authenticatedCustomer;
		
		// Load objects to test
		
		
		// Checks basic requirements
		
		
		// Execution of test
		customer = customerService.create();
		
		customer.setName("Nuevo");
		customer.setSurname("Customer");
		customer.setPhone("123456789");

		userAccount = userAccountService.create("CUSTOMER");
		
		userAccount.setUsername("nuevoCustomer");
		userAccount.setPassword(null);
		
		customer.setUserAccount(userAccount);
		
		customerService.save(customer);
		
		// Checks results
//		authenticate("admin");
//		Assert.isTrue(customerService.findAll().contains(customerRegistered), "El customer nuevo registrado no se encuentra entre los customers registrados en el sistema."); // First check
//		unauthenticate();
//		
//		authenticate("customer1");
//		
//		authenticatedCustomer = actorService.findByPrincipal();
//		
//		Assert.notNull(authenticatedCustomer, "No se ha podido loguear al customer que se acaba de registrar."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Registrarse como Customer estando identificado en el sistema
	 * 		- Acción
	 * 		+ Entrar el registro como customer
	 * 		+ Rellenar los campos y la contraseña a null
	 * 		+ Presionar en registrarse
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testNewCustomerAsCustomer() {
		// Declare variables
		Customer customer;
		UserAccount userAccount;
//		Customer customerRegistered;
//		Actor authenticatedCustomer;
		
		// Load objects to test
		authenticate("customer1");
		
		// Checks basic requirements
		
		
		// Execution of test
		customer = customerService.create();
		
		customer.setName("Nuevo");
		customer.setSurname("Customer");
		customer.setPhone("123456789");

		userAccount = userAccountService.create("CUSTOMER");
		
		userAccount.setUsername("nuevoCustomer");
		userAccount.setPassword("nuevoCustomer");
		
		customer.setUserAccount(userAccount);
		
		customerService.save(customer);
		
		// Checks results
//		authenticate("admin");
//		Assert.isTrue(customerService.findAll().contains(customerRegistered), "El customer nuevo registrado no se encuentra entre los customers registrados en el sistema."); // First check
//		unauthenticate();
//		
//		authenticate("customer1");
//		
//		authenticatedCustomer = actorService.findByPrincipal();
//		
//		Assert.notNull(authenticatedCustomer, "No se ha podido loguear al customer que se acaba de registrar."); // Second check
		
		unauthenticate();

	}
	
}
