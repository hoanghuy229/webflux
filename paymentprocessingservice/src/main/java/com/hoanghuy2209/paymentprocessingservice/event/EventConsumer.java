package com.hoanghuy2209.paymentprocessingservice.event;
import com.google.gson.Gson;
import com.hoanghuy2209.commonservice.model.PaymentDTO;
import com.hoanghuy2209.commonservice.util.Constant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.security.SecureRandom;
import java.util.Collections;

@Service
@Slf4j
public class EventConsumer {
    Gson gson = new Gson();
    SecureRandom random = new SecureRandom();

    @Autowired
    private EventProducer eventProducer;

    public EventConsumer(ReceiverOptions<String,String> receiverOptions){
        KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PAYMENT_CREATED_TOPIC)))
                .receive().subscribe(this::paymentCreated);
    }

    @SneakyThrows
    public void paymentCreated(ReceiverRecord<String,String> receiverRecord){
        log.info("Processing payment ...");
        PaymentDTO paymentDTO = gson.fromJson(receiverRecord.value(), PaymentDTO.class);
        String[] randomStatus = {Constant.STATUS_PAYMENT_REJECTED,Constant.STATUS_PAYMENT_SUCCESSFUL};
        int index = random.nextInt(randomStatus.length);
        paymentDTO.setStatus(randomStatus[index]);
        Thread.sleep(3000);
        eventProducer.sendEvent(Constant.PAYMENT_COMPLETED_TOPIC,gson.toJson(paymentDTO)).subscribe();
    }
}
