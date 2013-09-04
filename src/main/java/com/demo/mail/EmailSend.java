package com.demo.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.StringUtils;

public class EmailSend {
	public static boolean sendHtmlMail(MailInfo mailInfo) {
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
            String[] receivers =StringUtils.split(mailInfo.getToAddress(),";");
            if (receivers != null){
                // Ϊÿ���ʼ������ߴ���һ����ַ
                tos = new InternetAddress[receivers.length];
                for (int i=0; i<receivers.length; i++){
                	   String s=receivers[i];
                       tos[i] = new InternetAddress(s);
                }
            } 			
            // �����н����ߵ�ַ����ӵ��ʼ�������������
            mailMessage.setRecipients(Message.RecipientType.BCC, tos);
			// �����ʼ���Ϣ������
			mailMessage.setSubject(mailInfo.getSubject());
			// �����ʼ���Ϣ���͵�ʱ��
//			mailMessage.setSentDate(new Date());
			// MiniMultipart����һ�������࣬����MimeBodyPart���͵Ķ���
			Multipart mainPart = new MimeMultipart();
			
			List<BodyPart> gifParts = new ArrayList<BodyPart>();
			if(mailInfo.getAttachFileNames()!=null){
				String content = mailInfo.getContent();
				for(String attacheFileName:mailInfo.getAttachFileNames()){
					BodyPart gifpart = new MimeBodyPart();
					DataSource gifDs = new FileDataSource(new File(attacheFileName));
					DataHandler gifDh = new DataHandler(gifDs);
					gifpart.setDataHandler(gifDh);
					String cid = genCid();
			        gifpart.setHeader("Content-ID", cid);
			        content = StringUtils.replaceOnce(content, "{image}", "cid:"+cid);
			        gifParts.add(gifpart);
				}
				
				mailInfo.setContent(content);
			}
			// ����һ������HTML���ݵ�MimeBodyPart
			BodyPart html = new MimeBodyPart();
			
	        html.setContent(mailInfo.getContent(), "text/html;charset=gb18030");
			// ����HTML����
			mainPart.addBodyPart(html);
			for(BodyPart gifPart:gifParts){
				mainPart.addBodyPart(gifPart);
			}
	        
			mailMessage.setContent(mainPart);
			// �����ʼ�
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	private static String genCid() {
		return UUID.randomUUID().toString();
	}
}
