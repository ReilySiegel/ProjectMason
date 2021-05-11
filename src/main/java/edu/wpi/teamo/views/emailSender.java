package edu.wpi.teamo.views;

import edu.wpi.teamo.App;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class emailSender {
    public static void sendCovidReceiptMail(String to, String type) throws MessagingException {
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

        Message message = prepareCovidReceiptMessage(mailSession, myEmailAcc, to, type);

        Transport.send(message);

    }

    public static Message prepareCovidReceiptMessage(javax.mail.Session mailSession, String myEmailAcc, String to, String type) {
        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(myEmailAcc));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(App.resourceBundle.getString("key.covid_survey"));
            if (type.equals("submitted")) message.setText(App.resourceBundle.getString("key.covid_email_receipt"));
            else if (type.equals("approved")) message.setText(App.resourceBundle.getString("key.covid_email_approved"));
            else if (type.equals("covid")) message.setText(App.resourceBundle.getString("key.covid_email_not_approved"));
            return message;
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}