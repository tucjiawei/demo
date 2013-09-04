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
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties props = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
			
		}else{
			props.setProperty("mail.smtp.auth", "false");
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(props, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address[] tos = null;
	           //将要群发的邮箱地址存在了个字符串中 用 ；隔开
            String[] receivers =StringUtils.split(mailInfo.getToAddress(),";");
            if (receivers != null){
                // 为每个邮件接收者创建一个地址
                tos = new InternetAddress[receivers.length];
                for (int i=0; i<receivers.length; i++){
                	   String s=receivers[i];
                       tos[i] = new InternetAddress(s);
                }
            } 			
            // 将所有接收者地址都添加到邮件接收者属性中
            mailMessage.setRecipients(Message.RecipientType.BCC, tos);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
//			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
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
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			
	        html.setContent(mailInfo.getContent(), "text/html;charset=gb18030");
			// 设置HTML内容
			mainPart.addBodyPart(html);
			for(BodyPart gifPart:gifParts){
				mainPart.addBodyPart(gifPart);
			}
	        
			mailMessage.setContent(mainPart);
			// 发送邮件
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
