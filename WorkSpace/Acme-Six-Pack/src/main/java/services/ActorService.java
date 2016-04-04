package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.form.ActorType;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;


@Service
@Transactional
public class ActorService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ActorRepository actorRepository;
	
	// Supporting services ----------------------------------------------------
	
	// Constructors -----------------------------------------------------------
	
	public ActorService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------
	
	public Actor findOne(int actorId){
		Actor result;
		
		result = actorRepository.findOne(actorId);
		
		return result;
	}

	public Collection<Actor> findAll(){
		Collection<Actor> result;
		
		result = actorRepository.findAll();
		
		return result;
	}
	
	// Other business methods -------------------------------------------------

	/**
	 *  Devuelve el actor que está realizando la operación
	 */
	//req: 24.1, 24.2
	public Actor findByPrincipal(){
		Actor result;
		UserAccount userAccount;
		
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = actorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		
		return result;
	}

	/**
	 * Comprueba si el usuario que está ejecutando tiene la AuthoritySolicitada
	 * @return boolean -> false si no es customer
	 * @param authority [ADMIN, CUSTOMER, TRAINER]
	 */
	public boolean checkAuthority(String authority){
		boolean result;
		Actor actor;
		Collection<Authority> authorities;
		result = false;

		try {
			actor = this.findByPrincipal();
			authorities = actor.getUserAccount().getAuthorities();
			
			for (Authority a : authorities) {
				if(a.getAuthority().equals(authority.toUpperCase())){
					result = true;
					break;
				}
			}
		} catch (IllegalArgumentException e) {
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Dice que tipo de actor es el usuario actual
	 * @return ActorType
	 */
	public ActorType discoverActorType() {
		ActorType result;
		try {
			if (this.checkAuthority("CUSTOMER")) {
				result = ActorType.CUSTOMER;
			} else if (this.checkAuthority("ADMIN")) {
				result = ActorType.ADMIN;
			} else if (this.checkAuthority("TRAINER")) {
				result = ActorType.TRAINER;
			} else {
				result = null;
			}
		} catch (Exception e) {
			result = null;
		}

		return result;
	}
	
	/* Query 7 */
	public Collection<Actor> findActorWhoSendMoreSpam(){
		Assert.isTrue(checkAuthority("ADMIN"), "Only an admin can open the dashboard");
		
		Collection<Actor> result;
		
		result = actorRepository.findActorWhoSendMoreSpam();
		
		return result;
	}
	
	/* Query 8 */
	public Double findAverageNumberOfMessagesInActorMessageBox(){
		Assert.isTrue(checkAuthority("ADMIN"), "Only an admin can open the dashboard");
		
		Double result;
		
		result = actorRepository.findAverageNumberOfMessagesInActorMessageBox();
		
		return result;
	}
	
	/* Query 11 */
	public Double findAverageNumberOfCommentWrittenByAnActor(){
		Assert.isTrue(checkAuthority("ADMIN"), "Only an admin can open the dashboard");
		
		Double result;
		
		result = actorRepository.findAverageNumberOfCommentWrittenByAnActor();
		
		return result;
	}
	
	/* Query 11 */
	public Double findStandardDeviationNumberOfCommentWrittenByAnActor(){
		Assert.isTrue(checkAuthority("ADMIN"), "Only an admin can open the dashboard");
		
		Double result;
		
		result = actorRepository.findStandardDeviationNumberOfCommentWrittenByAnActor();
		
		return result;
	}
	
	public void flush(){
		actorRepository.flush();
	}
}
