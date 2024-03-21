package com.nttdata.microservice.bankcreditaccounts.config;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.timelimiter.TimeLimiterConfig;

//@Configuration
public class ReactiveCircuitBreakerConfig {
	
    /*@Bean
    public ReactiveCircuitBreaker customersServiceReactiveCircuitBreaker(ReactiveResilience4JCircuitBreakerFactory reactiveCircuitBreakerFactory) {
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(2))
                .build();

        reactiveCircuitBreakerFactory.configure(builder -> builder
                .timeLimiterConfig(timeLimiterConfig), "customersServiceCircuitBreaker");

        return reactiveCircuitBreakerFactory.create("customersServiceCircuitBreaker");
    }*/

}
