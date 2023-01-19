package org.komponente.notificationservice.listener.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MessageHelper {


    private Validator validator;
    private ObjectMapper objectMapper;


    public MessageHelper(Validator validator, ObjectMapper objectMapper) {
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    /*
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean().getValidator();
    }

     */


    public <T> T getMessage(Message message, Class<T> clazz) throws RuntimeException, JMSException {
        /*
        try{
            String json = ((TextMessage) message).getText();
            T data = objectMapper.readValue(json, clazz);

            Set<ConstraintViolation<T>> violations = validator.validate(data);
            if (violations.isEmpty()) {
                return data;
            }

            printViolationsAndThrowException(violations);
            return null;
        } catch (IOException exception) {
            throw new RuntimeException("Message parsing fails.", exception);
        }

         */
        try {
            if(message instanceof ObjectMessage){
                ObjectMessage objectMessage = (ObjectMessage) message;
                T data = (T) objectMessage.getObject();
                Set<ConstraintViolation<T>> violations = validator.validate(data);
                if (violations.isEmpty()) {
                    return data;
                }
                printViolationsAndThrowException(violations);
                return null;
            } else if(message instanceof TextMessage){
                String json = ((TextMessage) message).getText();
                T data = objectMapper.readValue(json, clazz);
                Set<ConstraintViolation<T>> violations = validator.validate(data);
                if (violations.isEmpty()) {
                    return data;
                }
                printViolationsAndThrowException(violations);
                return null;
            } else {
                throw new RuntimeException("Message parsing fails.");
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String createTextMessage(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Problem with creating text message");
        }
    }

    private <T> void printViolationsAndThrowException(Set<ConstraintViolation<T>> violations) {
        String concatenatedViolations = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        throw new RuntimeException(concatenatedViolations);
    }
}

