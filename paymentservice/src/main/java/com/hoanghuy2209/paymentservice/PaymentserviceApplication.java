package com.hoanghuy2209.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories
@ComponentScan({"com.hoanghuy2209.paymentservice", "com.hoanghuy2209.commonservice"})
public class PaymentserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentserviceApplication.class, args);
	}

}
