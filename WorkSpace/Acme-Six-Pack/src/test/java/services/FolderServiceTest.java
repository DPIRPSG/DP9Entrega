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
	 * Actors can create additional folders(, rename, or delete them).
	 */
	
	/**
	 * Positive test case
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Crearse una carpeta
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que aparece la nueva carpeta creada
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testNewFolder() {
		// Declare variables
		Actor customer;
		Folder folder;
		Folder newFolder;
		Collection<Folder> actorFolders;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		folder = folderService.create();
		folder.setName("Nueva carpeta");
		folder.setIsSystem(false);
		folder.setActor(customer);
		
		newFolder = folderService.save(folder);
		
		// Checks results
		folderService.checkActor(newFolder); // First check
		
		actorFolders = folderService.findAllByActor();
		Assert.isTrue(actorFolders.contains(newFolder), "El usuario no tiene asignada la carpeta que acaba de crearse."); // Second check
		
		unauthenticate();

	}
}
