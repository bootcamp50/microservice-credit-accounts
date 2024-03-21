package com.nttdata.microservice.bankcreditaccounts.dto;

import java.util.Date;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditInfo;
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
public class CreditCreateDto {
	private String customerId;
    private Double creditAmountLimit;
    private Date issueDate;
    private Date paymentDate;
    private CreditInfo creditInfo;

}
