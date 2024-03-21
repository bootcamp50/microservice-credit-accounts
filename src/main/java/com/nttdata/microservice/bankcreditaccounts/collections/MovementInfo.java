package com.nttdata.microservice.bankcreditaccounts.collections;

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
public class MovementInfo {
	private String id;
    private Double calculatedAmount;
    private Double amountToRefund;
    private String cycle;
    private String status;

}
