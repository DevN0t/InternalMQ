package com.ryvo.broker.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class MessageQueue {


    private static final Logger logger = LoggerFactory.getLogger(MessageQueue.class);
    private final ConcurrentHashMap<String, BlockingQueue<String>> queueMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ExecutorService> executorMap = new ConcurrentHashMap<>();

    public void publish(String queueName, String message) {
        BlockingQueue<String> queue = getQueue(queueName);
        try {
            queue.put(message);
            logger.info("Message added to queue '{}': {}", queueName, message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Error adding message to queue '{}'", queueName, e);
        }
    }

    private BlockingQueue<String> getQueue(String queueName) {
        return queueMap.computeIfAbsent(queueName, key -> new LinkedBlockingQueue<>());
    }

    public void subscribe(String queueName, Object bean, java.lang.reflect.Method method) {
        BlockingQueue<String> queue = getQueue(queueName);
        ExecutorService executor = executorMap.computeIfAbsent(queueName, k -> Executors.newFixedThreadPool(5));

        executor.submit(() -> {
            while (true) {
                try {
                    String message = queue.take();
                    logger.info("Consuming message from queue '{}': {}", queueName, message);
                    method.invoke(bean, message);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("Consumer interrupted for queue '{}'", queueName, e);
                    break;
                } catch (Exception e) {
                    logger.error("Error processing message from queue '{}'", queueName, e);
                }
            }
        });
    }
}

