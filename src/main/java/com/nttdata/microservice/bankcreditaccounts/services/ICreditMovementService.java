package com.nttdata.microservice.bankcreditaccounts.services;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditMovementCollection;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditMovementService {
	
	public Mono<Void> savePaymentCredit(CreditMovementCollection collection);
	public Mono<Void> savePaymentCreditCard(CreditMovementCollection collection);
	public Mono<Void> saveConsumeCreditCard(CreditMovementCollection collection);
	public Mono<Void> savePaymentCreditThird(CreditMovementCollection collection);
	
	public Flux<CreditMovementCollection> getByCreditNumber(String creditNumber);
	
}
