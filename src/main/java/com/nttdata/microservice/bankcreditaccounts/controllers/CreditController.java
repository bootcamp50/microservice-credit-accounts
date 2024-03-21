package com.nttdata.microservice.bankcreditaccounts.controllers;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditCollection;
import com.nttdata.microservice.bankcreditaccounts.collections.Movement;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditBalanceDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditConsumeDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditCreateDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditDto;
import com.nttdata.microservice.bankcreditaccounts.exceptions.CircuitBreakerException;
import com.nttdata.microservice.bankcreditaccounts.exceptions.CustomerInactiveException;
import com.nttdata.microservice.bankcreditaccounts.exceptions.CustomerWithCreditException;
import com.nttdata.microservice.bankcreditaccounts.services.ICreditService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping(value = "/credit")
public class CreditController {
	
	@Autowired
	private ICreditService service;
	
	@GetMapping("/credits")
    public Flux<CreditDto> findAllCredits(){
            log.info("Get all credits");
        return service.findAllDto();
    }
	
	@GetMapping("/credits/{id}")
    public Mono<ResponseEntity<CreditCollection>> findCreditById(@PathVariable("id") String id) {
        log.info("Get credits{}", id);
        return service.findById(id)
                .flatMap(retrievedCredit -> Mono.just(ResponseEntity.ok(retrievedCredit)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
	
	
	@PostMapping("/saveCredit")
    public Mono<ResponseEntity<CreditCollection>> createCredit(@RequestBody CreditCreateDto creditCreateDto) {
        log.info("Post operation in /credits");
        return service.save(creditCreateDto)
                .flatMap(createdCredit -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(createdCredit)))
                .onErrorResume(CustomerInactiveException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.LOCKED).build()))
                .onErrorResume(CustomerWithCreditException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
                .onErrorResume(IllegalArgumentException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()))
                .onErrorResume(NoSuchElementException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .onErrorResume(CircuitBreakerException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build()))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null)));
    }
	
	@GetMapping("findCreditsByCustomerId/{customerId}")
    public Flux<CreditCollection> findCreditsByCustomerId(@PathVariable("customerId") String customerId) {
        log.info("Get credits by customer {}", customerId);
        return service.findByCustomerId(customerId);
    }
	
	@PostMapping("/consumeCredit")
    public Mono<ResponseEntity<CreditCollection>> consumeCredit(@RequestBody CreditConsumeDto operationDTO) {
        log.info("Post operation in /consumeCredit");
        return service.consumeCredit(operationDTO)
                .flatMap(createdCredit -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(createdCredit)))
                .onErrorResume(CustomerInactiveException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.LOCKED).build()))
                .onErrorResume(IllegalArgumentException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()))
                .onErrorResume(NoSuchElementException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null)));
    }
	
	@PostMapping("/generateBillingOrder/{id}")
    public Mono<ResponseEntity<CreditCollection>> generateBillingOrder(@PathVariable("id") String creditId) {
        log.info("Post operation in /credits/generateBillingOrder");
        return service.generateBillingOrder(creditId)
                .flatMap(createdCredit -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(createdCredit)))
                .onErrorResume(CustomerInactiveException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
                .onErrorResume(NoSuchElementException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null)));
    }
	
	
	@PostMapping("/payCredit/{id}")
    public Mono<ResponseEntity<CreditCollection>> payCredit(@PathVariable("id") String id) {
        log.info("Post operation in /credits/operation/payments");
        return service.payCredit(id)
                .flatMap(createdCredit -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(createdCredit)))
                .onErrorResume(NoSuchElementException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null)));
    }
    
	
	@GetMapping("/findMovementsByCreditId/{id}")
    public Flux<Movement> findMovementsByCreditId(@PathVariable("id") String id) {
        log.info("Get findMovementsByCreditId/{id}", id);
        return service.findOperationsByCreditId(id);
    }

    @GetMapping("findBalancesByCustomerId/{id}")
    public Flux<CreditBalanceDto> findBalancesByCustomerId(@PathVariable("id") String id) {
        log.info("Get findBalancesByCustomerId in /findBalancesByCustomerId/{}", id);
        return service.findBalancesByCustomerId(id);
    }
    
	
	
	
}
