package com.nttdata.microservice.bankcreditaccounts.utils;

public interface MovementInfoUtils {
	Double roundDouble(Double numberToRound, int decimalPlaces);
    Double applyInterests(Double number, Double interestPercentage);
}
