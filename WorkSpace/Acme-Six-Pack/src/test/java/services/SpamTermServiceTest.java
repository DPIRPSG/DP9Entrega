package services;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

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
import domain.Actor;
import domain.SpamTerm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class SpamTermServiceTest extends AbstractTest {
	
	// Service under test -------------------------

	@Autowired
	private SpamTermService spamTermService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ActorService actorService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Six-Pack - Level B - 18.1
	 * Change the list of terms that the system uses to flag a message as spam.
	 */
	
	/**
	 * Positive test case: Listar todos
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		- Comprobación
	 * 		+ Comprobar que son 10 términos (los que deben estar por defecto en el sistema)
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testListSpamTerm() {
		// Declare variables
		Actor admin;
		Collection<SpamTerm> spamTerms;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "El usuario no logueado no es un administrador.");
		
		// Execution of test
		spamTerms = spamTermService.findAll();
		
		// Checks results
		Assert.isTrue(!spamTerms.isEmpty() && spamTerms.size() == 10, "No se han obtenido los 10 términos de spam esperados");
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Añadir uno nuevo
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Añadir el término "newSpamTerm"
	 * 		- Comprobación
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		+ Comprobar que son 11 términos
	 * 		+ Comprobar que uno de ellos es "newSpamTerm"
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testAddSpamTerm() {
		// Declare variables
		Actor admin;
		Collection<SpamTerm> spamTerms;
		SpamTerm spamTermToAdd;
		SpamTerm spamTermAdded;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "El usuario no logueado no es un administrador.");
		
		// Execution of test
		spamTermToAdd = spamTermService.create();
		spamTermToAdd.setTerm("newSpamTerm");
		
		spamTermAdded = spamTermService.save(spamTermToAdd);
		
		spamTerms = spamTermService.findAll();
		
		// Checks results
		Assert.isTrue(spamTerms.size() == 11, "No hay los 11 spamTerms que se esperaban en el sistema tras añadir uno nuevo.");
		Assert.isTrue(spamTerms.contains(spamTermAdded), "No existe el nuevo término añadido en la lista de términos de spam del sistema.");
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Editar una existente
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		+ Editar un término de la lista por "editedSpamTerm"
	 * 		- Comprobación
	 * 		+ Comprobar que siguen siendo 10 los términos del sistema
	 * 		+ Comprobar que no existe el término que ha sido editado
	 * 		+ Comrobar que "editedSpamTerm" se encuentra entre los términos del sistema
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testEditSpamTerm() {
		// Declare variables
		Actor admin;
		Collection<SpamTerm> spamTerms;
		SpamTerm spamTermToEdit;
		SpamTerm spamTermEdited;
		String originalSpamTermName;
		Collection<SpamTerm> spamTermsEdited;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "El usuario no logueado no es un administrador.");
		
		// Execution of test
		spamTerms = spamTermService.findAll();
		
		spamTermToEdit = spamTerms.iterator().next();
		
		originalSpamTermName = spamTermToEdit.getTerm();
		
		spamTermToEdit.setTerm("editedSpamTerm");
		spamTermEdited = spamTermService.save(spamTermToEdit);
		
		spamTermsEdited = spamTermService.findAll();
		
		// Checks results
		Assert.isTrue(!spamTerms.isEmpty() && spamTerms.size() == 10, "No se han obtenido los 10 términos de spam esperados tras la edición.");
		for(SpamTerm sp: spamTermsEdited){
			if(sp.getTerm().equals(originalSpamTermName)){
				Assert.isTrue(false, "El término de spam antes de ser editado sigue en la lista de términos del sistema.");
			}
		}
		Assert.isTrue(spamTermsEdited.contains(spamTermEdited), "El término de spam editado no aparece en la lista de términos del sistema.");
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Eliminar uno existente
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		+ Eliminar uno existente
	 * 		- Comprobación
	 * 		+ Comprobar que ahora hay 9 términos en el sistema
	 * 		+ Comprobar que el término que hemos eliminado no está entre esos 9 obtenidos
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testDeleteSpamTerm() {
		// Declare variables
		Actor admin;
		Collection<SpamTerm> spamTerms;
		SpamTerm spamTermToDelete;
		String originalSpamTermName;
		Collection<SpamTerm> spamTermsEdited;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "El usuario no logueado no es un administrador.");
		
		// Execution of test
		spamTerms = spamTermService.findAll();
		
		spamTermToDelete = spamTerms.iterator().next();
		
		originalSpamTermName = spamTermToDelete.getTerm();
		
		spamTermService.delete(spamTermToDelete);
		
		spamTermsEdited = spamTermService.findAll();
		
		// Checks results
		Assert.isTrue(!spamTermsEdited.isEmpty() && spamTermsEdited.size() == 9, "No se han obtenido los 9 términos de spam esperados tras la edición.");
		for(SpamTerm sp: spamTermsEdited){
			if(sp.getTerm().equals(originalSpamTermName)){
				Assert.isTrue(false, "El término de spam borrado sigue en la lista de términos del sistema.");
			}
		}
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Crear un término ya existente
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		+ Crear un nuevo término con el nombre de uno ya existente
	 * 		+ Eliminar uno existente
	 * 		- Comprobación
	 * 		+ Comprobar que salta la excepción de tipo: DataIntegrityViolationException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=DataIntegrityViolationException.class)
	@Rollback(value = true)
//	@Test
	public void testNewSpamTermThatExists() {
		// Declare variables
		Actor admin;
		Collection<SpamTerm> spamTerms;
		SpamTerm spamTermToAdd;
		String originalSpamTermName;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "El usuario no logueado no es un administrador.");
		
		// Execution of test
		spamTerms = spamTermService.findAll();
		
		originalSpamTermName = spamTerms.iterator().next().getTerm();
		
		spamTermToAdd = spamTermService.create();
		spamTermToAdd.setTerm(originalSpamTermName);
		
		spamTermService.save(spamTermToAdd);
		
		unauthenticate();
		
		spamTermService.flush();

	}
	
	/**
	 * Negative test case: Crear un término con user sin privilegios de administrador
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como cliente
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		+ Crear un nuevo término
	 * 		- Comprobación
	 * 		+ Comprobar que salta la excepción de tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
//	org.springframework.transaction.TransactionSystemException: Could not commit JPA transaction; nested exception is javax.persistence.RollbackException: Transaction marked as rollbackOnly
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testNewSpamTermNoAdmin() {
		// Declare variables
		Actor customer;
//		Collection<SpamTerm> spamTerms;
		SpamTerm spamTermToAdd;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		spamTerms = spamTermService.findAll();
		
		spamTermToAdd = spamTermService.create();
		spamTermToAdd.setTerm("newSpamTerm");
		
		spamTermService.save(spamTermToAdd);
		
		unauthenticate();
		
		spamTermService.flush();

	}
	
	/**
	 * Negative test case: Crear un término con el term (nombre) vacío
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		+ Crear un nuevo término con el term (nombre) vacío
	 * 		- Comprobación
	 * 		+ Comprobar que salta la excepción de tipo: ConstraintViolationException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test
	public void testNewSpamTermBlankName() {
		// Declare variables
		Actor admin;
//		Collection<SpamTerm> spamTerms;
		SpamTerm spamTermToAdd;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		spamTerms = spamTermService.findAll();
		
		spamTermToAdd = spamTermService.create();
		spamTermToAdd.setTerm("");
		
		spamTermService.save(spamTermToAdd);
		
		unauthenticate();
		
		spamTermService.flush();

	}
	
	/**
	 * Negative test case: Crear un término con el term (nombre) a null
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		+ Crear un nuevo término con el term (nombre) a null
	 * 		- Comprobación
	 * 		+ Comprobar que salta la excepción de tipo: ConstraintViolationException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test
	public void testNewSpamTermNullName() {
		// Declare variables
		Actor admin;
//		Collection<SpamTerm> spamTerms;
		SpamTerm spamTermToAdd;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		spamTerms = spamTermService.findAll();
		
		spamTermToAdd = spamTermService.create();
		spamTermToAdd.setTerm(null);
		
		spamTermService.save(spamTermToAdd);
		
		unauthenticate();
		
		spamTermService.flush();

	}
}