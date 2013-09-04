package com.demo.mail;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
 
/**
 * This program demonstrates how to download e-mail messages and save
 * attachments into files on disk.
 *
 * @author www.codejava.net
 *
 */
public class EmailRead {
 
    /**
     * Downloads new messages and saves attachments to disk if any.
     * @param host
     * @param port
     * @param userName
     * @param password
     * @param readEmailCallback 
     */
    public static void downloadEmailAttachments(MailInfo mailInfo, ReadEmailCallback readEmailCallback) {
        Properties properties = new Properties();
 
        // server setting
        properties.put("mail.pop3.host", mailInfo.getMailServerHost());
        properties.put("mail.pop3.port", mailInfo.getMailServerPort());
 
        // SSL setting
//        properties.setProperty("mail.pop3.socketFactory.class",
//                "javax.net.ssl.SSLSocketFactory");
//        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
//        properties.setProperty("mail.pop3.socketFactory.port",
//                String.valueOf(port));
 
        Session session = Session.getDefaultInstance(properties);
 
        try {
            Store store = session.getStore("pop3");
            store.connect(mailInfo.getUserName(), mailInfo.getPassword());
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);
 
            Message[] arrayMessages = folderInbox.getMessages();
            for (int i = 0; i<arrayMessages.length; i++) {
                Message message = arrayMessages[i];
                Address[] fromAddress = message.getFrom();
                String from = fromAddress[0].toString();
                String subject = message.getSubject();
                String sentDate = message.getSentDate().toString();
 
                System.out.println(subject);
                System.out.println(from);
                System.out.println(sentDate);
 
                String content = parseEmailContent(message.getContent(),mailInfo);
 
                readEmailCallback.execute(content);
            }
            
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for pop3.");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static String parseEmailContent(Object object, MailInfo mailInfo) throws MessagingException, IOException{
    	String content = null;
    	if(object instanceof Multipart){
    		Multipart multiPart = (Multipart)object;
    		int numberOfParts = multiPart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    String fileName = mailInfo.getAttachDir() + File.separator + part.getFileName();
                    mailInfo.getAttachFileNames().add(fileName);
                    part.saveFile(fileName);
                    
                } else {
                	parseEmailContent(part.getContent(),mailInfo);
                }
            }

    	}else {
        	content = object.toString();
        }
    	return content;
    }
 
    public static void main(String[] args) {
        String host = "pop3.126.com";
        String port = "110";
        String userName = "tucjiawei@126.com";
        String password = "WANGZHELAI";
        String saveDirectory = "D:/Attachment";
        
        MailInfo mailInfo = new MailInfo();
        mailInfo.setUserName(userName);
        mailInfo.setPassword(password);
        mailInfo.setMailServerHost(host);
        mailInfo.setMailServerPort(port);
        mailInfo.setAttachDir(saveDirectory);
 
        EmailRead.downloadEmailAttachments(mailInfo,new ReadEmailCallbackImpl());
 
    }
}