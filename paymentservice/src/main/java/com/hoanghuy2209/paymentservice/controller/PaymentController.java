package com.hoanghuy2209.paymentservice.controller;

import com.hoanghuy2209.paymentservice.model.PaymentDTO;
import com.hoanghuy2209.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @GetMapping(value = "/{id}")
    public ResponseEntity<Flux<PaymentDTO>> getAllPayment(@PathVariable long id){
        return ResponseEntity.ok(paymentService.getAllPayment(id));
    }
    @PostMapping(value = "/payment")
    public ResponseEntity<Mono<PaymentDTO>> makePayment(@RequestBody PaymentDTO paymentDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.makePayment(paymentDTO));
    }
}
