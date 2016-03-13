package services.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Administrator;
import domain.Customer;
import domain.Trainer;
import domain.form.ActorForm;
import domain.form.ActorType;

import security.UserAccount;
import security.UserAccountService;
import services.ActorService;
import services.AdministratorService;
import services.CustomerService;
import services.TrainerService;

@Service
@Transactional
public class ActorFormService {
		// Managed repository -----------------------------------------------------

		
		// Supporting services ----------------------------------------------------

		@Autowired
		private ActorService actorService;
		
		@Autowired
		private CustomerService customerService;
		
		@Autowired
		private TrainerService trainerService;
		
		@Autowired
		private AdministratorService administratorService;
		
		@Autowired
		private UserAccountService userAccountService;
		
		
		
		// Constructors -----------------------------------------------------------
		
		public ActorFormService(){
			super();
		}
		
		// Other business methods -------------------------------------------------
		
		public ActorForm createForm(){
			return this.createForm(actorService.discoverActorType());
		}
		
		public ActorForm createForm(ActorType actorType){
			ActorForm result;
			
			if(actorType != null){ 
				result = this.createFormActor(actorService.findByPrincipal());
			}else{ //Usuario registrandose
				result = new ActorForm();
			}
			
			return result;
		}
		
		private ActorForm createFormActor(Actor actor){
			ActorForm result;
			
			result = new ActorForm();
			
			result.setName(actor.getName());
			result.setSurname(actor.getSurname());
			result.setPhone(actor.getPhone());
			result.setUsername(actor.getUserAccount().getUsername());
			if(actorService.checkAuthority("TRAINER")){
				Trainer actTrainer;
				
				actTrainer = trainerService.findByPrincipal();
				result.setPicture(actTrainer.getPicture());
			}
			
			return result;
		}
		
		public int saveForm(ActorForm input){
			return this.saveForm(input, false);
		}
		
		public int saveForm(ActorForm input, boolean newTrainer){
			boolean unregister;
			int result;
			
			if(input.getPassword() != null){
				Assert.isTrue(input.getPassword().equals(input.getRepeatedPassword()), "actorForm.error.passwordMismatch");
			}
			try{
				unregister = ! (actorService.checkAuthority("CUSTOMER")
						|| actorService.checkAuthority("ADMIN")
						|| actorService.checkAuthority("TRAINER"));
			}catch (Exception e) {
				unregister = true;
			}
			unregister = unregister || newTrainer;
			
			if(!unregister){
				result = this.saveActor(input, actorService.discoverActorType());
			}else if(newTrainer){
				result = this.saveTrainerRegistration(input);
			}
			else{ //Usuario registrandose
				result = this.saveCustomerRegistration(input);
			}
			return result;
		}
		
		private int saveActor(ActorForm input, ActorType actorType){
			UserAccount acount;
			String pass;
			int res;
			
			acount = actorService.findByPrincipal().getUserAccount();
			pass = input.getPassword();
			
			acount.setUsername(input.getUsername());
			if(pass != null){
				if(!(pass.isEmpty() || pass.equals(""))){
				acount.setPassword(pass);
				acount = userAccountService.modifyPassword(acount);
				}
			}
			
			if(actorType.equals(ActorType.CUSTOMER)){
				Customer result;
				
				result = customerService.findByPrincipal();
				
				result.setName(input.getName());
				result.setSurname(input.getSurname());
				result.setPhone(input.getPhone());
				result.setUserAccount(acount);
				
				customerService.save(result);
				res = result.getId();
				
			}else if(actorType.equals(ActorType.ADMIN)){
				Administrator result;
				
				result = administratorService.findByPrincipal();
				
				result.setName(input.getName());
				result.setSurname(input.getSurname());
				result.setPhone(input.getPhone());
				result.setUserAccount(acount);
				
				administratorService.save(result);
				res = result.getId();
			}else if(actorType.equals(ActorType.TRAINER)){
				Trainer result;
				
				result = trainerService.findByPrincipal();
				
				result.setName(input.getName());
				result.setSurname(input.getSurname());
				result.setPhone(input.getPhone());
				result.setPicture(input.getPicture());
				
				result.setUserAccount(acount);
				
				trainerService.save(result);
				res = result.getId();
			}else{
				Assert.notNull(null, "Unexpected ActorType");
				res = 0;
			}
			
			return res;
		}
		
		private int saveCustomerRegistration(ActorForm input){
			UserAccount acount;	
			int res;			
			Customer result;
			Assert.isTrue(input.getAcceptTerm(), "actorForm.error.termsDenied");
			
			acount = userAccountService.createComplete(input.getUsername(), input.getPassword(), "CUSTOMER");
			result = customerService.create();
			
			result.setName(input.getName());
			result.setSurname(input.getSurname());
			result.setPhone(input.getUsername());
			result.setUserAccount(acount);
			
			result = customerService.save(result);
			res = result.getId();
			return res;
		}	
		
		private int saveTrainerRegistration(ActorForm input){
			Assert.isTrue(actorService.checkAuthority("ADMIN"), "actorForm.error.notAdmin");
			UserAccount acount;			
			Trainer result;
			int res;
			
			acount = userAccountService.createComplete(input.getUsername(), input.getPassword(), "TRAINER");
			result = trainerService.create();
			
			result.setName(input.getName());
			result.setSurname(input.getSurname());
			result.setPhone(input.getUsername());
			result.setUserAccount(acount);
			
			result = trainerService.save(result);	
			res = result.getId();
			
			return res;
		}

}
