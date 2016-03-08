package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Comment;
import domain.Folder;
import domain.Message;
import domain.Trainer;


import repositories.TrainerRepository;
import security.Authority;
import security.UserAccount;
import security.UserAccountService;

@Service
@Transactional
public class TrainerService {
	//Managed repository -----------------------------------------------------
	
	@Autowired
	private TrainerRepository trainerRepository;
	
	//Supporting services ----------------------------------------------------

	@Autowired
	private FolderService folderService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	/*@Autowired
	private ShoppingCartService shoppingCartService;*/
	
	//Constructors -----------------------------------------------------------

	public TrainerService(){
		super();
	}
	
	//Simple CRUD methods ----------------------------------------------------
	
	/** Devuelve trainer preparado para ser modificado. Necesita usar save para que persista en la base de datos
	 * 
	 */
	// req: 10.1
	public Trainer create(){
		Trainer result;
		UserAccount userAccount;

		result = new Trainer();
		
		userAccount = userAccountService.create("TRAINER");
		result.setUserAccount(userAccount);
		
		return result;
	}
	
	/**
	 * Almacena en la base de datos el cambio
	 */
	// req: 10.1
	public void save(Trainer trainer){
		Assert.notNull(trainer);
		
		Trainer modify;
		
		boolean result = true;
		for(Authority a: trainer.getUserAccount().getAuthorities()){
			if(!a.getAuthority().equals("TRAINER")){
				result = false;
				break;
			}
		}
		Assert.isTrue(result, "A trainer can only be a authority.trainer");
		
		if(trainer.getId() == 0){
			Assert.isTrue(actorService.checkAuthority("ADMIN"), "trainer.create.permissionDenied");
			
			Collection<Folder> folders;
			Collection<Message> sent;
			Collection<Message> received;
			Collection<Comment> comments;
			// Collection<FeePayment>feePayments;
			UserAccount auth;
			
			//Encoding password
			auth = trainer.getUserAccount();
			auth = userAccountService.modifyPassword(auth);
			trainer.setUserAccount(auth);
			
			// Initialize folders
			folders = folderService.initializeSystemFolder(trainer);
			trainer.setMessageBoxes(folders);
			
			sent = new ArrayList<Message>();
			received = new ArrayList<Message>();
			trainer.setSent(sent);
			trainer.setReceived(received);
			
			// Initialize anothers
			
			/*comments = new ArrayList<Comment>();
			feePayments = new ArrayList<FeePayment>();
			trainer.setCommentss(comments);
			trainer.setFeePayments(feePayments);*/

			
		}
		//modify = customerRepository.saveAndFlush(customer);
		modify = trainerRepository.save(trainer);		
		
		if(trainer.getId() == 0){
			Collection<Folder> folders;

			folders = folderService.initializeSystemFolder(modify);
			folderService.save(folders);
		}
		
	}
	
	/**
	 * Lista los customers registrados
	 */
//	// req: 12.5
//	public Collection<Customer> findAll(){
//		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can list customers");
//		
//		Collection<Customer> result;
//		
//		result = trainerRepository.findAll();
//		
//		return result;
//	}

	//Other business methods -------------------------------------------------

	/**
	 * Devuelve el customers que est� realizando la operaci�n
	 */
//	//req: x
//	public Customer findByPrincipal(){
//		Customer result;
//		UserAccount userAccount;
//		
//		userAccount = LoginService.getPrincipal();
//		Assert.notNull(userAccount);
//		result = trainerRepository.findByUserAccountId(userAccount.getId());
//		Assert.notNull(result);
//		
//		return result;
//	}
	

}
