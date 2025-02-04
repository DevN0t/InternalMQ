package com.ryvo.broker;

import com.ryvo.broker.bean.ConsumerBean;
import com.ryvo.broker.consumer.QueueConsumer;
import com.ryvo.broker.queue.MessageQueue;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@ConsumerBean
public class Component {

    @QueueConsumer(queueName = "oi")
    public void processMessage(String message) throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("Processing message: " + message);
    }
}
