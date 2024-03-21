package com.nttdata.microservice.bankcreditaccounts.constants;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class CreditConstant {
	
    private String gatewayServiceUrl = "bank-gateway";
    private String customerServiceUrl = "customers";

    private String urlPrefix = "//";

}
