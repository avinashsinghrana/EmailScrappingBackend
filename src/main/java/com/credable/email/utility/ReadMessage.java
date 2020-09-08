package com.credable.email.utility;

import com.credable.email.dto.UserDetails;
import com.credable.email.model.EmailRef;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;

import javax.mail.*;
import javax.mail.search.*;
import java.io.*;
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

    public List<EmailRef> getLastHoursMails(int hours, boolean msgType){
        return getLastHoursMails(hours, "inbox", msgType);
    }

    public List<EmailRef> getLastHoursMails(int hours, String inboxName, boolean msgType){
        Folder inbox = null;
        try {
            inbox = store.getFolder(inboxName);
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.search(lastHoursSearchTerm(hours, msgType));
            return getEmailRefs(messages);
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            closeFolder(inbox);
        }
        return new ArrayList<>();
    }

    private List<EmailRef> getEmailRefs(Message[] messages) throws MessagingException {
        List<EmailRef> emails = new ArrayList<>();
        for (Message message : messages) {
            EmailRef email = new EmailRef();
            email.setSender(message.getFrom()[0].toString());
            email.setSubject(message.getSubject());
            email.setTime(message.getReceivedDate().toString());
            email.setBodyContent(getContent(message));
            emails.add(email);
        }
        return emails;
    }

    private SearchTerm lastHoursSearchTerm(int hours, boolean msgType) {
        DateTime rightNow = new DateTime();
        DateTime past = rightNow.minusHours(hours);
        Flags seen = new Flags(Flags.Flag.SEEN);
        FlagTerm unseenFlagTerm = new FlagTerm(seen, msgType);
        SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GE, past.toDate());
        return new AndTerm(unseenFlagTerm, newerThan);
    }

    public String getContent(Message msg)

    {
        StringBuilder message = new StringBuilder();
        try {
            String contentType = msg.getContentType();
            System.out.println("Content Type : " + contentType);
            Multipart mp = (Multipart) msg.getContent();
                message.append(dumpPart(mp.getBodyPart(0)));
//                message.append(mp.getBodyPart(i).getContent().toString());
                message.append("\n");
        } catch (Exception ex) {
            System.out.println("Exception arise at get Content");
            ex.printStackTrace();
        }
        return message.toString();
    }

    public String dumpPart(Part p) throws Exception {
        StringBuilder lines = new StringBuilder();
        InputStream inputStream = p.getInputStream();
        if (!(inputStream instanceof BufferedInputStream)) {
            inputStream = new BufferedInputStream(inputStream);
        }

        int c;
        System.out.println("Message : ");
        while ((c = inputStream.read()) != -1) {
            lines.append((char) c);
            System.out.write(c);
        }
        return lines.toString();
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
