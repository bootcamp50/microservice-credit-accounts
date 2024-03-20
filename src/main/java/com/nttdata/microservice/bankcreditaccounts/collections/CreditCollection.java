package com.nttdata.microservice.bankcreditaccounts.collections;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "credits")
public class CreditCollection {

	@Id
	private ObjectId id;

	private String personCode;
	private String personType;
	private String creditType;
	private String creditNumber;
	
	//CREDIT
	//private Double creditAmount;
	//private Double creditRemaining;
	//private Integer quantityCreditLimit;
	
	//CREDIT CARD
	private Double creditAmountLimit;
	private Double creditAmountAvailable;
	
	private Date paymentDate;

	private String state;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

}
