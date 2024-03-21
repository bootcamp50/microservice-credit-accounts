package com.nttdata.microservice.bankcreditaccounts.utils.impl;

import org.springframework.stereotype.Component;

import com.nttdata.microservice.bankcreditaccounts.collections.Customer;
import com.nttdata.microservice.bankcreditaccounts.dto.CustomerDto;
import com.nttdata.microservice.bankcreditaccounts.utils.CustomerUtils;


@Component
public class CustomerUtilsImpl implements CustomerUtils {

	@Override
	public Customer customerDtoToCustomer(CustomerDto customerDto) {
        return Customer.builder()
                .id(customerDto.getId())
                .customerType(customerDto.getPersonType())
                .state(customerDto.getState())
                .build();
    }
}
