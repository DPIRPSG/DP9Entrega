package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
public class MessageServiceTest extends AbstractTest {
	
	// Service under test -------------------------

	@Autowired
	private MessageService messageService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ActorService actorService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Six-Pack - Level B - 17.1
	 * Exchange messages with other users.
	 */
	
	/**
	 * Positive test case
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Enviar un mensaje a otro usuario
	 * 		- Comprobación
	 * 		+ Ver su carpeta de OutBox
	 * 		+ Comprobar que el mensaje se encuentra en la misma
	 * 		+ Cerrar su sesión en el sistema
	 * 		+ Autenticarse con los otros usuarios
	 * 		+ Ver sus carpetas de InBox
	 * 		+ Comprobar que el mensaje se encuentra en dicha carpeta
	 * 		+ Cerrar sus sesiones
	 */
	
	@Test 
	public void testExchangeMessage() {
		// Declare variables
		Actor customer;
		Message message;
		Message sentMessage;
		Date sentMoment;
		Collection<Actor> allActors;
		Collection<Actor> recipients;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		message = messageService.create();
		message.setSubject("Aquí lo tienes");
		message.setBody("Aquí tienes tu nuevo mensaje");
		sentMoment = new Date();
		message.setSentMoment(sentMoment);
		
		message.setSender(customer);
		
		allActors = actorService.findAll();
		
		recipients = new ArrayList<Actor>();
		for(int i = 0; i<2; i++){
			recipients.add(allActors.iterator().next());
		}
		message.setRecipients(recipients);
		
		sentMessage = messageService.firstSave(message);
		
		// Checks results
		for(Folder f: customer.getMessageBoxes()){
			if(f.getName().equals("OutBox")){
				Assert.isTrue(f.getMessages().contains(sentMessage), "El mensaje no ha sido añadido a la carpeta OutBox del emisor");
			}
		}
		
		unauthenticate();
		
		for(Actor a: recipients){
			authenticate(a.getUserAccount().getUsername());
			for(Folder f: a.getMessageBoxes()){
				if(f.getName().equals("InBox")){
					Assert.isTrue(f.getMessages().contains(sentMessage), "El mensaje no ha sido añadido a la carpeta InBox del receptor");
				}
			}
			unauthenticate();
		}

	}
	
	/**
	 * Negative test case: Send a message to anyone
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Enviar un mensaje sin seleccionar ningún receptor
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 * 		+ Ver su carpeta de OutBox
	 * 		+ Comprobar que el mensaje se encuentra en la misma
	 * 		+ Cerrar su sesión en el sistema
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test
	public void testExchangeMessageToAnyone() {
		// Declare variables
		Actor customer;
		Message message;
//		Message sentMessage;
		Date sentMoment;
//		Collection<Actor> allActors;
//		Collection<Actor> recipients;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		message = messageService.create();
		message.setSubject("Aquí lo tienes");
		message.setBody("Aquí tienes tu nuevo mensaje");
		sentMoment = new Date();
		message.setSentMoment(sentMoment);
		
		message.setSender(customer);
		
//		allActors = actorService.findAll();
//		
//		recipients = new ArrayList<Actor>();
//		for(int i = 0; i<2; i++){
//			recipients.add(allActors.iterator().next());
//		}
//		message.setRecipients(recipients);
		
		messageService.firstSave(message);
		
		// Checks results
//		for(Folder f: customer.getMessageBoxes()){
//			if(f.getName().equals("OutBox")){
//				Assert.isTrue(!f.getMessages().contains(sentMessage), "El mensaje ha sido añadido a la carpeta OutBox del emisor a pesar de que ha fallado");
//			}
//		}
		
		unauthenticate();
		
//		System.out.println(sentMessage.getRecipients());
		
//		for(Actor a: recipients){
//			authenticate(a.getUserAccount().getUsername());
//			for(Folder f: a.getMessageBoxes()){
//				if(f.getName().equals("InBox")){
//					Assert.isTrue(f.getMessages().contains(sentMessage), "El mensaje no ha sido añadido a la carpeta InBox del receptor");
//				}
//			}
//			unauthenticate();
//		}
		
		messageService.flush();

	}
	
	/**
	 * Negative test case: Send a message from anonymous user
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Enviar un mensaje como anónimo
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: NullPointerException
	 * 		+ Cerrar su sesión en el sistema
	 */
	
	@Test(expected=NullPointerException.class)
	@Rollback(value = true)
//	@Test
	public void testExchangeMessageAnonymous() {
		// Declare variables
		Actor customer;
		Message message;
		Message sentMessage;
		Date sentMoment;
		Collection<Actor> allActors;
		Collection<Actor> recipients;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		message = messageService.create();
		message.setSubject("Aquí lo tienes");
		message.setBody("Aquí tienes tu nuevo mensaje");
		sentMoment = new Date();
		message.setSentMoment(sentMoment);
		
		message.setSender(null);
		
		allActors = actorService.findAll();
		
		recipients = new ArrayList<Actor>();
		for(int i = 0; i<2; i++){
			recipients.add(allActors.iterator().next());
		}
		message.setRecipients(recipients);
		
		sentMessage = messageService.firstSave(message);
		
		// Checks results
		for(Folder f: customer.getMessageBoxes()){
			if(f.getName().equals("OutBox")){
				Assert.isTrue(f.getMessages().contains(sentMessage), "El mensaje no ha sido añadido a la carpeta OutBox del emisor");
			}
		}
		
		unauthenticate();
		
		for(Actor a: recipients){
			authenticate(a.getUserAccount().getUsername());
			for(Folder f: a.getMessageBoxes()){
				if(f.getName().equals("InBox")){
					Assert.isTrue(f.getMessages().contains(sentMessage), "El mensaje no ha sido añadido a la carpeta InBox del receptor");
				}
			}
			unauthenticate();
		}

	}
	
}
