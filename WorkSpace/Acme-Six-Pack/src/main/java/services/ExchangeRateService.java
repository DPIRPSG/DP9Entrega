package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.ExchangeRate;

import repositories.ExchangeRateRepository;

@Service
@Transactional
public class ExchangeRateService {
	//Managed repository -----------------------------------------------------
	
	@Autowired
	private ExchangeRateRepository exchangeRateRepository;
	
	@Autowired
	private ActorService actorService;
	
	//Supporting services ----------------------------------------------------
	
	//Constructors -----------------------------------------------------------

	public ExchangeRateService(){
		super();
	}
	
	//Simple CRUD methods ----------------------------------------------------
	
	public Collection<ExchangeRate> findAll(){
		Collection<ExchangeRate> result;
		
		result = exchangeRateRepository.findAll();
		
		return result;
	}
	
	public ExchangeRate findOne(int id){
		ExchangeRate result;
		
		result = exchangeRateRepository.findOne(id);
		
		return result;
	}
	
	public ExchangeRate findByCurrency(String text){
		ExchangeRate result;
		
		result = exchangeRateRepository.findByCurrency(text);
		
		return result;
	}
	
	public ExchangeRate create(){
		ExchangeRate result;
		
		result = new ExchangeRate();
		
		return result;
	}

	public ExchangeRate save(ExchangeRate exchaRate){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "exchangeRate.modify.permissionDenied");
		
		ExchangeRate result;
		
		result = exchangeRateRepository.save(exchaRate);
		
		return result;
	}
	
	public void delete(ExchangeRate exchangeRate){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "exchangeRate.delete.permissionDenied");
		
		exchangeRateRepository.delete(exchangeRate);
	}
	//Other business methods -------------------------------------------------
	



}
