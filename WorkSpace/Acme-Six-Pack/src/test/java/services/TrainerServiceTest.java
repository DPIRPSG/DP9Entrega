package services;

import java.util.Collection;
import java.util.Iterator;

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
import domain.ServiceEntity;
import domain.Trainer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class TrainerServiceTest extends AbstractTest{
	
	// Service under test -------------------------
	
	@Autowired
	private TrainerService trainerService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private ServiceService serviceService;
	
	// Test ---------------------------------------
	
	/**
	 * Description: A user who is not authenticated must be able to search for a trainer using a single keyword that must appear in his or name, surname, or curriculum.
	 * Precondition: The given key word is found in a name.
	 * Return: TRUE
	 * Postcondition: All trainer that contains the given key word in its name, surname or curriculum are shown.
	 */
	@Test
	public void findAllTrainersByGivenKeyword1(){
		
		Collection<Trainer> all;
		
		all = trainerService.findBySingleKeyword("Pablo");
		
		Assert.isTrue(all.size() == 1);
		
		trainerService.flush();
		
	}
	
	/**
	 * Description: A user who is not authenticated must be able to search for a trainer using a single keyword that must appear in his or name, surname, or curriculum.
	 * Precondition: The given key word is found in a surname
	 * Return: TRUE
	 * Postcondition: All trainer that contains the given key word in its name, surname or curriculum are shown.
	 */
	@Test
	public void findAllTrainersByGivenKeyword2(){
		
		Collection<Trainer> all;
		
		all = trainerService.findBySingleKeyword("Gil");
		
		Assert.isTrue(all.size() == 1);
		
		trainerService.flush();
	}
	
	/**
	 * Description: A user who is not authenticated must be able to search for a trainer using a single keyword that must appear in his or name, surname, or curriculum.
	 * Precondition: The given key word is found in a surname
	 * Return: TRUE
	 * Postcondition: All trainer that contains the given key word in its name, surname or curriculum are shown.
	 */
	@Test
	public void findAllTrainersByGivenKeyword3(){
		
		Collection<Trainer> all;
		
		all = trainerService.findBySingleKeyword("Mata");
				
		Assert.isTrue(all.size() == 1);
		
		trainerService.flush();
	}
	
	/**
	 * Description: A user who is not authenticated must be able to search for a trainer using a single keyword that must appear in his or name, surname, or curriculum.
	 * Precondition: The given key word is found in a curriculum.
	 * Return: TRUE
	 * Postcondition: All trainer that contains the given key word in its name, surname or curriculum are shown.
	 */
	@Test
	public void findAllTrainersByGivenKeyword4(){
		
		Collection<Trainer> all;
		
		all = trainerService.findBySingleKeyword("No");
				
		Assert.isTrue(all.size() == 5);
		
		trainerService.flush();
	}
	
	/**
	 * Description: A user who is not authenticated must be able to search for a trainer using a single keyword that must appear in his or name, surname, or curriculum.
	 * Precondition: The given key word is found in a curriculum.
	 * Return: TRUE
	 * Postcondition: All trainer that contains the given key word in its name, surname or curriculum are shown.
	 */
	@Test
	public void findAllTrainersByGivenKeyword5(){
		
		Collection<Trainer> all;
		
		all = trainerService.findBySingleKeyword("Hololens");
				
		Assert.isTrue(all.size() == 0);
		
		trainerService.flush();
	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 7.2
	 * Navigate through the trainers.
	 */
	
	/**
	 * Positive test case: Listar los trainers
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Listar los trainers del sistema
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que el numero de trainers obtenido es 6
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testListTrainers() {
		// Declare variables
		Actor customer;
		Collection<Trainer> trainers;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		trainers = trainerService.findAll();
		
		// Checks results
		Assert.isTrue(trainers.size() == 6, "No se han listado los 6 trainers del sistema.");
		
		unauthenticate();

	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 10.1
	 * Register a new trainer to the system.
	 */
	
	/**
	 * Positive test case: Registrar un nuevo Trainer
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Registrar un nuevo Trainer
	 * 		- Comprobación
	 * 		+ Listar los trainers
	 * 		+ Comprobar que hay 1 más de los que había
	 * 		+ Comprobar que el nuevo trainer se encuentra entre ellos
	 * 		+ Cerrar su sesión
	 * 		+ Comprobar que puedes loguearte con el nuevo Trainer
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testRegisterTrainer() {
		// Declare variables
		Actor admin;
		Trainer trainer;
		UserAccount userAccount;
		Trainer trainerRegistered;
		Integer numberOfTrainers;
		Integer newNumberOfTrainers;
		Collection<Trainer> trainers;
		Actor authenticatedTrainer;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		numberOfTrainers = trainerService.findAll().size();
		
		trainer = trainerService.create();
		
		trainer.setName("Nuevo");
		trainer.setSurname("Trainer");
		trainer.setPhone("123456789");

		userAccount = userAccountService.create("TRAINER");
		
		userAccount.setUsername("nuevoTrainer");
		userAccount.setPassword("nuevoTrainer");
		
		trainer.setUserAccount(userAccount);
		
		trainerRegistered = trainerService.save(trainer);
		
		// Checks results
		trainers = trainerService.findAll();
		newNumberOfTrainers = trainers.size();
		
		Assert.isTrue(numberOfTrainers + 1 == newNumberOfTrainers, "El numero de trainers tras el registro no es el mismo que antes + 1"); // First check
		
		Assert.isTrue(trainers.contains(trainerRegistered), "El Trainer registrado no se encuentra entre los trainers del sistema."); // Second check
		
		unauthenticate();
		
		authenticate("nuevoTrainer");
		
		authenticatedTrainer = actorService.findByPrincipal();
		
		Assert.notNull(authenticatedTrainer, "No se ha podido loguear al trainer que se acaba de registrar."); // Third check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Registrar un nuevo Trainer sin estar autenticado
	 * 		- Acción
	 * 		+ Registrar un nuevo Trainer
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testRegisterTrainerAsUnauthenticated() {
		// Declare variables
//		Actor admin;
		Trainer trainer;
		UserAccount userAccount;
//		Trainer trainerRegistered;
//		Integer numberOfTrainers;
//		Integer newNumberOfTrainers;
//		Collection<Trainer> trainers;
//		Actor authenticatedTrainer;
		
		// Load objects to test
//		authenticate("admin");
//		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
//		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		numberOfTrainers = trainerService.findAll().size();
		
		trainer = trainerService.create();
		
		trainer.setName("Nuevo");
		trainer.setSurname("Trainer");
		trainer.setPhone("123456789");

		userAccount = userAccountService.create("TRAINER");
		
		userAccount.setUsername("nuevoTrainer");
		userAccount.setPassword("nuevoTrainer");
		
		trainer.setUserAccount(userAccount);
		
		trainerService.save(trainer);
		
		// Checks results
//		trainers = trainerService.findAll();
//		newNumberOfTrainers = trainers.size();
//		
//		Assert.isTrue(numberOfTrainers + 1 == newNumberOfTrainers, "El numero de trainers tras el registro no es el mismo que antes + 1"); // First check
//		
//		Assert.isTrue(trainers.contains(trainerRegistered), "El Trainer registrado no se encuentra entre los trainers del sistema."); // Second check
//		
//		unauthenticate();
//		
//		authenticate("nuevoTrainer");
//		
//		authenticatedTrainer = actorService.findByPrincipal();
//		
//		Assert.notNull(authenticatedTrainer, "No se ha podido loguear al trainer que se acaba de registrar."); // Third check
//		
//		unauthenticate();

	}
	
	/**
	 * Negative test case: Registrar un nuevo Trainer con username a null
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Registrar un nuevo Trainer con username nulo
	 * 		- Comprobación
	 * 		+ Listar los trainers
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testRegisterTrainerNullUsername() {
		// Declare variables
		Actor admin;
		Trainer trainer;
		UserAccount userAccount;
//		Trainer trainerRegistered;
//		Integer numberOfTrainers;
//		Integer newNumberOfTrainers;
//		Collection<Trainer> trainers;
//		Actor authenticatedTrainer;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		numberOfTrainers = trainerService.findAll().size();
		
		trainer = trainerService.create();
		
		trainer.setName("Nuevo");
		trainer.setSurname("Trainer");
		trainer.setPhone("123456789");

		userAccount = userAccountService.create("TRAINER");
		
		userAccount.setUsername(null);
		userAccount.setPassword("nuevoTrainer");
		
		trainer.setUserAccount(userAccount);
		
		trainerService.save(trainer);
		
		
		// Checks results
//		trainers = trainerService.findAll();
//		newNumberOfTrainers = trainers.size();
//		
//		Assert.isTrue(numberOfTrainers + 1 == newNumberOfTrainers, "El numero de trainers tras el registro no es el mismo que antes + 1"); // First check
//		
//		Assert.isTrue(trainers.contains(trainerRegistered), "El Trainer registrado no se encuentra entre los trainers del sistema."); // Second check
//		
//		unauthenticate();
//		
//		authenticate("nuevoTrainer");
//		
//		authenticatedTrainer = actorService.findByPrincipal();
//		
//		Assert.notNull(authenticatedTrainer, "No se ha podido loguear al trainer que se acaba de registrar."); // Third check
		
		unauthenticate();

	}
	
	/**
	 * Acme-Six-Pack-2.0 - Level B - 9.1
	 * Manage the list of services in which he or she specialises.
	 */
	
	/**
	 * Positive test case: Añadir un nuevo servicio especializado
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como trainer
	 * 		+ Añadirse un nuevo servicio especializado
	 * 		- Comprobación
	 * 		+ Listar sus servicios especializados
	 * 		+ Comprobar que hay 1 más de los que había
	 * 		+ Comprobar que el nuevo servicio se encuentra entre ellos
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testNewServiceSpecialised() {
		// Declare variables
		Actor trainer;
		Trainer trainerUser;
		Integer numberOfServices;
		Collection<ServiceEntity> newServices;
		Integer newnumberOfServices;
		Collection<ServiceEntity> services;
		ServiceEntity serviceToAdd;
		Iterator<ServiceEntity> serviceIterator;
		
		// Load objects to test
		authenticate("trainer1");
		trainer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(trainer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		trainerUser = trainerService.findByPrincipal();
		numberOfServices = trainerUser.getServices().size();
		
		services = serviceService.findAll();
		
		serviceIterator = services.iterator();
		serviceToAdd = serviceIterator.next();
		
		if(services.size() == numberOfServices){
			Assert.isTrue(false, "El Trainer ya tiene todos los servicios del sistema asignados como especializados.");
		}else{
			while(trainerUser.getServices().contains(serviceToAdd)){
				serviceToAdd = serviceIterator.next();
			}
		}
		
		trainerService.addService(serviceToAdd);
		
		
		// Checks results
		newServices = trainerUser.getServices();
		newnumberOfServices = newServices.size();
		
		Assert.isTrue(numberOfServices + 1 == newnumberOfServices, "El numero de servicios especializados del trainer o es el mismo que antes + 1."); // First check
		
		Assert.isTrue(newServices.contains(serviceToAdd), "El nuevo servicio no se encuentra entre los especializados del trainer."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Eliminar un servicio especializado
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como trainer
	 * 		+ Eliminarse un servicio especializado
	 * 		- Comprobación
	 * 		+ Listar sus servicios especializados
	 * 		+ Comprobar que hay 1 menos de los que había
	 * 		+ Comprobar que el servicio eliminado no se encuentra entre ellos
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testDeleteServiceSpecialised() {
		// Declare variables
		Actor trainer;
		Trainer trainerUser;
		Integer numberOfServices;
		Collection<ServiceEntity> newServices;
		Integer newnumberOfServices;
		Collection<ServiceEntity> services;
		ServiceEntity serviceToRemove;
		Iterator<ServiceEntity> serviceIterator;
		
		// Load objects to test
		authenticate("trainer5");
		trainer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(trainer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		trainerUser = trainerService.findByPrincipal();
		numberOfServices = trainerUser.getServices().size();
		
		services = serviceService.findAll();
		
		serviceIterator = services.iterator();
		
		serviceToRemove = serviceIterator.next();
		
		if(numberOfServices == 0){
			Assert.isTrue(false, "El Trainer no tiene servicios especializados para poder eliminar alguno.");
		}else{
			while(!trainerUser.getServices().contains(serviceToRemove) && !serviceToRemove.getName().equals("Fitness")){
				serviceToRemove = serviceIterator.next();
			}
		}
		
		trainerService.removeService(serviceToRemove);
		
		
		// Checks results
		newServices = trainerUser.getServices();
		newnumberOfServices = newServices.size();
		
		Assert.isTrue(numberOfServices - 1 == newnumberOfServices, "El numero de servicios especializados del trainer o es el mismo que antes + 1."); // First check
		
		Assert.isTrue(!newServices.contains(serviceToRemove), "El nuevo servicio se sigue encontrando entre los especializados del trainer."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Eliminar un servicio especializado con el que ya participes en alguna actividad
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como trainer
	 * 		+ Eliminarse un servicio especializado con el que ya participes en alguna actividad
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testDeleteServiceSpecialisedUsedInActivity() {
		// Declare variables
		Actor trainer;
		Trainer trainerUser;
		Integer numberOfServices;
		Collection<ServiceEntity> newServices;
		Integer newnumberOfServices;
		Collection<ServiceEntity> services;
		ServiceEntity serviceToRemove;
		Iterator<ServiceEntity> serviceIterator;
		
		// Load objects to test
		authenticate("trainer1");
		trainer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(trainer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		trainerUser = trainerService.findByPrincipal();
		numberOfServices = trainerUser.getServices().size();
		
		services = serviceService.findAll();
		
		serviceIterator = services.iterator();
		
		serviceToRemove = serviceIterator.next();
		
		if(numberOfServices == 0){
			Assert.isTrue(false, "El Trainer no tiene servicios especializados para poder eliminar alguno.");
		}else{
			while(!trainerUser.getServices().contains(serviceToRemove)){
				serviceToRemove = serviceIterator.next();
			}
		}
		
		trainerService.removeService(serviceToRemove);
		
		
		// Checks results
		newServices = trainerUser.getServices();
		newnumberOfServices = newServices.size();
		
		Assert.isTrue(numberOfServices - 1 == newnumberOfServices, "El numero de servicios especializados del trainer o es el mismo que antes + 1."); // First check
		
		Assert.isTrue(!newServices.contains(serviceToRemove), "El nuevo servicio se sigue encontrando entre los especializados del trainer."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Añadir un nuevo servicio especializado que ya tengas especializado
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como trainer
	 * 		+ Añadirse un nuevo servicio especializado que ya tengas especializado
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testNewServiceSpecialisedStillSpecialised() {
		// Declare variables
		Actor trainer;
		Trainer trainerUser;
		Integer numberOfServices;
//		Collection<ServiceEntity> newServices;
//		Integer newnumberOfServices;
		Collection<ServiceEntity> services;
		ServiceEntity serviceToAdd;
		Iterator<ServiceEntity> serviceIterator;
		
		// Load objects to test
		authenticate("trainer1");
		trainer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(trainer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		trainerUser = trainerService.findByPrincipal();
		numberOfServices = trainerUser.getServices().size();
		
		services = serviceService.findAll();
		
		serviceIterator = services.iterator();
		serviceToAdd = serviceIterator.next();
		
		if(services.size() == numberOfServices){
			Assert.isTrue(false, "El Trainer ya tiene todos los servicios del sistema asignados como especializados.");
		}else{
			while(!trainerUser.getServices().contains(serviceToAdd)){
				serviceToAdd = serviceIterator.next();
			}
		}
		
		trainerService.addService(serviceToAdd);
		
		
		// Checks results
//		newServices = trainerUser.getServices();
//		newnumberOfServices = newServices.size();
//		
//		Assert.isTrue(numberOfServices + 1 == newnumberOfServices, "El numero de servicios especializados del trainer no es el mismo que antes + 1."); // First check
//		
//		Assert.isTrue(newServices.contains(serviceToAdd), "El nuevo servicio no se encuentra entre los especializados del trainer."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Eliminar un servicio especializado que no tengas especializado (Test planteado como negativo, pero resultante como positivo al no saltar excepción alguna, si no ignorarlo en el servicio para que no borre nada, puesto que no tiene sentido borrar lo que se pide)
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como trainer
	 * 		+ Eliminarse un servicio especializado que no tengas especializado todavía
	 * 		- Comprobación
	 * 		+ Listar sus servicios especializados
	 * 		+ Comprobar que tiene especializados el mismo número de servicios que tenía
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testDeleteServiceSpecialisedNotSpecialied() {
		// Declare variables
		Actor trainer;
		Trainer trainerUser;
		Integer numberOfServices;
		Collection<ServiceEntity> newServices;
		Integer newnumberOfServices;
		Collection<ServiceEntity> services;
		ServiceEntity serviceToRemove;
		Iterator<ServiceEntity> serviceIterator;
		
		// Load objects to test
		authenticate("trainer1");
		trainer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(trainer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		trainerUser = trainerService.findByPrincipal();
		numberOfServices = trainerUser.getServices().size();
		
		services = serviceService.findAll();
		
		serviceIterator = services.iterator();
		
		serviceToRemove = serviceIterator.next();
		
		if(numberOfServices == 0){
			Assert.isTrue(false, "El Trainer no tiene servicios especializados para poder eliminar alguno.");
		}else{
			while(trainerUser.getServices().contains(serviceToRemove)){
				serviceToRemove = serviceIterator.next();
			}
		}
		
		trainerService.removeService(serviceToRemove);
		
		
		// Checks results
		newServices = trainerUser.getServices();
		newnumberOfServices = newServices.size();
		
		Assert.isTrue(numberOfServices == newnumberOfServices, "El numero de servicios especializados del trainer no es el mismo que antes."); // First check
		
		unauthenticate();

	}
	
}
