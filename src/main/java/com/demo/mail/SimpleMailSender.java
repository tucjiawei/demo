package com.demo.mail;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Package com.dangdang.logger.mail
 * @ClassName: SimpleMailSender
 * @Description: ���ʼ��������������ʼ���������
 * @author mrajian
 * @Version V1.0
 * @date 2013-3-1 ����02:38:55
 */
public class SimpleMailSender {
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleMailSender.class);
	/**
	 * ���ı���ʽ�����ʼ�
	 * 
	 * @param mailInfo
	 *            �����͵��ʼ�����Ϣ
	 */
	public boolean sendTextMail(MailSenderInfo mailInfo) {
		// �ж��Ƿ���Ҫ�����֤
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// �����Ҫ�����֤���򴴽�һ��������֤��
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// �����ʼ��Ự���Ժ�������֤������һ�������ʼ���session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			// ����session����һ���ʼ���Ϣ
			Message mailMessage = new MimeMessage(sendMailSession);
			// �����ʼ������ߵ�ַ
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// �����ʼ���Ϣ�ķ�����
			mailMessage.setFrom(from);
			// �����ʼ��Ľ����ߵ�ַ�������õ��ʼ���Ϣ��
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// �����ʼ���Ϣ������
			mailMessage.setSubject(mailInfo.getSubject());
			// �����ʼ���Ϣ���͵�ʱ��
			mailMessage.setSentDate(new Date());
			// �����ʼ���Ϣ����Ҫ����
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);
			// �����ʼ�
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * ��HTML��ʽ�����ʼ�
	 * 
	 * @param mailInfo
	 *            �����͵��ʼ���Ϣ
	 */
	public static boolean sendHtmlMail(MailSenderInfo mailInfo) {
		// �ж��Ƿ���Ҫ�����֤
		MyAuthenticator authenticator = null;
		Properties props = mailInfo.getProperties();
		// �����Ҫ�����֤���򴴽�һ��������֤��
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
			
		}else{
			props.setProperty("mail.smtp.auth", "false");
		}
