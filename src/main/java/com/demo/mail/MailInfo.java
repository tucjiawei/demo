package com.demo.mail;

import java.util.List;
import java.util.Properties;

/**
 * @Package com.dangdang.logger.mail
 * @ClassName: MailSenderInfo
 * @Description: 发送邮件需要使用的基本信息
 * @author mrajian
 * @Version V1.0
 * @date 2013-3-1 下午02:36:24
 */
public class MailInfo {
	// 发送邮件的服务器的IP和端口
	private String mailServerHost;
	private String mailServerPort;
	// 邮件发送者的地址
	private String fromAddress;
	// 邮件接收者的地址
	private String toAddress;
	// 登陆邮件发送服务器的用户名和密码
	private String userName;
	private String password;
	// 是否需要身份验证
	private boolean validate;
	// 邮件主题
	private String subject;
	// 邮件的文本内容
	private String content;
	// 邮件附件的文件名
	private List<String> attachFileNames;
	
	private String attachDir;

	/**
	 * 获得邮件会话属性
	 */
	public Properties getProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.host", this.mailServerHost);
		props.put("mail.smtp.port", this.mailServerPort);
		props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "false");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
		return props;
	}

	public String getMailServerHost() {
		return mailServerHost;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String textContent) {
		this.content = textContent;
	}

	public String getAttachDir() {
		return attachDir;
	}

	public void setAttachDir(String attachDir) {
		this.attachDir = attachDir;
	}

	public void setAttachFileNames(List<String> attachFileNames) {
		this.attachFileNames = attachFileNames;
	}

	public List<String> getAttachFileNames() {
		return attachFileNames;
	}
}
