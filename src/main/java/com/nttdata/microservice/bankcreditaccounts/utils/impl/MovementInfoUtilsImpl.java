package com.nttdata.microservice.bankcreditaccounts.utils.impl;

import org.springframework.stereotype.Component;

import com.nttdata.microservice.bankcreditaccounts.utils.MovementInfoUtils;

@Component
public class MovementInfoUtilsImpl implements MovementInfoUtils {
	@Override
    public Double roundDouble(Double numberToRound, int decimalPlaces) {
        numberToRound = numberToRound * Math.pow(10, decimalPlaces);
        numberToRound = (double) (Math.round(numberToRound));
        return numberToRound / Math.pow(10, decimalPlaces);
    }

    @Override
    public Double applyInterests(Double amount, Double interestPercentage) {
        return (100 + interestPercentage)/100 * amount;
    }
}
