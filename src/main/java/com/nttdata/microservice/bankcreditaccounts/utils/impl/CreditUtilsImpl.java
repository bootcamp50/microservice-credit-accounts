package com.nttdata.microservice.bankcreditaccounts.utils.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditCollection;
import com.nttdata.microservice.bankcreditaccounts.collections.Customer;
import com.nttdata.microservice.bankcreditaccounts.collections.Movement;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditBalanceDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditConsumeDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditCreateDto;
import com.nttdata.microservice.bankcreditaccounts.dto.CreditDto;
import com.nttdata.microservice.bankcreditaccounts.enums.MovementTypeEnum;
import com.nttdata.microservice.bankcreditaccounts.utils.CreditUtils;

@Component
public class CreditUtilsImpl implements CreditUtils{

	@Override
    public CreditCollection creditCreateDtoToCreditCollection(CreditCreateDto creditCreateDto) {
        return CreditCollection.builder()
                .customer(Customer.builder().id(creditCreateDto.getCustomerId()).build())
                .creditAmountLimit(creditCreateDto.getCreditAmountLimit())
                .creditNumber(UUID.randomUUID().toString())
                .issueDate(creditCreateDto.getIssueDate())
                .paymentDate(creditCreateDto.getPaymentDate())
                .creditInfo(creditCreateDto.getCreditInfo())
                .build();
    }
	
	@Override
    public CreditCollection fillCreditWithCreditConsumeDto(CreditCollection credit, CreditConsumeDto creditDto) {
        Movement operation = Movement.builder()
                .amount(creditDto.getAmount())
                .time(new Date())
                .type(MovementTypeEnum.CONSUME.toString())
                .operationNumber(UUID.randomUUID().toString())
                .build();

        ArrayList<Movement> movements = credit.getMovements() == null ? new ArrayList<>() : credit.getMovements();
        movements.add(operation);

        credit.setMovements(movements);
        credit.setId(new ObjectId(creditDto.getId()));

        return credit;
    }

	@Override
	public CreditDto toCreditDto(CreditCollection credit) {
		return CreditDto.builder()
				.id(String.valueOf(credit.getId()))
                .customerId(credit.getCustomer().getId())
                .creditAmountLimit(credit.getCreditAmountLimit())
                .creditAmountAvailable(credit.getCreditAmountAvailable())
                .creditNumber(credit.getCreditNumber())
                .creditType(credit.getCreditType())
                .issueDate(credit.getIssueDate())
                .paymentDate(credit.getPaymentDate())
                .creditInfo(credit.getCreditInfo())
                .movements(credit.getMovements())
                .build();
	}
	
	@Override
    public CreditBalanceDto toCreditBalanceDto(CreditCollection credit) {
        return CreditBalanceDto.builder()
                .id(String.valueOf(credit.getId()))
                .limitAmount(credit.getCreditAmountLimit())
                .availableAmount(credit.getCreditAmountAvailable())
                .build();
    }
	
}
