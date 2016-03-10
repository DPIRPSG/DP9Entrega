package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Activity;
import domain.Comment;
import domain.Folder;
import domain.Message;
import domain.ServiceEntity;
import domain.Trainer;


import repositories.TrainerRepository;
import security.Authority;
import security.LoginService;
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
	public Trainer save(Trainer trainer){
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
			Collection<ServiceEntity> services;
			Collection<Activity> activities;
			Collection<Comment> comments;
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
			
			services = new ArrayList<ServiceEntity>();
			activities = new ArrayList<Activity>();
			comments = new ArrayList<Comment>();
			trainer.setServices(services);
			trainer.setActivities(activities);
			trainer.setComments(comments);
			trainer.setMadeComments(comments);

			
		}
		//modify = customerRepository.saveAndFlush(customer);
		modify = trainerRepository.save(trainer);		
		
		if(trainer.getId() == 0){
			Collection<Folder> folders;

			folders = folderService.initializeSystemFolder(modify);
			folderService.save(folders);
		}
		return modify;
	}
	
	public Collection<Trainer> findAll(){
		Collection<Trainer> result;
		
		result = trainerRepository.findAll();
		
		return result;
	}
	
	public Trainer findOne(int id){
		Trainer result;
		
		result = trainerRepository.findOne(id);
		
		return result;
	}
	

	//Other business methods -------------------------------------------------

	/**
	 * Devuelve el trainer que está realizando la operación
	 */
	//req: x
	public Trainer findByPrincipal(){
		Trainer result;
		UserAccount userAccount;
		
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = trainerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		
		return result;
	}
	
	public void addService(ServiceEntity serv){
		Trainer actTrainer;
		Collection<ServiceEntity> services;
		Collection<Trainer> trainers;
		
		actTrainer = this.findByPrincipal();
		services = actTrainer.getServices();
		trainers = serv.getTrainers();
		
		trainers.add(actTrainer);
		serv.setTrainers(trainers);
		
		services.add(serv);
		actTrainer.setServices(services);
		
		this.save(actTrainer);
		
	}
	
	public void removeService(ServiceEntity serv){
		Trainer actTrainer;
		Collection<ServiceEntity> services;
		Collection<Trainer> trainers;
		
		actTrainer = this.findByPrincipal();
		services = actTrainer.getServices();
		trainers = serv.getTrainers();
		
		trainers.remove(actTrainer);
		serv.setTrainers(trainers);
		
		services.remove(serv);
		actTrainer.setServices(services);
		
		this.save(actTrainer);
		
	}
	

}
