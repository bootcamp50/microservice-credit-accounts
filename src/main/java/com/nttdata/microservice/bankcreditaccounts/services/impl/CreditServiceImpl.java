package com.nttdata.microservice.bankcreditaccounts.services.impl;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditCollection;
import com.nttdata.microservice.bankcreditaccounts.collections.Customer;
import com.nttdata.microservice.bankcreditaccounts.collections.Movement;
import com.nttdata.microservice.bankcreditaccounts.collections.MovementInfo;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditBalanceDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditConsumeDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditCreateDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CustomerDto;
import com.nttdata.microservice.bankcreditaccounts.enums.CreditStateEnum;

import com.nttdata.microservice.bankcreditaccounts.enums.CustomerStateEnum;
import com.nttdata.microservice.bankcreditaccounts.enums.CustomerTypeEnum;
import com.nttdata.microservice.bankcreditaccounts.enums.MovementInfoTypeEnum;
import com.nttdata.microservice.bankcreditaccounts.enums.MovementTypeEnum;
import com.nttdata.microservice.bankcreditaccounts.exceptions.CustomerInactiveException;
import com.nttdata.microservice.bankcreditaccounts.exceptions.CustomerWithCreditException;
import com.nttdata.microservice.bankcreditaccounts.repository.ICreditRepository;
import com.nttdata.microservice.bankcreditaccounts.services.ICreditService;
import com.nttdata.microservice.bankcreditaccounts.utils.CreditUtils;
import com.nttdata.microservice.bankcreditaccounts.utils.CustomerUtils;
import com.nttdata.microservice.bankcreditaccounts.utils.MovementInfoUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CreditServiceImpl implements ICreditService {
	
	@Autowired
	private ICreditRepository repository;
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private CreditUtils creditUtils;
	@Autowired
	private CustomerUtils customerUtils;
	@Autowired
	private MovementInfoUtils movementInfoUtils;
	
	private SecureRandom randomInstance = new SecureRandom();

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
	public Flux<CreditCollection> findAll() {
		return repository.findAll();
	}
	
	@Override
	public Flux<CreditDto> findAllDto() {
		return repository.findAll().map(creditUtils::toCreditDto);
	}

	@Override
	public Mono<CreditCollection> findById(String id) {
		return repository.findById(new ObjectId(id));
	}

	@Override
	public Mono<CreditCollection> save(CreditCreateDto creditCreateDto) {
		log.info("Create a credit");
		
		if (creditCreateDto.getCustomerId() == null || !creditCreateDto.getCustomerId().isBlank()) {
            Mono<CreditCollection> createdCredit = findCustomerById(creditCreateDto.getCustomerId())
                    .flatMap(retrievedCustomer -> {
                        log.info("Validating credit");
                        log.info("creditCreateDto: [{}]", creditCreateDto.toString());
                        log.info("retrievedCustomer: [{}]", retrievedCustomer.toString());
                        return saveValidation(creditCreateDto, retrievedCustomer);
                    })
                    .flatMap(validatedCustomer -> {
                        CreditCollection creditToCreate = creditUtils.creditCreateDtoToCreditCollection(creditCreateDto);
                        Customer customer = customerUtils.customerDtoToCustomer(validatedCustomer);

                        creditToCreate.setCustomer(customer);
                        creditToCreate.setState(CreditStateEnum.ACTIVE.toString());
                        creditToCreate.setCreditAmountAvailable(creditToCreate.getCreditAmountLimit());

                        log.info("Creating new credit: [{}]", creditToCreate.toString());
                        return repository.save(creditToCreate);
                    })
                    .switchIfEmpty(Mono.error(new NoSuchElementException("Customer does not exist")));

            log.info("End of operation to create a credit");
            return createdCredit;
        } else {
            log.warn("Credit does not contain a customer id");
            log.warn("Proceeding to abort create credit");
            return Mono.error(new IllegalArgumentException("Credit does not contain customer id"));
        }
	}
	
	@Override
    public Mono<CustomerDto> findCustomerById(String id) {
        log.info("Start of operation to retrieve customer with id [{}] from customer-service", id);

        log.info("Retrieving customer");
        //String url = constants.getUrlPrefix() + constants.getGatewayServiceUrl() + "/" + constants.getCustomerServiceUrl() + "/person/getCustomer/" + id;
        String url = "http://bank-gateway:8090/person/getCustomer/" + id;
        log.info("url: " + url);
        Mono<CustomerDto> retrievedCustomer = /*customersServiceReactiveCircuitBreaker.run(*/
                webClientBuilder.build().get()
                        .uri(url)
                        .retrieve()
                        .onStatus(httpStatus -> httpStatus == HttpStatus.NOT_FOUND, clientResponse -> Mono.empty())
                        .bodyToMono(CustomerDto.class);
                /*throwable -> {
                    log.warn("Error in circuit breaker call");
                    log.warn(throwable.getMessage());
                    return Mono.error(new CircuitBreakerException("Error in circuit breaker"));
                });*/
        log.info("Customer retrieved successfully");

        log.info("End of operation to retrieve customer with id: [{}]", id);
        return retrievedCustomer;
    }
	
	@Override
    public Flux<CreditCollection> findByCustomerId(String id) {
        log.info("Start of operation to retrieve all credits of the customer with id: [{}]", id);

        log.info("Retrieving credits");
        Flux<CreditCollection> retrievedAccount = repository.findCreditsByCustomerId(id);
        log.info("Credits retrieved successfully");

        log.info("End of operation to retrieve credits of the customer with id: [{}]", id);
        return retrievedAccount;
    }
	
	
	private Mono<CustomerDto> saveValidation(CreditCreateDto creditForCreate, CustomerDto customerFromMicroservice) {
        log.info("Customer exists in database");

        if (customerFromMicroservice.getState().contentEquals(CustomerStateEnum.INACTIVE.toString()))
        {
            log.warn("Customer have inactive status");
            log.warn("Proceeding to abort create credit");
            return Mono.error(new CustomerInactiveException("The customer have inactive status"));
        }

        if (creditForCreate.getCreditInfo() == null) {
            log.warn("Credit does not contain info detail");
            log.warn("Proceeding to abort update account");
            return Mono.error(new IllegalArgumentException("Credit does not info detail"));
        }

        if (customerFromMicroservice.getPersonType().contentEquals(CustomerTypeEnum.PERSONAL.toString())
        		|| customerFromMicroservice.getPersonType().contentEquals(CustomerTypeEnum.PERSONAL_VIP.toString()))
        {
            return findByCustomerId(customerFromMicroservice.getId())
                    .filter(retrievedAccount -> retrievedAccount.getState().contentEquals(CreditStateEnum.ACTIVE.toString()))
                    .hasElements()
                    .flatMap(haveAnAccount -> {
                        if (Boolean.TRUE.equals(haveAnAccount)) {
                            log.warn("Can not create more than one credit for a personal customer");
                            log.warn("Proceeding to abort create credit");
                            return Mono.error(new CustomerWithCreditException("Customer already have one credit"));
                        }
                        else {
                            log.info("Credit successfully validated");
                            return Mono.just(customerFromMicroservice);
                        }
                    });
        } else {
            log.info("Credit successfully validated");
            return Mono.just(customerFromMicroservice);
        }
    }
	
	
	@Override
    public Mono<CreditCollection> consumeCredit(CreditConsumeDto creditDTO) {
        log.info("Start to save a new credit consumption for the credit with id: [{}]", creditDTO.getId());

        Mono<CreditCollection> updatedCredit = repository.findById(new ObjectId(creditDTO.getId()))
                .flatMap(retrievedCredit -> {
                    log.info("Validating consumption operation");
                    return consumptionValidation(creditDTO, retrievedCredit);
                })
                .flatMap(validatedCredit -> {
                    Double amountToUpdate = validatedCredit.getCreditAmountAvailable() - creditDTO.getAmount();
                    validatedCredit.setCreditAmountAvailable(amountToUpdate);

                    CreditCollection creditToUpdate = creditUtils.fillCreditWithCreditConsumeDto(validatedCredit, creditDTO);

                    log.info("Doing consumption of [{}] to credit with id [{}]", creditDTO.getAmount(), creditDTO.getId());
                    log.info("Saving consumption into credit: [{}]", creditDTO.toString());
                    Mono<CreditCollection> nestedUpdatedCredit = repository.save(creditToUpdate);
                    log.info("Consumption was successfully saved");

                    return nestedUpdatedCredit;
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("Credit does not exist")));

        log.info("End to save a new account operation for the credit with id: [{}]", creditDTO.getId());
        return updatedCredit;
    }
	
	
	private Mono<CreditCollection> consumptionValidation(CreditConsumeDto creditToUpdateOperation, CreditCollection creditInDatabase) {
        log.info("Credit exists in database");

        if (creditInDatabase.getState().contentEquals(CreditStateEnum.INACTIVE.toString())) {
            log.warn("Credit have blocked status");
            log.warn("Proceeding to abort consumption operation");
            return Mono.error(new CustomerInactiveException("The credit have blocked status"));
        }

        if (creditInDatabase.getCreditAmountAvailable() < creditToUpdateOperation.getAmount()) {
            log.info("Credit has insufficient funds");
            log.warn("Proceeding to abort do operation");
            return Mono.error(new IllegalArgumentException("The credit has insufficient funds"));
        }

        log.info("Operation successfully validated");
        return Mono.just(creditInDatabase);
    }
	
	@Override
    public Mono<CreditCollection> payCredit(String billingOrderId) {
        log.info("Start of operation to pay a billing order");

        log.info("Looking for billing order");
        Mono<CreditCollection> updatedCredit = findAll()
                .filter(retrievedCredit -> {
                    if (retrievedCredit.getMovements() != null) {
                        return retrievedCredit.getMovements()
                                .stream()
                                .anyMatch(operation -> operation.getMovementInfo() != null &&
                                        operation.getMovementInfo().getStatus().equals(MovementInfoTypeEnum.UNPAID.toString()) &&
                                        operation.getMovementInfo().getId().contentEquals(billingOrderId));
                    }
                    return false;
                })
                .single()
                .flatMap(retrievedCredit -> {
                    log.info("Billing order exists in database");
                    ArrayList<Movement> movements = retrievedCredit.getMovements();
                    ArrayList<Movement> mappedOperations = new ArrayList<>(movements.stream()
                            .map(operation -> {
                                if (operation.getMovementInfo() != null && operation.getMovementInfo().getId().contentEquals(billingOrderId)) {
                                    log.info("Refunding amount to account");
                                    Double availableAmount = retrievedCredit.getCreditAmountAvailable() + operation.getMovementInfo().getAmountToRefund();
                                    if (availableAmount > retrievedCredit.getCreditAmountLimit()) retrievedCredit.setCreditAmountAvailable(retrievedCredit.getCreditAmountLimit());
                                    else retrievedCredit.setCreditAmountAvailable(availableAmount);

                                    log.info("Updating payment operation");
                                    operation.setTime(new Date());
                                    operation.setOperationNumber(UUID.randomUUID().toString());
                                    operation.setAmount(operation.getMovementInfo().getCalculatedAmount());

                                    MovementInfo billingOrder = operation.getMovementInfo();
                                    billingOrder.setStatus(MovementInfoTypeEnum.PAID.toString());
                                    operation.setMovementInfo(billingOrder);
                                    operation.setOperationNumber(UUID.randomUUID().toString());
                                }
                                return operation;
                            }).collect(Collectors.toList()));

                    retrievedCredit.setMovements(mappedOperations);
                    log.info("Saving payment into credit: [{}]", retrievedCredit.toString());
                    Mono<CreditCollection> nestedUpdatedCredit =  repository.save(retrievedCredit);
                    log.info("Payment was successfully saved");

                    return nestedUpdatedCredit;
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("Billing order does not exist")));

        log.info("End of operation to pay a billing order");
        return updatedCredit;
    }
	
	@Override
    public Mono<CreditCollection> generateBillingOrder(String creditId) {
        log.info("Start to generate a billing order for the credit with id: [{}]", creditId);

        Mono<CreditCollection> updatedCredit = repository.findById(new ObjectId(creditId))
                .flatMap(retrievedCredit -> {
                    log.info("Validating credit");
                    return generateBillingOrderValidation(retrievedCredit);
                })
                .flatMap(validatedCredit -> {
                    MovementInfo billingOrder = new MovementInfo();

                    // Generate random amount to refund
                    Double randomAmountToRefund = (validatedCredit.getCreditAmountLimit() - validatedCredit.getCreditAmountAvailable()) * randomInstance.nextDouble();
                    Double roundedAmountToRefund = movementInfoUtils.roundDouble(randomAmountToRefund, 2);
                    billingOrder.setAmountToRefund(roundedAmountToRefund);

                    // Generate amount with interests
                    Double randomConsumeAmountWithInterests = movementInfoUtils.applyInterests(randomAmountToRefund, validatedCredit.getCreditInfo().getInterestPercentage());
                    Double roundedConsumeAmountWithInterests = movementInfoUtils.roundDouble(randomConsumeAmountWithInterests, 2);
                    billingOrder.setCalculatedAmount(roundedConsumeAmountWithInterests);

                    // Set default values for billing order
                    billingOrder.setStatus(MovementInfoTypeEnum.UNPAID.toString());
                    billingOrder.setId(UUID.randomUUID().toString());
                    billingOrder.setCycle(Calendar.MONTH + "/" + Calendar.YEAR);

                    Movement operation = Movement.builder()
                            .type(MovementTypeEnum.PAYMENT.toString())
                            .movementInfo(billingOrder)
                            .build();

                    ArrayList<Movement> operations = validatedCredit.getMovements() == null ? new ArrayList<>() : validatedCredit.getMovements();
                    operations.add(operation);
                    validatedCredit.setMovements(operations);

                    log.info("Generating billing order into credit with id: [{}]", creditId);
                    Mono<CreditCollection> nestedUpdatedCredit = repository.save(validatedCredit);
                    log.info("Generation was successful");

                    return nestedUpdatedCredit;
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("Credit does not exist")));

        log.info("End to generate a new billing order for the credit with id: [{}]", creditId);
        return updatedCredit;
    }
	
	private Mono<CreditCollection> generateBillingOrderValidation(CreditCollection creditInDatabase) {
        log.info("Credit exists in database");

        if (creditInDatabase.getCreditAmountAvailable().equals(creditInDatabase.getCreditAmountLimit())) {
            log.warn("Customer does not have debts for this credit");
            log.warn("Proceeding to abort billing order generation");
            return Mono.error(new CustomerWithCreditException("Customer does not have debts for this credit"));
        }

        log.info("Operation successfully validated");
        return Mono.just(creditInDatabase);
    }
	
	 @Override
	    public Flux<Movement> findOperationsByCreditId(String id) {
	        log.info("Start of operation to retrieve all operations from credit with id: [{}]", id);

	        log.info("Retrieving all operations");
	        Flux<Movement> retrievedOperations = findById(id)
	                .filter(retrievedCredit -> retrievedCredit.getMovements() != null)
	                .flux()
	                .flatMap(retrievedCredit -> Flux.fromIterable(retrievedCredit.getMovements()));
	        log.info("Operations retrieved successfully");

	        log.info("End of operation to retrieve operations from credit with id: [{}]", id);
	        return retrievedOperations;
	    }

	    @Override
	    public Flux<CreditBalanceDto> findBalancesByCustomerId(String id) {
	        log.info("Start of operation to retrieve credit balances from customer with id: [{}]", id);

	        log.info("Retrieving credit balances");
	        Flux<CreditBalanceDto> retrievedBalances = findByCustomerId(id)
	                .map(creditUtils::toCreditBalanceDto);
	        log.info("Credits retrieved successfully");

	        log.info("End of operation to retrieve credit balances from customer with id: [{}]", id);
	        return retrievedBalances;
	    }
	    
	
	

}
