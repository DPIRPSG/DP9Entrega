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
import domain.Message;

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
		
		newFolder = folderService.saveToEdit(folder);
		
		// Checks results
		folderService.checkActor(newFolder); // First check
		
		actorFolders = folderService.findAllByActor();
		Assert.isTrue(actorFolders.contains(newFolder), "El usuario no tiene asignada la carpeta que acaba de crearse."); // Second check
		
		Assert.isTrue(folderService.findAllByActor().size() == numberOfFolders + 1, "El actor no tiene el mismo n�mero de carpetas que antes + 1 tras crearse una nueva carpeta"); // Third check
		
		unauthenticate();

	}
	
//	/**
//	 * Negative test case: Crear un nuevo folder a otro usuario del sistema
//	 * 		- Acci�n
//	 * 		+ Autenticarse en el sistema
//	 * 		+ Crear una carpeta
//	 * 		+ Asignarsela a otro usuario del sistema
//	 * 		- Comprobaci�n
//	 * 		+ Listar sus carpetas
//	 * 		+ Comprobar que salta una excepci�n del tipo: 
//	 * 		+ Cerrar su sesi�n
//	 */
//	
//	// CORREGIR
//	@Test 
//	public void testNewFolderForAnotherUser() {
//		// Declare variables
//		Actor customer;
//		Actor otherCustomer;
//		Folder folder;
//		Folder newFolder;
//		Collection<Folder> actorFolders;
//		Integer numberOfFolders;
//		
//		// Load objects to test
//		authenticate("customer1");
//		customer = actorService.findByPrincipal();
//		otherCustomer = actorService.findOne(71); // Id del customer2
//		
//		// Checks basic requirements
//		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
//		
//		// Execution of test
//		numberOfFolders = folderService.findAllByActor().size();
//		
//		folder = folderService.create();
//		folder.setName("Nueva carpeta");
//		folder.setIsSystem(false);
//		folder.setActor(otherCustomer);
//		
//		newFolder = folderService.saveToEdit(folder);
//		
//		// Checks results
//		folderService.checkActor(newFolder); // First check
//		
//		actorFolders = folderService.findAllByActor();
//		Assert.isTrue(actorFolders.contains(newFolder), "El usuario no tiene asignada la carpeta que acaba de crearse."); // Second check
//		
//		Assert.isTrue(folderService.findAllByActor().size() == numberOfFolders + 1, "El actor no tiene el mismo n�mero de carpetas que antes + 1 tras crearse una nueva carpeta"); // Third check
//		
//		unauthenticate();
//
//	}
	
	
	
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
		Folder newFolder;
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
		
		newFolder = folderService.saveToEdit(folder);
		
		// Checks results
		System.out.println(newFolder.getIsSystem());
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
		
		renamedFolder = folderService.saveToEdit(folder);
		
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
		
		folderService.saveToEdit(folder);
		
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
	 * Negative test case: Renombrar un folder a un texto vac�o
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
		
		folderService.saveToEdit(folder);
		
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
	
	/**
	 * Positive test case: Eliminar un folder
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema
	 * 		+ Eliminar una carpeta suya
	 * 		- Comprobaci�n
	 * 		+ Listar sus carpetas
	 * 		* Comprobar que el actor logueado ya no posee dicha carpeta
	 * 		+ Comprobar que no existe carpeta creada con el nombre que ten�a la carpeta eliminada
	 * 		+ Comprobar que el n�mero de carpetas que posee es el mismo que antes - 1
	 * 		+ Cerrar su sesi�n
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
				
		Assert.isTrue( (numberOfFolders - 1 == newNumberOfFolders) , "El actor no tiene el mismo n�mero de carpetas que antes - 1 al borrar una de sus carpetas."); // Fourth check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Eliminar un folder del sistema
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema
	 * 		+ Eliminar una carpeta suya, pero que sea carpeta del sistema
	 * 		- Comprobaci�n
	 * 		* Comprobar que salta una excepci�n del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesi�n
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
//		Assert.isTrue( (numberOfFolders - 1 == newNumberOfFolders) , "El actor no tiene el mismo n�mero de carpetas que antes - 1 al borrar una de sus carpetas."); // Fourth check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Eliminar un folder de otro usuario
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema
	 * 		+ Eliminar una carpeta que no sea tuya
	 * 		- Comprobaci�n
	 * 		* Comprobar que salta una excepci�n del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesi�n
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
//		Assert.isTrue( (numberOfFolders - 1 == newNumberOfFolders) , "El actor no tiene el mismo n�mero de carpetas que antes - 1 al borrar una de sus carpetas."); // Fourth check
		
		unauthenticate();

	}
	
	/**
	 * Acme-Six-Pack - Level B - 17.2
	 * When a message is deleted from a folder other than "trash box", it is moved to "trash box"
	 */
	
	/**
	 * Positive test case: Borrar un mensaje de una carpeta
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema
	 * 		+ Entrar a una carpeta
	 * 		+ Borrar un mensaje de ella
	 * 		- Comprobaci�n
	 * 		+ Entrar a la carpeta
	 * 		+ Comprobar que el mensaje no sigue en esa carpeta
	 * 		+ Comprobar que el mensaje est� ahora en la carpeta TrashBox
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testDeleteMessage() {
		// Declare variables
		Actor customer;
		Folder folder;
		Collection<Folder> actorFolders;
		Message message;
		Collection<Folder> newActorFolders;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		actorFolders = folderService.findAllByActor();
		
		folder = null;
		for(Folder f: actorFolders){
			if(f.getName().equals("InBox")){
				folder = f;
			}
		}
		Assert.notNull(folder, "No est� el folder \"InBox\" necesario para realizar el test.");
		
		message = null;
		for(Message m: folder.getMessages()){
			message = m;
		}
		Assert.notNull(message, "No hay ning�n mensaje en la carpeta, necesario para realizar el test.");
		
		folderService.removeMessage(folder, message);
		
		// Checks results
		newActorFolders = folderService.findAllByActor();
		
		folder = null;
		for(Folder f: newActorFolders){
			if(f.getName().equals("InBox")){
				folder = f;
			}
		}
		
		Assert.isTrue(!folder.getMessages().contains(message), "La carpeta todav�a contiene el mensaje eliminado."); // First check
		
		folder = null;
		for(Folder f: newActorFolders){
			if(f.getName().equals("TrashBox")){
				folder = f;
			}
		}
		
		Assert.isTrue(folder.getMessages().contains(message), "La carpeta TrashBox no contiene el mensaje eliminado."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Borrar un mensaje de la carpeta TrashBox
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema
	 * 		+ Entrar a la carpeta TrashBox
	 * 		+ Borrar un mensaje de ella
	 * 		- Comprobaci�n
	 * 		+ Entrar a la carpeta
	 * 		+ Comprobar que el mensaje no est� ahora en la carpeta TrashBox
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testDeleteMessageFromTrashBox() {
		// Declare variables
		Actor customer;
		Folder folder;
		Collection<Folder> actorFolders;
		Message message;
		Collection<Folder> newActorFolders;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		actorFolders = folderService.findAllByActor();
		
		folder = null;
		for(Folder f: actorFolders){
			if(f.getName().equals("TrashBox")){
				folder = f;
			}
		}
		Assert.notNull(folder, "No est� el folder \"TrashBox\" necesario para realizar el test.");
		
		message = null;
		for(Message m: folder.getMessages()){
			message = m;
		}
		Assert.notNull(message, "No hay ning�n mensaje en la carpeta, necesario para realizar el test.");
		
		folderService.removeMessage(folder, message);
		
		// Checks results
		newActorFolders = folderService.findAllByActor();
		
		folder = null;
		for(Folder f: newActorFolders){
			if(f.getName().equals("TrashBox")){
				folder = f;
			}
		}
		
		Assert.isTrue(!folder.getMessages().contains(message), "La carpeta TrashBox sigue conteniendo el mensaje eliminado."); // First check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Borrar un mensaje de otro usuario
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema
	 * 		+ Borrar un mensaje de otro usuario
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta una excepci�n del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testDeleteMessageOfOther() {
		// Declare variables
		Actor customer;
		Folder folder;
//		Collection<Folder> actorFolders;
		Message message;
//		Collection<Folder> newActorFolders;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		actorFolders = folderService.findAllByActor();
		
		folder = folderService.findOne(86); // Carpeta InBox del customer2
		Assert.notNull(folder, "No est� el folder necesario para realizar el test.");
		
		message = null;
		for(Message m: folder.getMessages()){
			message = m;
		}
		Assert.notNull(message, "No hay ning�n mensaje en la carpeta, necesario para realizar el test.");
		
		folderService.removeMessage(folder, message);
		
		// Checks results
//		newActorFolders = folderService.findAllByActor();
//		
//		folder = null;
//		for(Folder f: newActorFolders){
//			if(f.getName().equals("InBox")){
//				folder = f;
//			}
//		}
//		
//		Assert.isTrue(!folder.getMessages().contains(message), "La carpeta todav�a contiene el mensaje eliminado."); // First check
//		
//		folder = null;
//		for(Folder f: newActorFolders){
//			if(f.getName().equals("TrashBox")){
//				folder = f;
//			}
//		}
//		
//		Assert.isTrue(folder.getMessages().contains(message), "La carpeta TrashBox no contiene el mensaje eliminado."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Borrar un mensaje null de una carpeta null
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema
	 * 		+ Borrar un mensaje null de una carpeta null
	 * 		- Comprobaci�n
	 * 		+ Entrar a la carpeta
	 * 		+ Comprobar que salta una execpci�n del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testDeleteNullMessageFromNullFolder() {
		// Declare variables
		Actor customer;
//		Folder folder;
//		Folder otherFolder;
//		Collection<Folder> actorFolders;
//		Message message;
//		Collection<Folder> newActorFolders;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		actorFolders = folderService.findAllByActor();
//		
//		folder = null;
//		for(Folder f: actorFolders){
//			if(f.getName().equals("InBox")){
//				folder = f;
//			}
//		}
//		Assert.notNull(folder, "No est� el folder \"InBox\" necesario para realizar el test.");
//		
//		otherFolder = null;
//		for(Folder f: actorFolders){
//			if(f.getName().equals("OutBox")){
//				otherFolder = f;
//			}
//		}
//		Assert.notNull(otherFolder, "No est� el folder \"OutBox\" necesario para realizar el test.");
//		
//		message = null;
//		for(Message m: folder.getMessages()){
//			message = m;
//		}
//		Assert.notNull(message, "No hay ning�n mensaje en la carpeta, necesario para realizar el test.");
		
		folderService.removeMessage(null, null);
		
		// Checks results
//		newActorFolders = folderService.findAllByActor();
//		
//		folder = null;
//		for(Folder f: newActorFolders){
//			if(f.getName().equals("InBox")){
//				folder = f;
//			}
//		}
//		
//		Assert.isTrue(folder.getMessages().contains(message), "La carpeta ya no contiene el mensaje, y deber�a."); // First check
//		
//		folder = null;
//		for(Folder f: newActorFolders){
//			if(f.getName().equals("TrashBox")){
//				folder = f;
//			}
//		}
//		
//		Assert.isTrue(!folder.getMessages().contains(message), "El mensaje ha sido enviado a TrashBox sin haber sido eliminado de ninguna carpeta."); // Second check
		
		unauthenticate();

	}
	
	
}
