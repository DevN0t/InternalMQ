package com.ryvo.broker;

import com.ryvo.broker.consumer.QueueConsumer;
import com.ryvo.broker.queue.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    MessageQueue messageQueue;

    @PostMapping("/send")
    public void sendMessage(@RequestBody String message) {
        messageQueue.publish("oi", message);
    }
}
