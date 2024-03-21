package com.nttdata.microservice.bankcreditaccounts.collections;

import java.util.Date;

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
public class Movement {
	
	private String operationNumber;
    private Date time;
    private String type;
    private Double amount;
    private MovementInfo movementInfo;

}
