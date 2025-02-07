package com.client.onboarding.service;

import com.client.onboarding.model.PasswordResetToken;
import com.client.onboarding.repository.PasswordResetTokenRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final String secretKey = "123!@#Qwcwev44#@$23dqaacs";
    private final TextEncryptor encryptor;
    private UserService userService;

    public PasswordResetService(PasswordResetTokenRepository tokenRepository, JavaMailSender mailSender,UserService userService) {
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
        String salt = KeyGenerators.string().generateKey();
        this.encryptor = Encryptors.text(secretKey, salt);
        this.userService = userService;
    }

    @Transactional
    public void generateAndSendToken(String email) {
        tokenRepository.deleteByEmail(email);

        String token = email + ":" + System.currentTimeMillis();
        String encryptedToken = encryptor.encrypt(token);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(encryptedToken);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(5));

        tokenRepository.save(resetToken);

        sendEmail(email, encryptedToken);
    }

    @Transactional
    public boolean verifyToken(String token,String email,String password) {
        Optional<PasswordResetToken> storedToken = tokenRepository.findByToken(token);
        if(storedToken.isEmpty()){
            throw new RuntimeException("Invalid or expired token.");
        }
        if(storedToken.isPresent() && storedToken.get().getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token is expired.");
        }
        if (storedToken.isPresent() && storedToken.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            if(!storedToken.get().getEmail().equals(email)){
                throw new RuntimeException("Invalid request");
            }
            tokenRepository.delete(storedToken.get());
            userService.createUser(email,password);
            sendOnboardingEmail(email);
            return true;
        }
        return false;
    }

    private void sendEmail(String email, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Reset Your Password");
    
            String resetLink = "http://localhost:5173/set-password/?token=" + token + "&email=" + email;
    
            String emailContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<meta name='viewport' content='width=device-width, initial-scale=1'>" +
                    "<title>Reset Your Password</title>" +
                    "<style>" +
                    "  body { font-family: 'Arial', sans-serif; margin: 0; padding: 0; background: #f9f9f9; }" +
                    "  .container { max-width: 500px; background: #ffffff; padding: 25px; margin: 50px auto; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); text-align: center; }" +
                    "  .header { font-size: 22px; font-weight: bold; color: #333; margin-bottom: 10px; }" +
                    "  .content { font-size: 15px; color: #555; margin-bottom: 20px; line-height: 1.5; }" +
                    "  .button { background: #2563EB; color: #ffffff !important; padding: 12px 20px; text-decoration: none; border-radius: 6px; font-size: 14px; font-weight: bold; display: inline-block; transition: 0.3s; }" +
                    "  .button:hover { background: #1e4bb8; transform: scale(1.05); }" +
                    "  .footer { font-size: 13px; color: #777; margin-top: 20px; padding-top: 10px; border-top: 1px solid #ddd; }" +
                    "  .link { color: #007BFF; text-decoration: none; font-weight: 600; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "  <div class='header'>Reset Your Password</div>" +
                    "  <div class='content'>Hello, <br> Click the button below to reset your password.</div>" +
                    "  <a href='" + resetLink + "' class='button'>Reset Password</a>" +
                    "  <div class='content' style='margin-top: 15px;'>If the button does not work, copy this link:</div>" +
                    "  <div class='content'><a href='" + resetLink + "' class='link'>" + resetLink + "</a></div>" +
                    "  <div class='footer'>If you didn’t request this, ignore this email. Need help? <a href='#' class='link'>Contact Support</a></div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
    
            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }
    
    private void sendOnboardingEmail(String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Welcome to Our ERP System");
    
            String loginLink = "http://localhost:5173/login";
    
            String emailContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<meta name='viewport' content='width=device-width, initial-scale=1'>" +
                    "<title>Welcome to ERP</title>" +
                    "<style>" +
                    "  body { font-family: 'Arial', sans-serif; background: #f9f9f9; padding: 20px; }" +
                    "  .container { max-width: 500px; background: #ffffff; padding: 25px; margin: 40px auto; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); text-align: center; }" +
                    "  .header { font-size: 22px; font-weight: bold; color: #333; margin-bottom: 10px; }" +
                    "  .content { font-size: 15px; color: #555; margin-bottom: 20px; line-height: 1.5; }" +
                    "  .button { background: #2563EB; color: #ffffff !important; padding: 12px 20px; text-decoration: none; border-radius: 6px; font-size: 14px; font-weight: bold; display: inline-block; transition: 0.3s; }" +
                    "  .button:hover { background: #1e4bb8; transform: scale(1.05); }" +
                    "  .footer { font-size: 13px; color: #777; margin-top: 20px; padding-top: 10px; border-top: 1px solid #ddd; }" +
                    "  .link { color: #007BFF; text-decoration: none; font-weight: 600; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "  <div class='header'>Welcome to Our ERP System</div>" +
                    "  <div class='content'>Hi <b>" + email + "</b>,<br>We are excited to have you! Click below to log in.</div>" +
                    "  <a href='" + loginLink + "' class='button'>Login to ERP</a>" +
                    "  <div class='content' style='margin-top: 15px;'>If the button does not work, copy this link:</div>" +
                    "  <div class='content'><a href='" + loginLink + "' class='link'>" + loginLink + "</a></div>" +
                    "  <div class='footer'>Thank you for joining us! Need help? <a href='#' class='link'>Contact Support</a></div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
    
            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }
    

}
