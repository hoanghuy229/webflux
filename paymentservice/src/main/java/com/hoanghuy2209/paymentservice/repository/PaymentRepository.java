package com.hoanghuy2209.paymentservice.repository;

import com.hoanghuy2209.paymentservice.data.Payment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PaymentRepository extends ReactiveCrudRepository<Payment,Long> {
    Flux<Payment> findByAccountId(long id);
}
