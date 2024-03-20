package com.nttdata.microservice.bankcreditaccounts.service.impl;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditCollection;
import com.nttdata.microservice.bankcreditaccounts.collections.CreditMovementCollection;
import com.nttdata.microservice.bankcreditaccounts.repository.ICreditMovementRepository;
import com.nttdata.microservice.bankcreditaccounts.services.ICreditService;
import com.nttdata.microservice.bankcreditaccounts.services.impl.CreditMovementServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class CreditMovementServiceImplTest {
	
	@InjectMocks
	CreditMovementServiceImpl service;
	
	@Mock
	ICreditService creditService;
	
	@Mock
	ICreditMovementRepository repository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Test save payment of a credit")
	void savePaymentCredit() {
		
		Date paymentDay = new Date();
		
		CreditMovementCollection creditMovement = new CreditMovementCollection();
		creditMovement.setCreditNumber("123456");
		creditMovement.setAmount(10.0);
		
		CreditCollection credit = new CreditCollection();
		credit.setCreditNumber("123789");
		
		when(creditService.getAmountAvailable("123456")).thenReturn(Mono.just(10.0));
		when(repository.save(creditMovement)).thenReturn(Mono.just(creditMovement));
		when(creditService.updateAmountAvalilable("123456", 10.0)).thenReturn(Mono.just(credit));
		when(creditService.updatePaymentDate("123456", paymentDay)).thenReturn(Mono.just(credit));
		
		Mono<Void> expected = service.savePaymentCredit(creditMovement);
		assertNotNull(expected);
		
	}
	
	@Test
	@DisplayName("Test save payment of a credit card")
	void savePaymentCreditCard() {
		
		Date paymentDay = new Date();
		
		CreditMovementCollection creditMovement = new CreditMovementCollection();
		creditMovement.setCreditNumber("123456");
		creditMovement.setAmount(10.0);
		
		CreditCollection credit = new CreditCollection();
		credit.setCreditNumber("123789");
		
		when(creditService.getAmountAvailable("123456")).thenReturn(Mono.just(10.0));
		when(repository.save(creditMovement)).thenReturn(Mono.just(creditMovement));
		when(creditService.updateAmountAvalilable("123456", 10.0)).thenReturn(Mono.just(credit));
		when(creditService.updatePaymentDate("123456", paymentDay)).thenReturn(Mono.just(credit));
		
		Mono<Void> expected = service.savePaymentCreditCard(creditMovement);
		assertNotNull(expected);
		
	}
	
	@Test
	@DisplayName("Test save consume of a credit card")
	void saveConsumeCreditCard() {
		
		CreditMovementCollection creditMovement = new CreditMovementCollection();
		creditMovement.setCreditNumber("123456");
		creditMovement.setAmount(10.0);
		
		CreditCollection credit = new CreditCollection();
		credit.setCreditNumber("123789");
		
		when(creditService.getAmountAvailable("123456")).thenReturn(Mono.just(10.0));
		when(repository.save(creditMovement)).thenReturn(Mono.just(creditMovement));
		when(creditService.updateAmountAvalilable("123456", 10.0)).thenReturn(Mono.just(credit));
		
		Mono<Void> expected = service.saveConsumeCreditCard(creditMovement);
		assertNotNull(expected);
	}
	
	@Test
	@DisplayName("Test save payment of a third's credit")
	void savePaymentCreditThird() {
		
		Date paymentDay = new Date();
		
		CreditMovementCollection creditMovement = new CreditMovementCollection();
		creditMovement.setCreditNumber("123456");
		creditMovement.setAmount(10.0);
		
		CreditCollection credit = new CreditCollection();
		credit.setCreditNumber("123789");
		
		when(creditService.getAmountAvailable("123456")).thenReturn(Mono.just(10.0));
		when(repository.save(creditMovement)).thenReturn(Mono.just(creditMovement));
		when(creditService.updateAmountAvalilable("123456", 10.0)).thenReturn(Mono.just(credit));
		when(creditService.updatePaymentDate("123456", paymentDay)).thenReturn(Mono.just(credit));
		
		Mono<Void> expected = service.savePaymentCreditThird(creditMovement);
		assertNotNull(expected);
	}
	
	@Test
	@DisplayName("Test get movements of a credit number")
	void getByCreditNumber() {
		
		CreditMovementCollection creditMovement = new CreditMovementCollection();
		creditMovement.setCreditNumber("123456");
		creditMovement.setAmount(10.0);
		
		CreditMovementCollection creditMovement2 = new CreditMovementCollection();
		creditMovement2.setCreditNumber("123456");
		creditMovement2.setAmount(10.0);
		
		Flux<CreditMovementCollection> movements = Flux.just(creditMovement,creditMovement2);
		when(repository.findByCreditNumber("123456")).thenReturn(movements);
		
		Flux<CreditMovementCollection> expected = service.getByCreditNumber("123456");
		
		assertNotNull(expected);
		assertEquals("123456",expected.blockFirst().getCreditNumber());
		
	}

}
