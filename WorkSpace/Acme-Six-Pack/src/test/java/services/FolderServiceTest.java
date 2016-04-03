package services;

import java.util.Collection;

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
import domain.Folder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class FolderServiceTest extends AbstractTest {
	
	// Service under test -------------------------

	@Autowired
	private FolderService folderService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ActorService actorService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Six-Pack - Level B - 17.2
	 * Actors can create additional folders, rename, or delete them.
	 */
	
	/**
	 * Positive test case: Crear un nuevo folder
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema
	 * 		+ Crearse una carpeta
	 * 		- Comprobaci�n
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que el due�o de la carpeta creada es el actor logueado
	 * 		+ Comprobar que aparece la nueva carpeta creada
	 * 		+ Comprobar que tiene la cantidad de carpetas de antes m�s 1
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testNewFolder() {
		// Declare variables
		Actor customer;
		Folder folder;
		Folder newFolder;
		Collection<Folder> actorFolders;
		Integer numberOfFolders;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		numberOfFolders = folderService.findAllByActor().size();
		
		folder = folderService.create();
		folder.setName("Nueva carpeta");
		folder.setIsSystem(false);
		folder.setActor(customer);
		
		newFolder = folderService.save(folder);
		
		// Checks results
		folderService.checkActor(newFolder); // First check
		
		actorFolders = folderService.findAllByActor();
		Assert.isTrue(actorFolders.contains(newFolder), "El usuario no tiene asignada la carpeta que acaba de crearse."); // Second check
		
		Assert.isTrue(folderService.findAllByActor().size() == numberOfFolders + 1, "El actor no tiene el mismo n�mero de carpetas que antes + 1 tras crearse una nueva carpeta"); // Third check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Crear un nuevo folder a otro usuario del sistema
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema
	 * 		+ Crear una carpeta
	 * 		+ Asignarsela a otro usuario del sistema
	 * 		- Comprobaci�n
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que salta una excepci�n del tipo: 
	 * 		+ Cerrar su sesi�n
	 */
	
	// CORREGIR
	@Test 
	public void testNewFolderForAnotherUser() {
		// Declare variables
		Actor customer;
		Actor otherCustomer;
		Folder folder;
//		Folder newFolder;
//		Collection<Folder> actorFolders;
//		Integer numberOfFolders;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		otherCustomer = actorService.findOne(71); // Id del customer2
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		numberOfFolders = folderService.findAllByActor().size();
		
		folder = folderService.create();
		folder.setName("Nueva carpeta");
		folder.setIsSystem(false);
		folder.setActor(otherCustomer);
		
		folderService.save(folder);
		
		// Checks results
//		folderService.checkActor(newFolder); // First check
//		
//		actorFolders = folderService.findAllByActor();
//		Assert.isTrue(actorFolders.contains(newFolder), "El usuario no tiene asignada la carpeta que acaba de crearse."); // Second check
//		
//		Assert.isTrue(folderService.findAllByActor().size() == numberOfFolders + 1, "El actor no tiene el mismo n�mero de carpetas que antes + 1 tras crearse una nueva carpeta"); // Third check
//		
		unauthenticate();

	}
	
	
	
	/**
	 * Negative test case: Crear un nuevo folder como folder del sistema (isSystem = true)
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema
	 * 		+ Crearse una carpeta
	 * 		+ Hacerla carpeta del sistema
	 * 		- Comprobaci�n
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que salta una excepci�n del tipo: 
	 * 		+ Cerrar su sesi�n
	 */
	
	// CORREGIR
	@Test 
	public void testNewFolderIsSystem() {
		// Declare variables
		Actor customer;
		Folder folder;
//		Folder newFolder;
//		Collection<Folder> actorFolders;
//		Integer numberOfFolders;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		numberOfFolders = folderService.findAllByActor().size();
		
		folder = folderService.create();
		folder.setName("Nueva carpeta");
		folder.setIsSystem(true);
		folder.setActor(customer);
		
		folderService.save(folder);
		
		// Checks results
//		folderService.checkActor(newFolder); // First check
//		
//		actorFolders = folderService.findAllByActor();
//		Assert.isTrue(actorFolders.contains(newFolder), "El usuario no tiene asignada la carpeta que acaba de crearse."); // Second check
//		
//		Assert.isTrue(folderService.findAllByActor().size() == numberOfFolders + 1, "El actor no tiene el mismo n�mero de carpetas que antes + 1 tras crearse una nueva carpeta"); // Third check
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Renombrar un folder
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema
	 * 		+ Renombrar una carpeta a "Carpeta renombrada"
	 * 		- Comprobaci�n
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que el due�o de la carpeta renombrada es el actor logueado
	 * 		* Comprobar que el actor logueado posee dicha carpeta
	 * 		+ Comprobar que no existe carpeta creada con el nombre que ten�a la carpeta renombrada
	 * 		+ Comprobar que existe una carpeta con el nombre "Carpeta renombrada"
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testRenameFolder() {
		// Declare variables
		Actor customer;
		Folder folder;
		Folder renamedFolder;
		Collection<Folder> actorFolders;
		Collection<Folder> newActorFolders;
		String oldFolderName;
		Boolean existNewFolderName;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		existNewFolderName = false;
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		actorFolders = folderService.findAllByActor();
		folder = actorFolders.iterator().next();
		oldFolderName = folder.getName();
		
		folder.setName("Carpeta renombrada");
		
		renamedFolder = folderService.save(folder);
		
		// Checks results
		folderService.checkActor(renamedFolder); // First check
		
		newActorFolders = folderService.findAllByActor();
		Assert.isTrue(newActorFolders.contains(renamedFolder), "El usuario no tiene asignada la carpeta que acaba de renombrar."); // Second check
				
		for(Folder f: newActorFolders){
			if(f.getName() == oldFolderName){
				Assert.isTrue(false, "La carpeta sigue teniendo el nombre que ten�a antes de ser renombrada."); // Third check
			}
			if(f.getName() == "Carpeta renombrada"){
				existNewFolderName = true;
			}
		}
		
		Assert.isTrue(existNewFolderName, "El actor logueado no tiene ninguna carpeta con el nombre (\"Carpeta renombrada\") que se le ha dado a la carpeta renombrada."); // Fourth check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Renombrar un folder de otro usuario
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema
	 * 		+ Renombrar una carpeta de otro usuario a "Carpeta renombrada"
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta una excepci�n del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testRenameFolderOfOther() {
		// Declare variables
		Actor customer;
		Folder folder;
//		Folder renamedFolder;
//		Collection<Folder> actorFolders;
//		Collection<Folder> newActorFolders;
//		String oldFolderName;
//		Boolean existNewFolderName;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
//		existNewFolderName = false;
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		folder = folderService.findOne(86); // Id de la carpeta InBox del customer2
//		oldFolderName = folder.getName();
		
		folder.setName("Carpeta renombrada");
		
		folderService.save(folder);
		
		// Checks results
//		folderService.checkActor(renamedFolder); // First check
//		
//		newActorFolders = folderService.findAllByActor();
//		Assert.isTrue(newActorFolders.contains(renamedFolder), "El usuario no tiene asignada la carpeta que acaba de renombrar."); // Second check
//				
//		for(Folder f: newActorFolders){
//			if(f.getName() == oldFolderName){
//				Assert.isTrue(false, "La carpeta sigue teniendo el nombre que ten�a antes de ser renombrada."); // Third check
//			}
//			if(f.getName() == "Carpeta renombrada"){
//				existNewFolderName = true;
//			}
//		}
//		
//		Assert.isTrue(existNewFolderName, "El actor logueado no tiene ninguna carpeta con el nombre (\"Carpeta renombrada\") que se le ha dado a la carpeta renombrada."); // Fourth check
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Renombrar un folder a un texto vac�o
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema
	 * 		+ Renombrar una carpeta a ""
	 * 		- Comprobaci�n
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que salta una excepci�n del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test
	public void testRenameFolderToBlank() {
		// Declare variables
		Actor customer;
		Folder folder;
//		Folder renamedFolder;
		Collection<Folder> actorFolders;
//		Collection<Folder> newActorFolders;
//		String oldFolderName;
//		Boolean existNewFolderName;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
//		existNewFolderName = false;
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		actorFolders = folderService.findAllByActor();
		folder = actorFolders.iterator().next();
//		oldFolderName = folder.getName();
		
		folder.setName("");
		
		folderService.save(folder);
		
		// Checks results
//		folderService.checkActor(renamedFolder); // First check
//		
//		newActorFolders = folderService.findAllByActor();
//		Assert.isTrue(newActorFolders.contains(renamedFolder), "El usuario no tiene asignada la carpeta que acaba de renombrar."); // Second check
//				
//		for(Folder f: newActorFolders){
//			if(f.getName() == oldFolderName){
//				Assert.isTrue(false, "La carpeta sigue teniendo el nombre que ten�a antes de ser renombrada."); // Third check
//			}
//			if(f.getName() == "Carpeta renombrada"){
//				existNewFolderName = true;
//			}
//		}
//		
//		Assert.isTrue(existNewFolderName, "El actor logueado no tiene ninguna carpeta con el nombre (\"Carpeta renombrada\") que se le ha dado a la carpeta renombrada."); // Fourth check
		
		unauthenticate();
		
		folderService.flush();

	}
	
	
}