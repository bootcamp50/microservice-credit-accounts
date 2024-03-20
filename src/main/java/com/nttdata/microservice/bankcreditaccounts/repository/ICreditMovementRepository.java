package com.nttdata.microservice.bankcreditaccounts.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditMovementCollection;

import reactor.core.publisher.Flux;

@Repository
public interface ICreditMovementRepository extends ReactiveCrudRepository<CreditMovementCollection, ObjectId>{

	public Flux<CreditMovementCollection> findByCreditNumber(String creditNumber);
}
