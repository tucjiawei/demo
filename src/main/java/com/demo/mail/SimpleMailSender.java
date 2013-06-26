package com.demo.mail;

import java.io.File;
import java.util.Date;
import java.util.Properties;

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
		Properties pro = mailInfo.getProperties();
		// �����Ҫ�����֤���򴴽�һ��������֤��
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
			
		}else{
			pro.setProperty("mail.smtp.auth", "false");
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
			mailMessage.setSentDate(new Date());
			// MiniMultipart����һ�������࣬����MimeBodyPart���͵Ķ���
			Multipart mainPart = new MimeMultipart();
			// ����һ������HTML���ݵ�MimeBodyPart
			BodyPart html = new MimeBodyPart();
			BodyPart gifpart = new MimeBodyPart();
			DataSource gifDs = new FileDataSource(new File("d:/1280367683639.jpg"));
			DataHandler gifDh = new DataHandler(gifDs);
			gifpart.setDataHandler(gifDh);
	        gifpart.setHeader("Content-ID", "it315logo_gif");
	        
	        html.setText("����һ��ͼƬ��һ�����ֵ�����");
	        html.setContent("<font color=red>����һ����Java�����Զ����ɵ��ʼ�������ظ���</font><img src='cid:it315logo_gif'>",  
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
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

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
	
}
