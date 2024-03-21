package com.nttdata.microservice.bankcreditaccounts.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditCollection;

import reactor.core.publisher.Flux;

@Repository
public interface ICreditRepository extends ReactiveCrudRepository<CreditCollection, ObjectId>{

	//public Flux<CreditCollection> findByPersonCode(String personCode);
	public Flux<CreditCollection> findByCreditNumber(String creditNumber);
	public Flux<CreditCollection> findCreditsByCustomerId(String id);
}
