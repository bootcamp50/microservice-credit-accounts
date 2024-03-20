package com.nttdata.microservice.bankcreditaccounts.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditCollection;
import com.nttdata.microservice.bankcreditaccounts.repository.ICreditRepository;
import com.nttdata.microservice.bankcreditaccounts.services.ICreditService;
import com.nttdata.microservice.bankcreditaccounts.services.impl.CreditServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class CreditServiceImplTest {
	
	@InjectMocks
	CreditServiceImpl service;
	
	@Mock
	ICreditRepository repository;
	
	@Mock
	ICreditService serviceInterface;
	

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Test save a personal credit")
	void saveCreditPersonal() {
		CreditCollection credit = new CreditCollection();
		credit.setPersonCode("123456");
		Mono<CreditCollection> creditMono = Mono.just(credit);
		
		when(serviceInterface.checkIfHaveCredit("123456")).thenReturn(Mono.just(false));
		when(repository.findByPersonCode("123456")).thenReturn(Flux.empty());
		
		when(serviceInterface.checkIfHaveDebt("123456")).thenReturn(Mono.just(false));
		when(repository.findByPersonCode("123456")).thenReturn(Flux.empty());
		
		when(repository.save(credit)).thenReturn(creditMono);
		
		Mono<CreditCollection> expected = service.saveCreditPersonal(credit);
		assertNotNull(expected);
		assertEquals("123456", expected.block().getPersonCode());
		
	}
	
	@Test
	@DisplayName("Test save a enterprise credit")
	void saveCreditEnterprise() {
		
		CreditCollection credit = new CreditCollection();
		credit.setPersonCode("123456");
		Mono<CreditCollection> creditMono = Mono.just(credit);
		
		when(serviceInterface.checkIfHaveCredit("123456")).thenReturn(Mono.just(false));
		when(repository.findByPersonCode("123456")).thenReturn(Flux.empty());
		
		when(serviceInterface.checkIfHaveDebt("123456")).thenReturn(Mono.just(false));
		when(repository.findByPersonCode("123456")).thenReturn(Flux.empty());
		
		when(repository.save(credit)).thenReturn(creditMono);
		
		Mono<CreditCollection> expected = service.saveCreditEnterprise(credit);
		assertNotNull(expected);
		assertEquals("123456", expected.block().getPersonCode());
	}
	
	@Test
	@DisplayName("Test save a personal credit card")
	void saveCreditCardPersonal() {
		
		CreditCollection credit = new CreditCollection();
		credit.setPersonCode("123456");
		Mono<CreditCollection> creditMono = Mono.just(credit);
		
		when(serviceInterface.checkIfHaveCredit("123456")).thenReturn(Mono.just(false));
		when(repository.findByPersonCode("123456")).thenReturn(Flux.empty());
		
		when(serviceInterface.checkIfHaveDebt("123456")).thenReturn(Mono.just(false));
		when(repository.findByPersonCode("123456")).thenReturn(Flux.empty());
		
		when(repository.save(credit)).thenReturn(creditMono);
		
		Mono<CreditCollection> expected = service.saveCreditCardPersonal(credit);
		assertNotNull(expected);
		assertEquals("123456", expected.block().getPersonCode());
		
	}
	
	@Test
	@DisplayName("Test save a enterprise credit card")
	void saveCreditCardEnterprise() {
		
		CreditCollection credit = new CreditCollection();
		credit.setPersonCode("123456");
		Mono<CreditCollection> creditMono = Mono.just(credit);
		
		when(serviceInterface.checkIfHaveCredit("123456")).thenReturn(Mono.just(false));
		when(repository.findByPersonCode("123456")).thenReturn(Flux.empty());
		
		when(serviceInterface.checkIfHaveDebt("123456")).thenReturn(Mono.just(false));
		when(repository.findByPersonCode("123456")).thenReturn(Flux.empty());
		
		when(repository.save(credit)).thenReturn(creditMono);
		
		Mono<CreditCollection> expected = service.saveCreditCardEnterprise(credit);
		assertNotNull(expected);
		assertEquals("123456", expected.block().getPersonCode());
		
		
	}
	
	@Test
	@DisplayName("Test check if a customer have a credit")
	void checkIfHaveCredit() {
		
		CreditCollection credit = new CreditCollection();
		credit.setPersonCode("123456");
		
		when(repository.findByPersonCode("123456")).thenReturn(Flux.empty());
		
		Mono<Boolean> expected = service.checkIfHaveCredit("123456");
		
		assertNotNull(expected);
		assertEquals(false, expected.block());
		
	}
	
	@Test
	@DisplayName("Test check if a customer have a credit card")
	void checkIfHaveCreditCard() {
		
		CreditCollection credit = new CreditCollection();
		credit.setPersonCode("123456");
		
		when(repository.findByPersonCode("123456")).thenReturn(Flux.empty());
		
		Mono<Boolean> expected = service.checkIfHaveCreditCard("123456");
		
		assertNotNull(expected);
		assertEquals(false, expected.block());
		
	}
	
	@Test
	@DisplayName("Test check if a customer have a debt")
	void checkIfHaveDebt() {
		CreditCollection credit = new CreditCollection();
		credit.setPersonCode("123456");
		
		when(repository.findByPersonCode("123456")).thenReturn(Flux.empty());
		
		Mono<Boolean> expected = service.checkIfHaveDebt("123456");
		
		assertNotNull(expected);
		assertEquals(false, expected.block());
	}
	
	@Test
	@DisplayName("Test get amount limit of a credit")
	void getAmountLimit() {
		
		CreditCollection credit = new CreditCollection();
		credit.setCreditNumber("123456");
		credit.setCreditAmountLimit(20.00);
		
		when(repository.findByCreditNumber("123456")).thenReturn(Flux.just(credit));
		
		Mono<Double> expected = service.getAmountLimit("123456");
		assertNotNull(expected);
		assertEquals(20.00, expected.block());
		
	}
	
	@Test
	@DisplayName("Test get amount available of a credit")
	void getAmountAvailable() {
		
		CreditCollection credit = new CreditCollection();
		credit.setCreditNumber("123456");
		credit.setCreditAmountAvailable(20.00);
		
		when(repository.findByCreditNumber("123456")).thenReturn(Flux.just(credit));
		
		Mono<Double> expected = service.getAmountAvailable("123456");
		assertNotNull(expected);
		assertEquals(20.00, expected.block());
		
	}
	
	@Test
	@DisplayName("Test update amount available of a credit")
	void updateAmountAvalilable() {
		
		String creditNumber="123456";
		Double amountAvailable = 20.0;
		
		CreditCollection credit = new CreditCollection();
		credit.setCreditNumber("123456");
		Mono<CreditCollection> creditMono = Mono.just(credit);
		when(repository.findByCreditNumber("123456")).thenReturn(Flux.just(credit));
		when(repository.save(credit)).thenReturn(creditMono);
		
		Mono<CreditCollection> expected = service.updateAmountAvalilable(creditNumber, amountAvailable);
		assertNotNull(expected);
		assertEquals(20.00, expected.block().getCreditAmountAvailable());
		
	}
	
	@Test
	@DisplayName("Test update payment date of a credit")
	void updatePaymentDate() {
		
		String creditNumber="123456";
		Date paymentDate = new Date();
		
		CreditCollection credit = new CreditCollection();
		credit.setCreditNumber("123456");
		Mono<CreditCollection> creditMono = Mono.just(credit);
		when(repository.findByCreditNumber("123456")).thenReturn(Flux.just(credit));
		when(repository.save(credit)).thenReturn(creditMono);
		
		
		Mono<CreditCollection> expected = service.updatePaymentDate(creditNumber, paymentDate);
		assertNotNull(expected);
		assertEquals(paymentDate, expected.block().getPaymentDate());
	}
}
