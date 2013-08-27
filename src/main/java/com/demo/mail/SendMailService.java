package com.demo.mail;

import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Package com.dangdang.logger.mail
 * @ClassName: SendMailService
 * @Description: �����ʼ�����ڷ���
 * @author mrajian
 * @Version V1.0
 * @date 2013-3-4 ����12:11:51
 */
@Service("sendMailService")
public class SendMailService {
	
	private static final Logger log  = LoggerFactory.getLogger(SendMailService.class.getName());
	
	//�ʼ���������ַ
//	@Value("${mail_server_host}")
	String mail_server_host;
	
	//�ʼ��������˿�
//	@Value("${mail_server_port}")
	String mail_server_port;
	
	//From�ʼ��˻�
//	@Value("${from_user_address}")
	String from_user_address;
	
	//From�ʼ��˻�����
//	@Value("${from_user_pass}")
	String from_user_pass;
	
	//To�ʼ��˻�
//	@Value("${to_user_address}")
	String to_user_address;
	
	/**
	 * �����ʼ�������ڷ���
	 * @param attachFiles : �ʼ�����·�� ��������Զ��ŷָ�
	 * @param mailSubject : �ʼ�����
	 * @param mailContent : �ʼ�����
	 * @return void
	 * @throws
	 * @author mrajian
	 * @date 2013-3-4 ����12:15:57
	 * @version V1.0
	 */
	public void sendMailService(String attachFiles, String mailSubject, String mailContent) {
		log.info("�ʼ���������ַ��{}", mail_server_host);
		log.info("�ʼ��������˿ڣ�{}", mail_server_port);
		log.info("From�ʼ��˻���{}", from_user_address);
		log.info("From�ʼ��˻����룺{}", from_user_pass);
		log.info("To�ʼ��˻���{}", to_user_address);
		log.info("�ʼ����⣺{}", mailSubject);
		log.info("�ʼ����ݣ�{}", mailContent);
		log.info("�ʼ�������{}", attachFiles);
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(mail_server_host);
		mailInfo.setMailServerPort(mail_server_port);
		mailInfo.setValidate(true);
		mailInfo.setUserName(from_user_address);
		mailInfo.setPassword(from_user_pass);// ������������
		mailInfo.setFromAddress(from_user_address);
		mailInfo.setToAddress(to_user_address);
		mailInfo.setSubject(mailSubject);
		mailInfo.setContent(mailContent);
		if (attachFiles != null && !attachFiles.equals("")) {
			String[] attachFileArray = attachFiles.split(",");
			mailInfo.setAttachFileNames(attachFileArray);
		}
		SimpleMailSender.sendHtmlMail(mailInfo);// ����html��ʽ
	}
	
	public static void main(String[] args) throws Exception {
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.126.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("tucjiawei@126.com");
		mailInfo.setPassword("WANGZHELAI");// ������������
		mailInfo.setFromAddress("tucjiawei@126.com");
		mailInfo.setToAddress("tucjiawei@126.com");
		mailInfo.setSubject("testproxy");
		mailInfo.setContent("test");
//		mailInfo.setAttachFileNames(new String[]{"e:/test.jpg"});
		
		// �������Ҫ�������ʼ�
	//	SimpleMailSender sms = new SimpleMailSender();
	//	sms.sendHtmlMail(mailInfo);
	//	sms.sendTextMail(mailInfo);// ���������ʽ
//		SimpleMailSender.sendHtmlMail(mailInfo);// ����html��ʽ
		
//		mailInfo.setMailServerHost("pop3.126.com");
//		mailInfo.setMailServerPort("25");
//		mailInfo.setUserName("tucjiawei@126.com");
//		mailInfo.setPassword("WANGZHELAI");// ������������
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