//		props.setProperty("proxySet","true");
//        props.setProperty("socksProxyHost","192.16.155.10");
//        props.setProperty("socksProxyPort","1080");
		// �����ʼ��Ự���Ժ�������֤������һ�������ʼ���session
		Session sendMailSession = Session
				.getDefaultInstance(props, authenticator);
		try {
			// ����session����һ���ʼ���Ϣ
			Message mailMessage = new MimeMessage(sendMailSession);
			// �����ʼ������ߵ�ַ
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// �����ʼ���Ϣ�ķ�����
			mailMessage.setFrom(from);
			// �����ʼ��Ľ����ߵ�ַ�������õ��ʼ���Ϣ��
			 Address[] tos = null;
	           //��ҪȺ���������ַ�����˸��ַ����� �� ������
	            String[] receivers =org.apache.commons.lang.StringUtils.split(mailInfo.getToAddress(),";");
	            if (receivers != null){
	                // Ϊÿ���ʼ������ߴ���һ����ַ
	                tos = new InternetAddress[receivers.length];
	                           for (int i=0; i<receivers.length; i++){
	                	   String s=receivers[i];
	                                   tos[i] = new InternetAddress(s);
	                            }
	            } 			
	            // �����н����ߵ�ַ����ӵ��ʼ�������������
	            mailMessage.setRecipients(Message.RecipientType.TO, tos);
			// �����ʼ���Ϣ������
			mailMessage.setSubject(mailInfo.getSubject());
			// �����ʼ���Ϣ���͵�ʱ��
//			mailMessage.setSentDate(new Date());
			// MiniMultipart����һ�������࣬����MimeBodyPart���͵Ķ���
			Multipart mainPart = new MimeMultipart();
			// ����һ������HTML���ݵ�MimeBodyPart
			BodyPart html = new MimeBodyPart();
			BodyPart gifpart = new MimeBodyPart();
			DataSource gifDs = new FileDataSource(new File("e:/test.jpg"));
			DataHandler gifDh = new DataHandler(gifDs);
			gifpart.setDataHandler(gifDh);
	        gifpart.setHeader("Content-ID", "it315logo_gif");
	        
	        html.setText("����һ��ͼƬ��һ�����ֵ�����");
	        html.setContent("<div>���Ը���ͼƬ���ʼ�����ʾ</div><img src='cid:it315logo_gif'>",  
	      	    "text/html;charset=gb2312");
			// ����HTML����
//			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
	        mainPart.addBodyPart(gifpart);
//			mainPart.addBodyPart(html);
			// ��MiniMultipart��������Ϊ�ʼ�����
			//���ø���
//			Multipart multipart = setAttMultipart(mailInfo.getAttachFileNames());
//			Multipart multipart = new MimeMultipart();
//			multipart.addBodyPart(html);
//			setAttMultipart(multipart, mailInfo.getAttachFileNames());
//			mailMessage.setContent(multipart,"text/html;charset=GB2312");
			
			
			/*MimeMultipart multipart = new MimeMultipart();
			   BodyPart msgBodyPart = new MimeBodyPart();//���������ı�����
			   msgBodyPart.setContent(mailInfo.getContent(),
			     "text/plain;charset=UTF-8");
			   multipart.addBodyPart(msgBodyPart);
			setAttMultipart(multipart,mailInfo.getAttachFileNames());   */
			mailMessage.setContent(mainPart);
			// �����ʼ�
			/*Transport transport = sendMailSession.getTransport("smtp");
		    transport.connect(mailInfo.getMailServerHost(), mailInfo.getUserName(),
					mailInfo.getPassword());
		    transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
		    transport.close();*/
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unused")
	private static void setAttMultipart(Multipart multipart,String[] attachments){
		try {
			BodyPart attBodyPart ;
		   DataSource ds ;
		   if(attachments!=null&&attachments.length!=0){
			   for (int i=0;i<attachments.length;i++){
				   attBodyPart = new MimeBodyPart();//�������ø���
				   ds = new FileDataSource(new File(attachments[i]));
				   attBodyPart.setDataHandler(new DataHandler(ds));//����DataHandler
				   attBodyPart.setFileName(ds.getName());//��������ʾ����
				   multipart.addBodyPart(attBodyPart);
				 }   
		   }
		} catch (MessagingException e) {
			logger.error("��Ӹ�������",e);
		}
	}

	public static void readMail(MailSenderInfo mailInfo) {
		MyAuthenticator authenticator = null;
		Properties props = new Properties();//mailInfo.getProperties();
		props.put("mail.store.protocol", "pop3");
		props.put("mail.pop3.host", "pop3.126.com");
		props.put("mail.pop3.port", "110");
//		props.put("mail.pop3.user", "");
		props.put("mail.pop3.timeout", "158000");
		props.put("mail.pop3.connectiontimeout", "158000");
		// �����Ҫ�����֤���򴴽�һ��������֤��
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
			
		}else{
			props.setProperty("mail.smtp.auth", "false");
		}
		// �����ʼ��Ự���Ժ�������֤������һ�������ʼ���session
		Session session = Session.getDefaultInstance(props, authenticator);
		Store store = null;
        Folder inbox = null;
        
        try {
        	store = session.getStore("pop3");
			store.connect(mailInfo.getMailServerHost(), mailInfo.getUserName(), mailInfo.getPassword());
			inbox = store.getFolder("Inbox");
	        inbox.open(Folder.READ_ONLY);

	        // get the list of inbox messages
	        int totalMsg = inbox.getMessageCount();
	        System.out.println("totalMsg:"+totalMsg);
	        int unread = inbox.getUnreadMessageCount();
	        System.out.println("unread:"+unread);
	        Message[] messages = inbox.getMessages();

	        System.out.print("size " + messages .length);
	        if (messages.length == 0) System.out.println("No messages found.");

	        for (int i = messages.length; i > 0; i--) {

	            // stop after listing ten messages
	            if (i < messages.length) {
	                System.exit(0);
	                inbox.close(true);
	                store.close();
	            }

	            System.out.println("Message " + (i));
	            System.out.println("From : " + messages[i-1].getFrom()[0]);
	            System.out.println("Subject : " + messages[i-1].getSubject());
	            Object content = messages[i-1].getContent();
	            
//	            String contentType = messages[i-1].getContentType();
	            
//	            Multipart multiPart = (Multipart)content;
	            if (content instanceof Multipart) {
	                Multipart multipart = (Multipart) content;
	                for (int j = 0; j < multipart.getCount(); j++) {

	                 BodyPart bodyPart = multipart.getBodyPart(j);

	                 String disposition = bodyPart.getDisposition();

	                 if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) { 
	                     System.out.println("Mail have some attachment");
	                     DataHandler handler = bodyPart.getDataHandler();
	                     System.out.println("file name : " + handler.getName());                                 
	                   }
	                 else { 
	                         getText(bodyPart);  // the changed code         
	                   }
	               }
	            }
	            else                
	                content= messages[i].getContent().toString();

//	            if (content instanceof String)  
//	                System.out.print(content);  
	            /* text/plain = String
	             * multipart" = Multipart
	             * MimeMessage
	             * input stream = Unknown Data Handler 
	             */

	        }

	        inbox.close(true);
	        store.close();
		} catch (NoSuchProviderException e) {
            System.out.println(e.getMessage());
        } catch (MessagingException e) {
            System.out.println(e.getMessage());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } 
        
	}
	/**
     * Return the primary text content of the message.
     */
    private static String getText(Part p) throws
                MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            @SuppressWarnings("unused")
			boolean textIsHtml = p.isMimeType("text/html");
            Pattern pn = Pattern.compile("�ռ���\\s+?((\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+))");
            Matcher m = pn.matcher(s);
            while(m.find()){
            	System.out.println(m.group(1));
            }
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }
	
}
