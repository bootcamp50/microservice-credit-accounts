package com.nttdata.microservice.bankcreditaccounts.exceptions;

public class CustomerInactiveException extends RuntimeException {
	private static final long serialVersionUID = -5713584292717311039L;

    public CustomerInactiveException(String s) {
        super(s);
    }
}
