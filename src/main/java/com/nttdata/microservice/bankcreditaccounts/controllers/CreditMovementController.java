package com.nttdata.microservice.bankcreditaccounts.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditMovementCollection;
import com.nttdata.microservice.bankcreditaccounts.services.ICreditMovementService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@RestController
//@RequestMapping(value = "/creditMovements")
public class CreditMovementController {
	/*
	public static Logger log = Logger.getLogger(CreditMovementController.class);
	
	@Autowired
	private ICreditMovementService service;
	
	@GetMapping(value = "/getMovementsByCreditNumber/{creditNumber}")
	public Flux<CreditMovementCollection> getMovementsByCreditNumber(@PathVariable("creditNumber") String creditNumber) {
		return service.getByCreditNumber(creditNumber);
	}
	
	@PostMapping(value = "/savePaymentCredit")
	public Mono<Void> savePaymentCredit(@RequestBody CreditMovementCollection collection){
		return service.savePaymentCredit(collection);
	}
	
	@PostMapping(value = "/savePaymentCreditCard")
	public Mono<Void> savePaymentCreditCard(@RequestBody CreditMovementCollection collection){
		return service.savePaymentCreditCard(collection);
	}
	
	@PostMapping(value = "/saveConsumeCreditCard")
	public Mono<Void> saveConsumeCreditCard(@RequestBody CreditMovementCollection collection){
		return service.saveConsumeCreditCard(collection);
	}
	
	@PostMapping(value = "/savePaymentCreditThird")
	public Mono<Void> savePaymentCreditThird(@RequestBody CreditMovementCollection collection){
		return service.savePaymentCreditThird(collection);
	}
	*/
}
