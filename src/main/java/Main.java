import javax.mail.*;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        VaccineService vaccineService = new VaccineService();

        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime presentDate = LocalDateTime.now();

        while (true) {
           List<Center> centers = vaccineService.isAvailable();

           if (centers.size()>0){
               System.out.println("Vaccine is Available for " + date.format(presentDate));
               String message = "Vaccine slots are available in the following areas:\n" + centers.stream().map( Center::getAddress /* Method reference */ ).collect(Collectors.joining("\n"));
               String subject = "Available Vaccine Slots for " + date.format(presentDate);
               String to = "amanranapoiu@gmail.com";
               String from = "amanr.main@gmail.com";
               System.out.println("............ Sending all the available slot addresses to " + to + "...........");
               sendEmail(message,subject,to,from);
           }

           else{
               System.out.println("...Preparing to send message ...");
               String message = " Currently any vaccine is not available.";
               String subject = "NO Vaccine slot is available for "+date.format(presentDate);
               String to = "#recieverEmailID";
               String from = "#senderEmailID";
               sendEmail(message,subject,to,from);
               System.out.println("Vaccine is not Available");
           }

           //calls after X minutes
            // works when input pincode is a constant
//           Thread.sleep(60*1000);
       }
    }

    private static void sendEmail(String message, String subject, String to, String from) {

        //Variable for gmail
        String host="smtp.gmail.com";

        //get the system properties
        Properties properties = System.getProperties();
        System.out.println("PROPERTIES "+ properties);

        //setting important information to properties object

        //host set
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");

        Session session=Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("amanr.main@gmail.com", "1038Wow!");
            }
        });

        session.setDebug(true);

        MimeMessage m = new MimeMessage(session);

        try {

            m.setFrom(from);
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            m.setSubject(subject);
            m.setText(message);
            Transport.send(m);
            System.out.println("........All available slots addresses successfully sent from "+from+ " to "+to+"........");

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
