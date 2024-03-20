package com.nttdata.microservice.bankcreditaccounts.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditCollection;
import com.nttdata.microservice.bankcreditaccounts.collections.CreditMovementCollection;
import com.nttdata.microservice.bankcreditaccounts.services.ICreditMovementService;
import com.nttdata.microservice.bankcreditaccounts.services.impl.CreditMovementServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest(CreditMovementController.class)
class CreditMovementControllerTest {
	
	@Autowired
	WebTestClient webTestClient;
	
	@MockBean
	ICreditMovementService service;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	@DisplayName("Test get movements by credit number")
	void getMovementsByCreditNumber() {
		
		CreditMovementCollection creditMovement = new CreditMovementCollection();
		creditMovement.setCreditNumber("123456");
		creditMovement.setPersonCode("123789");
		creditMovement.setAmount(10.0);
		
		CreditMovementCollection creditMovement2 = new CreditMovementCollection();
		creditMovement2.setCreditNumber("123456");
		creditMovement2.setPersonCode("123789");
		creditMovement2.setAmount(10.0);
		
		Flux<CreditMovementCollection> creditMovementFlux = Flux.just(creditMovement, creditMovement2);
		
		when(service.getByCreditNumber("123456")).thenReturn(creditMovementFlux);
		
		Flux<CreditMovementCollection> response = webTestClient.get().uri("/creditMovements/getMovementsByCreditNumber/123456")
				.exchange()
				.expectStatus().isOk()
				.returnResult(CreditMovementCollection.class)
				.getResponseBody();
				
				StepVerifier.create(response)
				.expectSubscription()
				.expectNext(creditMovement)
				.expectNext(creditMovement2)
				.verifyComplete();
				
		
	}
	
	@Test
	@DisplayName("Test save payment of a credit")
	void savePaymentCredit(){
		
		CreditMovementCollection creditMovement = new CreditMovementCollection();
		creditMovement.setCreditNumber("123456");
		creditMovement.setPersonCode("123789");
		creditMovement.setAmount(10.0);
		
		Mono<CreditMovementCollection> creditMovementMono = Mono.just(creditMovement);
		
		when(service.savePaymentCredit(creditMovement)).thenReturn(Mono.empty());
		
		webTestClient.post().uri("/creditMovements/savePaymentCredit")
		.body(Mono.just(creditMovementMono),CreditMovementCollection.class)
		.exchange()
		.expectStatus().isOk();
	}
	
	@Test
	@DisplayName("Test save payment of a credit card")
	void savePaymentCreditCard(){

		CreditMovementCollection creditMovement = new CreditMovementCollection();
		creditMovement.setCreditNumber("123456");
		creditMovement.setPersonCode("123789");
		creditMovement.setAmount(10.0);
		
		Mono<CreditMovementCollection> creditMovementMono = Mono.just(creditMovement);
		
		when(service.savePaymentCreditCard(creditMovement)).thenReturn(Mono.empty());
		
		webTestClient.post().uri("/creditMovements/savePaymentCreditCard")
		.body(Mono.just(creditMovementMono),CreditMovementCollection.class)
		.exchange()
		.expectStatus().isOk();
		
	}

	@Test
	@DisplayName("Test save consume of a credit card")
	void saveConsumeCreditCard(){
		
		CreditMovementCollection creditMovement = new CreditMovementCollection();
		creditMovement.setCreditNumber("123456");
		creditMovement.setPersonCode("123789");
		creditMovement.setAmount(10.0);
		
		Mono<CreditMovementCollection> creditMovementMono = Mono.just(creditMovement);
		
		when(service.saveConsumeCreditCard(creditMovement)).thenReturn(Mono.empty());
		
		webTestClient.post().uri("/creditMovements/saveConsumeCreditCard")
		.body(Mono.just(creditMovementMono),CreditMovementCollection.class)
		.exchange()
		.expectStatus().isOk();
		
	}
	
	@Test
	@DisplayName("Test save payment of a thrid's credit")
	void savePaymentCreditThird(){

		CreditMovementCollection creditMovement = new CreditMovementCollection();
		creditMovement.setCreditNumber("123456");
		creditMovement.setPersonCode("123789");
		creditMovement.setAmount(10.0);
		
		Mono<CreditMovementCollection> creditMovementMono = Mono.just(creditMovement);
		
		when(service.savePaymentCreditThird(creditMovement)).thenReturn(Mono.empty());
		
		webTestClient.post().uri("/creditMovements/savePaymentCreditThird")
		.body(Mono.just(creditMovementMono),CreditMovementCollection.class)
		.exchange()
		.expectStatus().isOk();
		
	}

}
