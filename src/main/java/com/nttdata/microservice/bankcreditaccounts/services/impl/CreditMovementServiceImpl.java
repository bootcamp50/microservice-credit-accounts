package com.nttdata.microservice.bankcreditaccounts.services.impl;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditMovementCollection;
import com.nttdata.microservice.bankcreditaccounts.enums.CreditMovementTypeEnum;
import com.nttdata.microservice.bankcreditaccounts.repository.ICreditMovementRepository;
import com.nttdata.microservice.bankcreditaccounts.services.ICreditMovementService;
import com.nttdata.microservice.bankcreditaccounts.services.ICreditService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditMovementServiceImpl implements ICreditMovementService {
	
	@Autowired
	private ICreditMovementRepository repository;
	
	@Autowired
	private ICreditService creditService;

	@Override
	public Mono<Void> savePaymentCredit(CreditMovementCollection collection) {
		collection.setMovementType(CreditMovementTypeEnum.PAYMENT_CREDIT.toString());

		// GET AMOUNT AVAILABLE
		return creditService.getAmountAvailable(collection.getCreditNumber())
		.flatMap(amountAvailable->{
			
			// SAVE MOVEMENT
			repository.save(collection).subscribe();
			
			// UPDATE AMOUNT AVAILABLE
			creditService.updateAmountAvalilable(collection.getCreditNumber(), amountAvailable + collection.getAmount()).subscribe();
			
			// UPDATE DAY PAYMENT
			Date paymentDate = Date.from(LocalDate.now().plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			creditService.updatePaymentDate(collection.getCreditNumber(), paymentDate).subscribe();
			
			return Mono.empty();	
		});
		
	}

	@Override
	public Mono<Void> savePaymentCreditCard(CreditMovementCollection collection) {
		collection.setMovementType(CreditMovementTypeEnum.PAYMENT_CREDIT_CARD.toString());
		
		// GET AMOUNT AVAILABLE
		return creditService.getAmountAvailable(collection.getCreditNumber())
		.flatMap(amountAvailable->{
			
			// SAVE MOVEMENT
			repository.save(collection).subscribe();
			
			// UPDATE AMOUNT AVAILABLE
			creditService.updateAmountAvalilable(collection.getCreditNumber(), amountAvailable + collection.getAmount()).subscribe();
			
			// UPDATE DAY PAYMENT
			Date paymentDate = Date.from(LocalDate.now().plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			creditService.updatePaymentDate(collection.getCreditNumber(), paymentDate).subscribe();
			
			return Mono.empty();	
		});
	}

	@Override
	public Mono<Void> saveConsumeCreditCard(CreditMovementCollection collection) {
		collection.setMovementType(CreditMovementTypeEnum.CONSUME_CREDIT_CARD.toString());
		
		// GET AMOUNT AVAILABLE
		return creditService.getAmountAvailable(collection.getCreditNumber())
		.flatMap(amountAvailable->{
			
			if(amountAvailable > collection.getAmount()) {
				
				// SAVE MOVEMENT
				repository.save(collection).subscribe();
				
				// UPDATE AMOUNT AVAILABLE
				creditService.updateAmountAvalilable(collection.getCreditNumber(), amountAvailable - collection.getAmount()).subscribe();
				
				return Mono.empty();	
			}
			
			return Mono.error(RuntimeException::new);
				
		});
	}

	@Override
	public Mono<Void> savePaymentCreditThird(CreditMovementCollection collection) {
		collection.setMovementType(CreditMovementTypeEnum.PAYMENT_CREDIT_THIRD.toString());
		
		// GET AMOUNT AVAILABLE
		return creditService.getAmountAvailable(collection.getCreditNumber())
		.flatMap(amountAvailable->{
			
			// SAVE MOVEMENT
			repository.save(collection).subscribe();
			
			// UPDATE AMOUNT AVAILABLE
			creditService.updateAmountAvalilable(collection.getCreditNumber(), amountAvailable + collection.getAmount()).subscribe();
			
			// UPDATE DAY PAYMENT
			Date paymentDate = Date.from(LocalDate.now().plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			creditService.updatePaymentDate(collection.getCreditNumber(), paymentDate).subscribe();
			
			return Mono.empty();	
		});
	}

	@Override
	public Flux<CreditMovementCollection> getByCreditNumber(String creditNumber) {
		return repository.findByCreditNumber(creditNumber);
	}

}
