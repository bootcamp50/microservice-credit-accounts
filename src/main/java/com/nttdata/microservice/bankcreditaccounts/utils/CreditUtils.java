package com.nttdata.microservice.bankcreditaccounts.utils;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditCollection;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditBalanceDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditConsumeDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditCreateDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditDto;

public interface CreditUtils {

	public CreditCollection creditCreateDtoToCreditCollection(CreditCreateDto creditDTO);
	public CreditDto toCreditDto(CreditCollection credit);
	CreditCollection fillCreditWithCreditConsumeDto(CreditCollection credit, CreditConsumeDto creditDTO);
	CreditBalanceDto toCreditBalanceDto(CreditCollection credit);
}
