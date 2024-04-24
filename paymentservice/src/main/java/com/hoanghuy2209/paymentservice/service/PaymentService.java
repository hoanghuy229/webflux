package com.hoanghuy2209.paymentservice.service;

import com.google.gson.Gson;
import com.hoanghuy2209.commonservice.common.CommonException;
import com.hoanghuy2209.commonservice.model.AccountDTO;
import com.hoanghuy2209.commonservice.util.Constant;
import com.hoanghuy2209.paymentservice.event.EventProducer;
import com.hoanghuy2209.paymentservice.model.PaymentDTO;
import com.hoanghuy2209.paymentservice.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WebClient webClientAccount;

    @Autowired
    private EventProducer eventProducer;

    Gson gson = new Gson();
    public Flux<PaymentDTO> getAllPayment(long id){
        return paymentRepository.findByAccountId(id)
                .map(PaymentDTO::entityToDto)
                .switchIfEmpty(Mono.error(new CommonException("P02", "Account don't have payment", HttpStatus.NOT_FOUND)));
    }

    public Mono<PaymentDTO> makePayment(PaymentDTO paymentDTO){
        return webClientAccount.get()
                .uri("/checkBalance/"+paymentDTO.getAccountId())
                .retrieve()
                .bodyToMono(AccountDTO.class)
                .flatMap(accountDTO -> {
                    if(paymentDTO.getAmount() <= accountDTO.getBalance()){
                        paymentDTO.setStatus(Constant.STATUS_PAYMENT_CREATING);
                    }else{
                        throw new CommonException("P01", "Balance not enough", HttpStatus.BAD_REQUEST);
                    }
                    return createNewPayment(paymentDTO);
                });
    }

    public Mono<PaymentDTO> createNewPayment(PaymentDTO paymentDTO){
        return Mono.just(paymentDTO)
                .map(PaymentDTO::dtoToEntity)
                .flatMap(payment -> paymentRepository.save(payment))
                .map(PaymentDTO::entityToDto)
                .doOnError(throwable -> log.error(throwable.getMessage()))
                .doOnSuccess(paymentDTO1 -> eventProducer.sendPaymentRequest(Constant.PAYMENT_REQUEST_TOPIC,gson.toJson(paymentDTO1)).subscribe());
    }

    public Mono<PaymentDTO> updateStatusPayment(PaymentDTO paymentDTO){
        return paymentRepository.findById(paymentDTO.getId())
                .switchIfEmpty(Mono.error(new CommonException("P03", "Payment not found", HttpStatus.NOT_FOUND)))
                .flatMap(payment -> {
                    payment.setStatus(paymentDTO.getStatus());
                    return paymentRepository.save(payment);
                })
                .map(PaymentDTO::entityToDto);
    }
}
