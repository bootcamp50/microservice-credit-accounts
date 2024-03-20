package com.nttdata.microservice.bankcreditaccounts.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditCollection;
import com.nttdata.microservice.bankcreditaccounts.services.ICreditService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/credit")
public class CreditController {
	
	@Autowired
	private ICreditService service;
	
	@GetMapping(value = "/checkIfHaveCreditCard/{personCode}")
	public Mono<Boolean> checkIfHaveCreditCard(@PathVariable("personCode") String personCode) {
		return service.checkIfHaveCreditCard(personCode);
	}
	
	@GetMapping(value = "/checkIfHaveDebt/{personCode}")
	public Mono<Boolean> checkIfHaveDebt(@PathVariable("personCode") String personCode) {
		return service.checkIfHaveDebt(personCode);
	}
	
	
	@PostMapping(value = "/saveCreditPersonal")
	public Mono<CreditCollection> saveCreditPersonal(@RequestBody CreditCollection collection) {
		return service.saveCreditPersonal(collection);
	}
	@PostMapping(value = "/saveCreditEnterprise")
	public Mono<CreditCollection> saveCreditEnterprise(@RequestBody CreditCollection collection) {
		return service.saveCreditEnterprise(collection);
	}
	@PostMapping(value = "/saveCreditCardPersonal")
	public Mono<CreditCollection> saveCreditCardPersonal(@RequestBody CreditCollection collection) {
		return service.saveCreditCardPersonal(collection);
	}
	@PostMapping(value = "/saveCreditCardEnterprise")
	public Mono<CreditCollection> saveCreditCardEnterprise(@RequestBody CreditCollection collection) {
		return service.saveCreditCardEnterprise(collection);
	}
	
}
