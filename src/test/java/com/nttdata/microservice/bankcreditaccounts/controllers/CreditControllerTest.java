package com.nttdata.microservice.bankcreditaccounts.controllers;

import static org.junit.jupiter.api.Assertions.*;

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
import com.nttdata.microservice.bankcreditaccounts.services.ICreditService;
import com.nttdata.microservice.bankcreditaccounts.services.impl.CreditServiceImpl;


import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest(CreditController.class)
class CreditControllerTest {
	
	
	@Autowired
	private WebTestClient webTestClient;
	
	
	@MockBean
	private ICreditService service;

	@BeforeEach
	void setUp() throws Exception {
	}

	
	@Test
	@DisplayName("Test if customer have a credit card")
	void checkIfHaveCreditCard() {
		
	}
	
	@Test
	@DisplayName("Test if customer have a debt")
	void checkIfHaveDebt() {
		
	}
	
	@Test
	@DisplayName("Test save personal credit")
	void saveCreditPersonal() {
	
	}
	
	@Test
	@DisplayName("Test save enterprise credit")
	void saveCreditEnterprise() {
	
	}
	
	@Test
	@DisplayName("Test save personal credit card")
	void saveCreditCardPersonal() {
	
	}
	
	@Test
	@DisplayName("Test save enterprise credit card")
	void saveCreditCardEnterprise() {
		
	}

}
