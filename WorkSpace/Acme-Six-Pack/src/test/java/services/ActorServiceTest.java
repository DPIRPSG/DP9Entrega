package services;

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

import utilities.AbstractTest;
import domain.Actor;
import domain.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ActorServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private ActorService actorService;
	
	// Other services needed -----------------------
	
	@Autowired
	private CustomerService customerService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Six-Pack - Level C - 9.2
	 * Change his or her profile data.
	 */
	
	/**
	 * Positive test case: Modificar sus datos del perfil
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Editar sus datos de perfil
	 * 		- Comprobación
	 * 		+ Comprobar que los datos de su perfil son ahora los nuevos que ha introducido
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testEditProfile() {
		// Declare variables
		Actor customer;
		Customer customerUser;
		Customer customerEdited;
		String originalName;
		String originalSurname;
		String originalPhone;
		String originalUsername;
//		String originalPassword;
		String newName;
		String newSurname;
		String newPhone;
		String newUsername;
		String newPassword;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		customerUser = customerService.findByPrincipal();
		
		originalName = customerUser.getName();
		newName = originalName + "Edited";
		customerUser.setName(newName);
		
		originalSurname = customerUser.getSurname();
		newSurname = originalSurname + "Edited";
		customerUser.setSurname(newSurname);
		
		originalPhone = customerUser.getPhone();
		newPhone = originalPhone + "Edited";
		customerUser.setPhone(newPhone);
		
		originalUsername = customerUser.getUserAccount().getUsername();
		newUsername = originalUsername + "Edited";
		customerUser.getUserAccount().setUsername(newUsername);
		
//		originalPassword = customerUser.getUserAccount().getPassword();
		newPassword = "EditedPassword";
		customerUser.getUserAccount().setPassword(newPassword);
		
		customerEdited = customerService.save(customerUser);
		
		// Checks results
		Assert.isTrue(customerEdited.getName().equals(originalName + "Edited"), "El nombre no se ha editado correctamente.");
		Assert.isTrue(customerEdited.getSurname().equals(originalSurname + "Edited"), "El apellido no se ha editado correctamente.");
		Assert.isTrue(customerEdited.getPhone().equals(originalPhone + "Edited"), "El teléfono no se ha editado correctamente.");
		Assert.isTrue(customerEdited.getUserAccount().getUsername().equals(originalUsername + "Edited"), "El nombre de usuario no se ha editado correctamente.");
		Assert.isTrue(customerEdited.getUserAccount().getPassword().equals("EditedPassword"), "La contraseña no se ha editado correctamente.");
		
		unauthenticate();
		
		actorService.flush();

	}
	
	/**
	 * Negative test case: Modificar sus datos del perfil dejando campos en blanco
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Editar sus datos de perfil
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test
	public void testEditProfileBlankFields() {
		// Declare variables
		Actor customer;
		Customer customerUser;
//		Customer customerEdited;
//		String originalName;
//		String originalSurname;
//		String originalPhone;
		String originalUsername;
//		String originalPassword;
		String newName;
		String newSurname;
		String newPhone;
		String newUsername;
		String newPassword;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		customerUser = customerService.findByPrincipal();
		
//		originalName = customerUser.getName();
		newName = "";
		customerUser.setName(newName);
		
//		originalSurname = customerUser.getSurname();
		newSurname = "";
		customerUser.setSurname(newSurname);
		
//		originalPhone = customerUser.getPhone();
		newPhone = "";
		customerUser.setPhone(newPhone);
		
		originalUsername = customerUser.getUserAccount().getUsername();
		newUsername = originalUsername + "Edited";
		customerUser.getUserAccount().setUsername(newUsername);
		
//		originalPassword = customerUser.getUserAccount().getPassword();
		newPassword = "EditedPassword";
		customerUser.getUserAccount().setPassword(newPassword);
		
		customerService.save(customerUser);
		
		// Checks results
//		Assert.isTrue(customerEdited.getName().equals(originalName + "Edited"), "El nombre no se ha editado correctamente.");
//		Assert.isTrue(customerEdited.getSurname().equals(originalSurname + "Edited"), "El apellido no se ha editado correctamente.");
//		Assert.isTrue(customerEdited.getPhone().equals(originalPhone + "Edited"), "El teléfono no se ha editado correctamente.");
//		Assert.isTrue(customerEdited.getUserAccount().getUsername().equals(originalUsername + "Edited"), "El nombre de usuario no se ha editado correctamente.");
//		Assert.isTrue(customerEdited.getUserAccount().getPassword().equals("EditedPassword"), "La contraseña no se ha editado correctamente.");
		
		unauthenticate();
		
		actorService.flush();

	}
	
	/**
	 * Negative test case: Modificar sus datos del perfil con contraseña demasiado corta
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Editar sus datos de perfil eligiendo una contraseña demasiado corta
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test
	public void testEditProfileShortPassword() {
		// Declare variables
		Actor customer;
		Customer customerUser;
//		Customer customerEdited;
		String originalName;
		String originalSurname;
		String originalPhone;
		String originalUsername;
//		String originalPassword;
		String newName;
		String newSurname;
		String newPhone;
		String newUsername;
		String newPassword;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		customerUser = customerService.findByPrincipal();
		
		originalName = customerUser.getName();
		newName = originalName + "Edited";
		customerUser.setName(newName);
		
		originalSurname = customerUser.getSurname();
		newSurname = originalSurname + "Edited";
		customerUser.setSurname(newSurname);
		
		originalPhone = customerUser.getPhone();
		newPhone = originalPhone + "Edited";
		customerUser.setPhone(newPhone);
		
		originalUsername = customerUser.getUserAccount().getUsername();
		newUsername = originalUsername + "Edited";
		customerUser.getUserAccount().setUsername(newUsername);
		
//		originalPassword = customerUser.getUserAccount().getPassword();
		newPassword = "pass";
		customerUser.getUserAccount().setPassword(newPassword);
		
		customerService.save(customerUser);
		
		// Checks results
//		Assert.isTrue(customerEdited.getName().equals(originalName + "Edited"), "El nombre no se ha editado correctamente.");
//		Assert.isTrue(customerEdited.getSurname().equals(originalSurname + "Edited"), "El apellido no se ha editado correctamente.");
//		Assert.isTrue(customerEdited.getPhone().equals(originalPhone + "Edited"), "El teléfono no se ha editado correctamente.");
//		Assert.isTrue(customerEdited.getUserAccount().getUsername().equals(originalUsername + "Edited"), "El nombre de usuario no se ha editado correctamente.");
//		Assert.isTrue(customerEdited.getUserAccount().getPassword().equals("EditedPassword"), "La contraseña no se ha editado correctamente.");
		
		unauthenticate();
		
		actorService.flush();

	}
	
}
