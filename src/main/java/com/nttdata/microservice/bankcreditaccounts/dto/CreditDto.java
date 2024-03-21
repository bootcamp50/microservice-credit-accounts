package com.nttdata.microservice.bankcreditaccounts.dto;

import java.util.ArrayList;
import java.util.Date;

import com.nttdata.microservice.bankcreditaccounts.collections.CreditInfo;
import com.nttdata.microservice.bankcreditaccounts.collections.Movement;

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
public class CreditDto {
	private String id;
	private String customerId;
	private String creditType;
	private String creditNumber;
    private Double creditAmountLimit;
    private Double creditAmountAvailable;
    private Date issueDate;
    private Date paymentDate;
    private CreditInfo creditInfo;
    private ArrayList<Movement> movements;
}
