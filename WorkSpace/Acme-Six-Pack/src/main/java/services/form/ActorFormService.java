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
			return this.createForm(false);
		}
		
		public ActorForm createForm(boolean newTrainer){
			ActorForm result;
			
			if((actorService.checkAuthority("CUSTOMER")
					|| actorService.checkAuthority("ADMIN")
					|| actorService.checkAuthority("TRAINER"))
					&& ! newTrainer){ 
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
			
			return result;
		}
		
		public void saveForm(ActorForm input){
			this.saveForm(input, false);
		}
		
		public void saveForm(ActorForm input, boolean newTrainer){
			boolean unregister;
			
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
				this.saveActor(input, actorService.checkAuthority("CUSTOMER"));
			}else if(newTrainer){
				this.saveTrainerRegistration(input);
			}
			else{ //Usuario registrandose
				this.saveCustomerRegistration(input);
			}
		}
		
		private void saveActor(ActorForm input, boolean isConsumer){
			UserAccount acount;
			String pass;
			
			acount = actorService.findByPrincipal().getUserAccount();
			pass = input.getPassword();
			
			acount.setUsername(input.getUsername());
			if(pass != null){
				if(!(pass.isEmpty() || pass.equals(""))){
				acount.setPassword(pass);
				acount = userAccountService.modifyPassword(acount);
				}
			}
			
			if(isConsumer){
				Customer result;
				
				result = customerService.findByPrincipal();
				
				result.setName(input.getName());
				result.setSurname(input.getSurname());
				result.setPhone(input.getPhone());
				result.setUserAccount(acount);
				
				customerService.save(result);
				
			}else{ //isAdmin
				Administrator result;
				
				result = administratorService.findByPrincipal();
				
				result.setName(input.getName());
				result.setSurname(input.getSurname());
				result.setPhone(input.getPhone());
				result.setUserAccount(acount);
				
				administratorService.save(result);
			}
			
			
		}
		
		private void saveCustomerRegistration(ActorForm input){
			UserAccount acount;			
			Customer result;
			Assert.isTrue(input.getAcceptTerm(), "actorForm.error.termsDenied");
			
			acount = userAccountService.createComplete(input.getUsername(), input.getPassword(), "CUSTOMER");
			result = customerService.create();
			
			result.setName(input.getName());
			result.setSurname(input.getSurname());
			result.setPhone(input.getUsername());
			result.setUserAccount(acount);
			
			customerService.save(result);
		}	
		
		private void saveTrainerRegistration(ActorForm input){
			Assert.isTrue(actorService.checkAuthority("ADMIN"), "actorForm.error.notAdmin");
			UserAccount acount;			
			Trainer result;
			
			acount = userAccountService.createComplete(input.getUsername(), input.getPassword(), "TRAINER");
			result = trainerService.create();
			
			result.setName(input.getName());
			result.setSurname(input.getSurname());
			result.setPhone(input.getUsername());
			result.setUserAccount(acount);
			// result.setPicture(input.getPicture());
			
			trainerService.save(result);	
			
		}

}
