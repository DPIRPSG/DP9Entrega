package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.ExchangeRate;

import repositories.ExchangeRateRepository;

@Service
@Transactional
public class ExchangeRateService {
	//Managed repository -----------------------------------------------------
	
	@Autowired
	private ExchangeRateRepository exchangeRateRepository;
	
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
	
	//Other business methods -------------------------------------------------
	



}
