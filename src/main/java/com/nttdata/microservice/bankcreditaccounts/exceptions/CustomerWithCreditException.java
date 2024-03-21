package com.nttdata.microservice.bankcreditaccounts.exceptions;

public class CustomerWithCreditException extends RuntimeException {
	private static final long serialVersionUID = -5713584292717311040L;

    public CustomerWithCreditException(String s) {
        super(s);
    }
}
