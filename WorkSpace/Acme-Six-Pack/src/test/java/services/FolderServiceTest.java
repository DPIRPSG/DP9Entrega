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
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Crearse una carpeta
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que el dueño de la carpeta creada es el actor logueado
	 * 		+ Comprobar que aparece la nueva carpeta creada
	 * 		+ Comprobar que tiene la cantidad de carpetas de antes más 1
	 * 		+ Cerrar su sesión
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
		
		Assert.isTrue(folderService.findAllByActor().size() == numberOfFolders + 1, "El actor no tiene el mismo número de carpetas que antes + 1 tras crearse una nueva carpeta"); // Third check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Crear un nuevo folder a otro usuario del sistema
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Crear una carpeta
	 * 		+ Asignarsela a otro usuario del sistema
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que salta una excepción del tipo: 
	 * 		+ Cerrar su sesión
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
//		Assert.isTrue(folderService.findAllByActor().size() == numberOfFolders + 1, "El actor no tiene el mismo número de carpetas que antes + 1 tras crearse una nueva carpeta"); // Third check
//		
		unauthenticate();

	}
	
	
	
	/**
	 * Negative test case: Crear un nuevo folder como folder del sistema (isSystem = true)
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Crearse una carpeta
	 * 		+ Hacerla carpeta del sistema
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que salta una excepción del tipo: 
	 * 		+ Cerrar su sesión
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
//		Assert.isTrue(folderService.findAllByActor().size() == numberOfFolders + 1, "El actor no tiene el mismo número de carpetas que antes + 1 tras crearse una nueva carpeta"); // Third check
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Renombrar un folder
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Renombrar una carpeta a "Carpeta renombrada"
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que el dueño de la carpeta renombrada es el actor logueado
	 * 		* Comprobar que el actor logueado posee dicha carpeta
	 * 		+ Comprobar que no existe carpeta creada con el nombre que tenía la carpeta renombrada
	 * 		+ Comprobar que existe una carpeta con el nombre "Carpeta renombrada"
	 * 		+ Cerrar su sesión
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
				Assert.isTrue(false, "La carpeta sigue teniendo el nombre que tenía antes de ser renombrada."); // Third check
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
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Renombrar una carpeta de otro usuario a "Carpeta renombrada"
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
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
//				Assert.isTrue(false, "La carpeta sigue teniendo el nombre que tenía antes de ser renombrada."); // Third check
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
	 * Negative test case: Renombrar un folder a un texto vacío
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Renombrar una carpeta a ""
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesión
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
//				Assert.isTrue(false, "La carpeta sigue teniendo el nombre que tenía antes de ser renombrada."); // Third check
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
	
	/**
	 * Positive test case: Eliminar un folder
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Eliminar una carpeta suya
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		* Comprobar que el actor logueado ya no posee dicha carpeta
	 * 		+ Comprobar que no existe carpeta creada con el nombre que tenía la carpeta eliminada
	 * 		+ Comprobar que el número de carpetas que posee es el mismo que antes - 1
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testDeleteFolder() {
		// Declare variables
		Actor customer;
		Folder folder;
		Collection<Folder> actorFolders;
		Collection<Folder> newActorFolders;
		String folderName;
		Integer numberOfFolders;
		Integer newNumberOfFolders;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		actorFolders = folderService.findAllByActor();
		numberOfFolders = actorFolders.size();
		
		folder = null;
		for(Folder f: actorFolders){
			if(f.getName().equals("MyBox")){
				folder = f;
			}
		}
		folderName = folder.getName();
		
		folderService.delete(folder);
		
		// Checks results
		newActorFolders = folderService.findAllByActor();
		newNumberOfFolders = newActorFolders.size();
		
		Assert.isTrue(!newActorFolders.contains(folder), "El usuario sigue teniendo asignada la carpeta que acaba de eliminar."); // First check
				
		for(Folder f: newActorFolders){
			if(f.getName() == folderName){
				Assert.isTrue(false, "El actor sigue teniendo una carpeta con el nombre de la carpeta que acaba de borrar."); // Second check
			}
		}
				
		Assert.isTrue( (numberOfFolders - 1 == newNumberOfFolders) , "El actor no tiene el mismo número de carpetas que antes - 1 al borrar una de sus carpetas."); // Fourth check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Eliminar un folder del sistema
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Eliminar una carpeta suya, pero que sea carpeta del sistema
	 * 		- Comprobación
	 * 		* Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testDeleteSystemFolder() {
		// Declare variables
		Actor customer;
		Folder folder;
		Collection<Folder> actorFolders;
//		Collection<Folder> newActorFolders;
//		String folderName;
//		Integer numberOfFolders;
//		Integer newNumberOfFolders;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		actorFolders = folderService.findAllByActor();
//		numberOfFolders = actorFolders.size();
		
		folder = actorFolders.iterator().next(); // folder es una carpeta del sistema
//		folderName = folder.getName();
		while(folder.getIsSystem() == false){
			folder = actorFolders.iterator().next();
		}
		
		folderService.delete(folder);
		
		// Checks results
//		newActorFolders = folderService.findAllByActor();
//		newNumberOfFolders = newActorFolders.size();
//		
//		Assert.isTrue(newActorFolders.contains(folder), "El usuario sigue teniendo asignada la carpeta que acaba de eliminar."); // First check
//				
//		for(Folder f: newActorFolders){
//			if(f.getName() == folderName){
//				Assert.isTrue(false, "El actor sigue teniendo una carpeta con el nombre de la carpeta que acaba de borrar."); // Second check
//			}
//		}
//				
//		Assert.isTrue( (numberOfFolders - 1 == newNumberOfFolders) , "El actor no tiene el mismo número de carpetas que antes - 1 al borrar una de sus carpetas."); // Fourth check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Eliminar un folder de otro usuario
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Eliminar una carpeta que no sea tuya
	 * 		- Comprobación
	 * 		* Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testDeleteSystemFolderOfOtherUser() {
		// Declare variables
		Actor customer;
		Folder folder;
//		Collection<Folder> actorFolders;
//		Collection<Folder> newActorFolders;
//		String folderName;
//		Integer numberOfFolders;
//		Integer newNumberOfFolders;
		
		// Load objects to test
		authenticate("customer2");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		actorFolders = folderService.findAllByActor();
//		numberOfFolders = actorFolders.size();
		
		folder = folderService.findOne(122); // Carpeta "MyBox", no del sistema, del customer1
//		folderName = folder.getName();
//		while(folder.getIsSystem() == false){
//			folder = actorFolders.iterator().next();
//		}
		
		folderService.delete(folder);
		
		// Checks results
//		newActorFolders = folderService.findAllByActor();
//		newNumberOfFolders = newActorFolders.size();
//		
//		Assert.isTrue(newActorFolders.contains(folder), "El usuario sigue teniendo asignada la carpeta que acaba de eliminar."); // First check
//				
//		for(Folder f: newActorFolders){
//			if(f.getName() == folderName){
//				Assert.isTrue(false, "El actor sigue teniendo una carpeta con el nombre de la carpeta que acaba de borrar."); // Second check
//			}
//		}
//				
//		Assert.isTrue( (numberOfFolders - 1 == newNumberOfFolders) , "El actor no tiene el mismo número de carpetas que antes - 1 al borrar una de sus carpetas."); // Fourth check
		
		unauthenticate();

	}
	
	
}
