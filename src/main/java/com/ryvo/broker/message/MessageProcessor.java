package com.ryvo.broker.message;

import com.ryvo.broker.bean.ConsumerBean;
import com.ryvo.broker.consumer.QueueConsumer;
import com.ryvo.broker.queue.MessageQueue;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MessageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);

    @Autowired
    private MessageQueue messageQueue;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void registerConsumers() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ConsumerBean.class);

        for (Object bean : beans.values()) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(QueueConsumer.class)) {
                    QueueConsumer annotation = method.getAnnotation(QueueConsumer.class);
                    String queueName = annotation.queueName();

                    try {
                        messageQueue.subscribe(queueName, bean, method);
                        logger.info("Registered consumer {} for queue {}", bean.getClass().getSimpleName(), queueName);
                    } catch (Exception e) {
                        logger.error("Error registering consumer {} for queue {}", bean.getClass().getSimpleName(), queueName, e);
                    }
                }
            }
        }
    }
}