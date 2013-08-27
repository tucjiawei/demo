package com.demo.mail;

import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Package com.dangdang.logger.mail
 * @ClassName: SendMailService
 * @Description: 发送邮件的入口方法
 * @author mrajian
 * @Version V1.0
 * @date 2013-3-4 下午12:11:51
 */
@Service("sendMailService")
public class SendMailService {
	
	private static final Logger log  = LoggerFactory.getLogger(SendMailService.class.getName());
	
	//邮件服务器地址
//	@Value("${mail_server_host}")
	String mail_server_host;
	
	//邮件服务器端口
//	@Value("${mail_server_port}")
	String mail_server_port;
	
	//From邮件账户
//	@Value("${from_user_address}")
	String from_user_address;
	
	//From邮件账户密码
//	@Value("${from_user_pass}")
	String from_user_pass;
	
	//To邮件账户
//	@Value("${to_user_address}")
	String to_user_address;
	
	/**
	 * 发送邮件的主入口方法
	 * @param attachFiles : 邮件附件路径 多个附件以逗号分隔
	 * @param mailSubject : 邮件主题
	 * @param mailContent : 邮件内容
	 * @return void
	 * @throws
	 * @author mrajian
	 * @date 2013-3-4 下午12:15:57
	 * @version V1.0
	 */
	public void sendMailService(String attachFiles, String mailSubject, String mailContent) {
		log.info("邮件服务器地址：{}", mail_server_host);
		log.info("邮件服务器端口：{}", mail_server_port);
		log.info("From邮件账户：{}", from_user_address);
		log.info("From邮件账户密码：{}", from_user_pass);
		log.info("To邮件账户：{}", to_user_address);
		log.info("邮件主题：{}", mailSubject);
		log.info("邮件内容：{}", mailContent);
		log.info("邮件附件：{}", attachFiles);
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(mail_server_host);
		mailInfo.setMailServerPort(mail_server_port);
		mailInfo.setValidate(true);
		mailInfo.setUserName(from_user_address);
		mailInfo.setPassword(from_user_pass);// 您的邮箱密码
		mailInfo.setFromAddress(from_user_address);
		mailInfo.setToAddress(to_user_address);
		mailInfo.setSubject(mailSubject);
		mailInfo.setContent(mailContent);
		if (attachFiles != null && !attachFiles.equals("")) {
			String[] attachFileArray = attachFiles.split(",");
			mailInfo.setAttachFileNames(attachFileArray);
		}
		SimpleMailSender.sendHtmlMail(mailInfo);// 发送html格式
	}
	
	public static void main(String[] args) throws Exception {
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.126.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("tucjiawei@126.com");
		mailInfo.setPassword("WANGZHELAI");// 您的邮箱密码
		mailInfo.setFromAddress("tucjiawei@126.com");
		mailInfo.setToAddress("tucjiawei@126.com");
		mailInfo.setSubject("testproxy");
		mailInfo.setContent("test");
//		mailInfo.setAttachFileNames(new String[]{"e:/test.jpg"});
		
		// 这个类主要来发送邮件
	//	SimpleMailSender sms = new SimpleMailSender();
	//	sms.sendHtmlMail(mailInfo);
	//	sms.sendTextMail(mailInfo);// 发送文体格式
//		SimpleMailSender.sendHtmlMail(mailInfo);// 发送html格式
		
//		mailInfo.setMailServerHost("pop3.126.com");
//		mailInfo.setMailServerPort("25");
//		mailInfo.setUserName("tucjiawei@126.com");
//		mailInfo.setPassword("WANGZHELAI");// 您的邮箱密码
//		SimpleMailSender.readMail(mailInfo);
		int path = System.getProperty("line.separator").length();
		RandomAccessFile f= new RandomAccessFile("E:/tmp/ips.txt","r");
		String str = null;
		int i=0;
		int t=0;
		while(i<10 && (str=f.readLine())!=null){
			System.out.println(str);
			t+=str.length()+path;
			i++;
		}
		System.out.println(t);
		f.seek(t);
		i=0;
		while(i<10 && (str=f.readLine())!=null){
			t+=str.length();
			i++;
		}
	}
	
	

}
