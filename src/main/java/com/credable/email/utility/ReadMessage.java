package com.credable.email.utility;

import com.credable.email.dto.UserDetails;
import com.credable.email.model.EmailRef;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;

import javax.mail.*;
import javax.mail.search.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReadMessage {
    private Store store;
    public ReadMessage(UserDetails userDetails) {
        Properties properties = new Properties();
        properties.setProperty("mail.host", "imap.gmail.com");
        properties.setProperty("mail.port", "995");
        properties.setProperty("mail.transport.protocol", "imaps");
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userDetails.getUser(), userDetails.getPassword());
                    }
                });
        try {
            store = session.getStore("imaps");
            store.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<EmailRef> getLastHoursMails(int hours){
        return getLastHoursMails(hours, "inbox");
    }

    public List<EmailRef> getLastHoursMails(int hours, String inboxName){
        Folder inbox = null;
        try {
            inbox = store.getFolder(inboxName);
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.search(lastHoursSearchTerm(hours));
            for(Message message : messages) {
                System.out.println(message);
                Address[] from = message.getFrom();
                System.out.println("-------------------------------");
                System.out.println("Date : " + message.getSentDate());
                System.out.println("From : " + from[0]);
                System.out.println("Subject: " + message.getSubject());
                System.out.println("Content :"+ Jsoup.parse(message.getContent().toString()).text());
                System.out.println("--------------------------------");
            }
            return getEmailRefs(messages);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        } finally {
            closeFolder(inbox);
        }
        return new ArrayList<>();
    }

    private String processMessageBody(Message message) {
        String messageBody = "";
        try {
            Object content = message.getContent();
            if (content instanceof String) {
                messageBody += Jsoup.parse(content.toString()).text();
            }
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
        return messageBody;
    }
    private List<EmailRef> getEmailRefs(Message[] messages) throws MessagingException {
        List<EmailRef> emails = new ArrayList<>();
        for (Message message : messages) {
            EmailRef email = new EmailRef();
            email.setSender(message.getFrom()[0].toString());
            email.setSubject(message.getSubject());
            email.setTime(message.getReceivedDate().toString());
            email.setText(processMessageBody(message));
            emails.add(email);
        }
        return emails;
    }

    private SearchTerm lastHoursSearchTerm(int hours) {
        DateTime rightNow = new DateTime();
        DateTime past = rightNow.minusHours(hours);
        Flags seen = new Flags(Flags.Flag.SEEN);
        FlagTerm unseenFlagTerm = new FlagTerm(seen, true);
        SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GE, past.toDate());
        return new AndTerm(unseenFlagTerm, newerThan);
    }

    private void closeFolder(Folder inbox) {
        try {
            if (inbox != null)
                inbox.close(false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
