package com.client.onboarding.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    // public void sendAlert(String city, Weather weather) {
    //     SimpleMailMessage message = new SimpleMailMessage();
    //     message.setTo("shubhammehta.212@gmail.com");
    //     message.setSubject("Extreme Weather Alert for " + city + "!");
    //     message.setText("City: " + city + "\nTemperature: " + weather.getTemperature() +
    //                     "°C\nWeather Condition: " );

    //     emailSender.send(message);
    // }
}