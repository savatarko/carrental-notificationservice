package org.komponente.notificationservice.listener;

import org.komponente.dto.email.*;
import org.komponente.notificationservice.listener.helper.MessageHelper;
import org.komponente.notificationservice.service.EmailService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class EmailListener {

    private MessageHelper messageHelper;
    private EmailService emailService;

    public EmailListener(MessageHelper messageHelper, EmailService emailService) {
        this.messageHelper = messageHelper;
        this.emailService = emailService;
    }

    @JmsListener(destination = "newregistration", concurrency = "5-10")
    public void register(Message message) throws JMSException {
        RegisterNotification registerNotification = messageHelper.getMessage(message, RegisterNotification.class);
        emailService.sendSimpleMessage(registerNotification.getEmail(), "Welcome to RAF car!", registerNotification.toString());
    }

    @JmsListener(destination = "changepassword", concurrency = "5-10")
    public void changePassword(Message message) throws JMSException {
        ChangePasswordNotification registerNotification = messageHelper.getMessage(message, ChangePasswordNotification.class);
        emailService.sendSimpleMessage(registerNotification.getEmail(), "Confirm password change", registerNotification.toString());
    }

    @JmsListener(destination = "reminder", concurrency = "5-10")
    public void reservationReminder(Message message) throws JMSException{
        ReservationReminderNotification reservationReminderNotification = messageHelper.getMessage(message, ReservationReminderNotification.class);
        emailService.sendSimpleMessage(reservationReminderNotification.getEmail(), "Reservation reminder", reservationReminderNotification.toString());
    }

    @JmsListener(destination = "reservation", concurrency = "5-10")
    public void successfulReservationClient(Message message) throws JMSException{
        SuccessfulReservationClientNotification successfulReservationClientNotification = messageHelper.getMessage(message, SuccessfulReservationClientNotification.class);
        emailService.sendSimpleMessage(successfulReservationClientNotification.getEmail(), "Successful registration", successfulReservationClientNotification.toString());
    }

    @JmsListener(destination = "reservationmanager", concurrency = "5-10")
    public void successfulReservationManager(Message message) throws JMSException{
        SuccessfulReservationManagerNotification successfulReservationManagerNotification = messageHelper.getMessage(message, SuccessfulReservationManagerNotification.class);
        emailService.sendSimpleMessage(successfulReservationManagerNotification.getEmail(), "Successful registration", successfulReservationManagerNotification.toString());
    }

    @JmsListener(destination = "cancelreservationclient", concurrency = "5-10")
    public void cancelReservationClient(Message message) throws JMSException{
        CancelReservationClientNotification cancelReservationClientNotification = messageHelper.getMessage(message, CancelReservationClientNotification.class);
        emailService.sendSimpleMessage(cancelReservationClientNotification.getEmail(), "Reservation cancelled", cancelReservationClientNotification.toString());

    }

    @JmsListener(destination = "cancelreservationmanager", concurrency = "5-10")
    public void cancelReservationManager(Message message) throws JMSException{
        CancelReservationManagerNotification cancelReservationManagerNotification = messageHelper.getMessage(message, CancelReservationManagerNotification.class);
        emailService.sendSimpleMessage(cancelReservationManagerNotification.getEmail(), "Reservation cancelled", cancelReservationManagerNotification.toString());
    }
}
