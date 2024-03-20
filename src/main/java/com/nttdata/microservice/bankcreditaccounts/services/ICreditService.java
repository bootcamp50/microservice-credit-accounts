package com.nttdata.microservice.bankcreditaccounts.services;

import java.util.Date;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditCollection;

import reactor.core.publisher.Mono;

public interface ICreditService {
	
	public Mono<CreditCollection> saveCreditPersonal(CreditCollection credit);
	public Mono<CreditCollection> saveCreditEnterprise(CreditCollection credit);
	public Mono<CreditCollection> saveCreditCardPersonal(CreditCollection credit);
	public Mono<CreditCollection> saveCreditCardEnterprise(CreditCollection credit);
	
	public Mono<Boolean> checkIfHaveCredit(String personCode);
	public Mono<Boolean> checkIfHaveCreditCard(String personCode);
	public Mono<Boolean> checkIfHaveDebt(String personCode);
	
	public Mono<Double> getAmountLimit(String creditNumber);
	public Mono<Double> getAmountAvailable(String creditNumber);
	
	public Mono<CreditCollection> updateAmountAvalilable(String creditNumber, Double newAmountAvailable);
	public Mono<CreditCollection> updatePaymentDate(String creditNumber, Date paymentDate);
}
