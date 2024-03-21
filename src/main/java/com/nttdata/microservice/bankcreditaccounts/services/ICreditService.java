package com.nttdata.microservice.bankcreditaccounts.services;

import java.util.Date;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditCollection;
import com.nttdata.microservice.bankcreditaccounts.collections.Movement;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditBalanceDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditConsumeDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditCreateDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CustomerDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditService {
	
	public Flux<CreditCollection> findAll();
	public Flux<CreditDto> findAllDto();
	
	public Mono<CreditCollection> findById(String id);
	public Mono<CustomerDto> findCustomerById(String id);
	public Flux<CreditCollection> findByCustomerId(String id);
	
	public Mono<CreditCollection> save(CreditCreateDto creditDto);
	Mono<CreditCollection> consumeCredit(CreditConsumeDto creditDTO);
	Mono<CreditCollection> payCredit(String billingOrderId);
	Mono<CreditCollection> generateBillingOrder(String creditId);
	
	Flux<Movement> findOperationsByCreditId(String id);
    Flux<CreditBalanceDto> findBalancesByCustomerId(String id);
    
	
	
	public Mono<Double> getAmountLimit(String creditNumber);
	public Mono<Double> getAmountAvailable(String creditNumber);
	
	public Mono<CreditCollection> updateAmountAvalilable(String creditNumber, Double newAmountAvailable);
	public Mono<CreditCollection> updatePaymentDate(String creditNumber, Date paymentDate);
}
