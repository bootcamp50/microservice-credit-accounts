package com.nttdata.microservice.bankcreditaccounts.services.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nttdata.microservice.bankcreditaccounts.collections.CreditCollection;
import com.nttdata.microservice.bankcreditaccounts.enums.CreditTypeEnum;
import com.nttdata.microservice.bankcreditaccounts.enums.PersonTypeEnum;
import com.nttdata.microservice.bankcreditaccounts.repository.ICreditRepository;
import com.nttdata.microservice.bankcreditaccounts.services.ICreditService;

import reactor.core.publisher.Mono;

@Service
public class CreditServiceImpl implements ICreditService {
	
	@Autowired
	private ICreditRepository repository;

	@Override
	public Mono<CreditCollection> saveCreditPersonal(CreditCollection credit) {
		
		credit.setCreditType(CreditTypeEnum.CREDIT.toString());
		credit.setPersonType(PersonTypeEnum.PERSONAL.toString());
		credit.setCreditNumber(UUID.randomUUID().toString());
		
		// CHECK IF HAVE CREDIT
		return checkIfHaveCredit(credit.getPersonCode()).flatMap(checkHaveCredit->{
			if(!checkHaveCredit) {
				
				// CHECK IF HAVE DEBT
				return checkIfHaveDebt(credit.getPersonCode())
						.flatMap(checkHaveDebt->{
							if(!checkHaveDebt) {
								//SAVE CREDIT
								return repository.save(credit);	
							}
								return Mono.error(RuntimeException::new);
				});
			}
			return Mono.error(RuntimeException::new);
		});
	}

	@Override
	public Mono<CreditCollection> saveCreditEnterprise(CreditCollection credit) {
		credit.setCreditType(CreditTypeEnum.CREDIT.toString());
		credit.setPersonType(PersonTypeEnum.ENTERPRISE.toString());
		credit.setCreditNumber(UUID.randomUUID().toString());
		
		// CHECK IF HAVE DEBT
		return checkIfHaveDebt(credit.getPersonCode())
				.flatMap(checkHaveDebt->{
					if(!checkHaveDebt) {
						//SAVE CREDIT
						return repository.save(credit);	
					}
						return Mono.error(RuntimeException::new);
		});
	}

	@Override
	public Mono<CreditCollection> saveCreditCardPersonal(CreditCollection credit) {
		credit.setCreditType(CreditTypeEnum.CREDIT_CARD.toString());
		credit.setPersonType(PersonTypeEnum.PERSONAL.toString());
		credit.setCreditNumber(UUID.randomUUID().toString());
		
		// CHECK IF HAVE DEBT
		return checkIfHaveDebt(credit.getPersonCode())
				.flatMap(checkHaveDebt->{
					if(!checkHaveDebt) {
						//SAVE CREDIT
						return repository.save(credit);	
					}
						return Mono.error(RuntimeException::new);
		});
	}

	@Override
	public Mono<CreditCollection> saveCreditCardEnterprise(CreditCollection credit) {
		credit.setCreditType(CreditTypeEnum.CREDIT_CARD.toString());
		credit.setPersonType(PersonTypeEnum.ENTERPRISE.toString());
		credit.setCreditNumber(UUID.randomUUID().toString());

		// CHECK IF HAVE DEBT
		return checkIfHaveDebt(credit.getPersonCode())
				.flatMap(checkHaveDebt->{
					if(!checkHaveDebt) {
						//SAVE CREDIT
						return repository.save(credit);	
					}
						return Mono.error(RuntimeException::new);
		});
	}

	@Override
	public Mono<Boolean> checkIfHaveCreditCard(String personCode) {
		
		return repository.findByPersonCode(personCode)
				.filter(x-> x.getCreditType().equals(CreditTypeEnum.CREDIT_CARD.toString()))
				.count()
				.flatMap(x -> {
					if(x > 0) {
						return Mono.just(true);	
					}
					return Mono.just(false);
				});
	}
	
	@Override
	public Mono<Boolean> checkIfHaveCredit(String personCode) {
		
		return repository.findByPersonCode(personCode)
				.filter(x-> x.getCreditType().equals(CreditTypeEnum.CREDIT.toString()))
				.count()
				.flatMap(x -> {
					if(x > 0) {
						return Mono.just(true);	
					}
					return Mono.just(false);
				});
	}

	@Override
	public Mono<CreditCollection> updateAmountAvalilable(String creditNumber, Double newAmountAvailable) {
		return repository.findByCreditNumber(creditNumber).next().flatMap(collection -> {
			collection.setCreditAmountAvailable(newAmountAvailable);
			return repository.save(collection);
		});
	}

	@Override
	public Mono<Double> getAmountLimit(String creditNumber) {
		return repository.findByCreditNumber(creditNumber).next().flatMap( x -> Mono.just(x.getCreditAmountLimit()));
	}

	@Override
	public Mono<Double> getAmountAvailable(String creditNumber) {
		return repository.findByCreditNumber(creditNumber).next().flatMap( x -> Mono.just(x.getCreditAmountAvailable()));
	}

	@Override
	public Mono<CreditCollection> updatePaymentDate(String creditNumber, Date paymentDate) {
		return repository.findByCreditNumber(creditNumber).next().flatMap(collection -> {
			collection.setPaymentDate(paymentDate);
			return repository.save(collection);
		});
	}

	@Override
	public Mono<Boolean> checkIfHaveDebt(String personCode) {
		return repository.findByPersonCode(personCode).filter(x -> new Date().compareTo(x.getPaymentDate()) > 0)
				.count().flatMap(w -> {
			if (w > 1) {
				return Mono.just(true);
			}
			return Mono.just(false);
		} );
	}

}
