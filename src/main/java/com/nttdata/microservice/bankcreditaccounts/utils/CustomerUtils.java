package com.nttdata.microservice.bankcreditaccounts.utils;

import com.nttdata.microservice.bankcreditaccounts.collections.Customer;
import com.nttdata.microservice.bankcreditaccounts.dto.CustomerDto;

public interface CustomerUtils {
	
	public Customer customerDtoToCustomer(CustomerDto customerDto);

}
