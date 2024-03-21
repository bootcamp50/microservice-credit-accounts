package com.nttdata.microservice.bankcreditaccounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreditBalanceDto {
	private String id;
    private Double limitAmount;
    private Double availableAmount;
}
