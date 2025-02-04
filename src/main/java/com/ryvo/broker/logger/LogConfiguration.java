package com.ryvo.broker.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfiguration {
    @Value("${queue.logging.enabled:false}")
    private boolean loggingEnabled;

    @Bean
    public Logger logger() {
        return loggingEnabled ? LoggerFactory.getLogger("QueueLogger") : LoggerFactory.getLogger("NoQueueLogger");
    }
}
