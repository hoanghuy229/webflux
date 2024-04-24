package com.hoanghuy2209.paymentprocessingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.hoanghuy2209.paymentprocessingservice", "com.hoanghuy2209.commonservice"})
public class PaymentprocessingserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentprocessingserviceApplication.class, args);
	}

}
