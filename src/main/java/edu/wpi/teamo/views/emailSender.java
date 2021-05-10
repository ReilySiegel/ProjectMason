package edu.wpi.teamo.views;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class emailSender {
    public static void sendCovidMail(String to) throws MessagingException {
        Properties emailprop = new Properties();
        emailprop.put("mail.smtp.auth", "true");
        emailprop.put("mail.smtp.starttls.enable", "true");
        emailprop.put("mail.smtp.host", "smtp.gmail.com");
        emailprop.put("mail.smtp.port", "587");

        String myEmailAcc = "dyllancolesuperfan@gmail.com";
        String password = "oxbloodorc1";

        javax.mail.Session mailSession = javax.mail.Session.getInstance(emailprop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(myEmailAcc,password);
            }
        });

        Message message = prepareMessage(mailSession, myEmailAcc);

        Transport.send(message);

    }

    public static Message prepareMessage(javax.mail.Session mailSession, String myEmailAcc) {
        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(myEmailAcc));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("masonpowell45@gmail.com"));
            message.setSubject("Covid Survey");
            message.setText("You have submitted a covid survey, please wait to be approved!");
            return message;
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}