package com.demo.mail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.io.FileUtils;
  
/**  
 * ��һ���ʼ�����Ҫ����һ��ReciveMail����  
 */  
public class ReceiveOneMail {   
    private MimeMessage mimeMessage = null;   
    private String saveAttachPath = ""; //�������غ�Ĵ��Ŀ¼   
    private StringBuffer bodytext = new StringBuffer();//����ʼ�����   
    private String dateformat = "yy-MM-dd HH:mm"; //Ĭ�ϵ���ǰ��ʾ��ʽ   
  
    public ReceiveOneMail(MimeMessage mimeMessage) {   
        this.mimeMessage = mimeMessage;   
    }   
  
    public void setMimeMessage(MimeMessage mimeMessage) {   
        this.mimeMessage = mimeMessage;   
    }   
  
    /**  
     * ��÷����˵ĵ�ַ������  
     */  
    public String getFrom() throws Exception {   
        InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();   
        String from = address[0].getAddress();   
        if (from == null)   
            from = "";   
        String personal = address[0].getPersonal();   
        if (personal == null)   
            personal = "";   
        String fromaddr = personal + "<" + from + ">";   
        return fromaddr;   
    }   
  
    /**  
     * ����ʼ����ռ��ˣ����ͣ������͵ĵ�ַ�����������������ݵĲ����Ĳ�ͬ "to"----�ռ��� "cc"---�����˵�ַ "bcc"---�����˵�ַ  
     */  
    public String getMailAddress(String type) throws Exception {   
        String mailaddr = "";   
        String addtype = type.toUpperCase();   
        InternetAddress[] address = null;   
        if (addtype.equals("TO") || addtype.equals("CC")|| addtype.equals("BCC")) {   
            if (addtype.equals("TO")) {   
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);   
            } else if (addtype.equals("CC")) {   
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);   
            } else {   
                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);   
            }   
            if (address != null) {   
                for (int i = 0; i < address.length; i++) {   
                    String email = address[i].getAddress();   
                    if (email == null)   
                        email = "";   
                    else {   
                        email = MimeUtility.decodeText(email);   
                    }   
                    String personal = address[i].getPersonal();   
                    if (personal == null)   
                        personal = "";   
                    else {   
                        personal = MimeUtility.decodeText(personal);   
                    }   
                    String compositeto = personal + "<" + email + ">";   
                    mailaddr += "," + compositeto;   
                }   
                mailaddr = mailaddr.substring(1);   
            }   
        } else {   
            throw new Exception("Error emailaddr type!");   
        }   
        return mailaddr;   
    }   
  
    /**  
     * ����ʼ�����  
     */  
    public String getSubject() throws MessagingException {   
        String subject = "";   
        try {   
            subject = MimeUtility.decodeText(mimeMessage.getSubject());   
            if (subject == null)   
                subject = "";   
        } catch (Exception exce) {}   
        return subject;   
    }   
  
    /**  
     * ����ʼ���������  
     */  
    public String getSentDate() throws Exception {   
        Date sentdate = mimeMessage.getSentDate();   
        SimpleDateFormat format = new SimpleDateFormat(dateformat);   
        return format.format(sentdate);   
    }   
  
    /**  
     * ����ʼ���������  
     */  
    public String getBodyText() {   
        return bodytext.toString();   
    }   
  
    /**  
     * �����ʼ����ѵõ����ʼ����ݱ��浽һ��StringBuffer�����У������ʼ� ��Ҫ�Ǹ���MimeType���͵Ĳ�ִͬ�в�ͬ�Ĳ�����һ��һ���Ľ���  
     */  
    public void getMailContent(Part part) throws Exception {   
        String contenttype = part.getContentType();   
        int nameindex = contenttype.indexOf("name");   
        boolean conname = false;   
        if (nameindex != -1)   
            conname = true;   
//        System.out.println("CONTENTTYPE: " + contenttype);   
        if (part.isMimeType("text/plain") && !conname) {   
            bodytext.append((String) part.getContent());   
        } else if (part.isMimeType("text/html") && !conname) {   
            bodytext.append((String) part.getContent());   
        } else if (part.isMimeType("multipart/*")) {   
            Multipart multipart = (Multipart) part.getContent();   
            int counts = multipart.getCount();   
            for (int i = 0; i < counts; i++) {   
                getMailContent(multipart.getBodyPart(i));   
            }   
        } else if (part.isMimeType("message/rfc822")) {   
            getMailContent((Part) part.getContent());   
        } else {}   
    }   
  
    /**   
     * �жϴ��ʼ��Ƿ���Ҫ��ִ�������Ҫ��ִ����"true",���򷵻�"false"  
     */   
    public boolean getReplySign() throws MessagingException {   
        boolean replysign = false;   
        String needreply[] = mimeMessage   
                .getHeader("Disposition-Notification-To");   
        if (needreply != null) {   
            replysign = true;   
        }   
        return replysign;   
    }   
  
    /**  
     * ��ô��ʼ���Message-ID  
     */  
    public String getMessageId() throws MessagingException {   
        return mimeMessage.getMessageID();   
    }   
  
    /**  
     * ���жϴ��ʼ��Ƿ��Ѷ������δ�����ط���false,��֮����true��  
     */  
    public boolean isNew() throws MessagingException {   
        boolean isnew = false;   
        Flags flags = ((Message) mimeMessage).getFlags();   
        Flags.Flag[] flag = flags.getSystemFlags();   
        System.out.println("flags's length: " + flag.length);   
        for (int i = 0; i < flag.length; i++) {   
            if (flag[i] == Flags.Flag.SEEN) {   
                isnew = true;   
                System.out.println("seen Message.......");   
                break;   
            }   
        }   
        return isnew;   
    }   
  
    /**  
     * �жϴ��ʼ��Ƿ��������  
     */  
    public boolean isContainAttach(Part part) throws Exception {   
        boolean attachflag = false;   
        String contentType = part.getContentType();   
        if (part.isMimeType("multipart/*")) {   
            Multipart mp = (Multipart) part.getContent();   
            for (int i = 0; i < mp.getCount(); i++) {   
                BodyPart mpart = mp.getBodyPart(i);   
                String disposition = mpart.getDisposition();   
                if ((disposition != null)   
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition   
                                .equals(Part.INLINE))))   
                    attachflag = true;   
                else if (mpart.isMimeType("multipart/*")) {   
                    attachflag = isContainAttach((Part) mpart);   
                } else {   
                    String contype = mpart.getContentType();   
                    if (contype.toLowerCase().indexOf("application") != -1)   
                        attachflag = true;   
                    if (contype.toLowerCase().indexOf("name") != -1)   
                        attachflag = true;   
                }   
            }   
        } else if (part.isMimeType("message/rfc822")) {   
            attachflag = isContainAttach((Part) part.getContent());   
        }   
        return attachflag;   
    }   
  
    /**   
     * �����渽����   
     */   
    public void saveAttachMent(Part part) throws Exception {   
        String fileName = "";   
        if (part.isMimeType("multipart/*")) {   
            Multipart mp = (Multipart) part.getContent();   
            for (int i = 0; i < mp.getCount(); i++) {   
                BodyPart mpart = mp.getBodyPart(i);   
                String disposition = mpart.getDisposition();   
                if ((disposition != null)   
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition   
                                .equals(Part.INLINE)))) {   
                    fileName = mpart.getFileName();   
                    if (fileName.toLowerCase().indexOf("gb2312") != -1) {   
                        fileName = MimeUtility.decodeText(fileName);   
                    }   
                    saveFile(fileName, mpart.getInputStream());   
                } else if (mpart.isMimeType("multipart/*")) {   
                    saveAttachMent(mpart);   
                } else {   
                    fileName = mpart.getFileName();   
                    if ((fileName != null)   
                            && (fileName.toLowerCase().indexOf("GB2312") != -1)) {   
                        fileName = MimeUtility.decodeText(fileName);   
                        saveFile(fileName, mpart.getInputStream());   
                    }   
                }   
            }   
        } else if (part.isMimeType("message/rfc822")) {   
            saveAttachMent((Part) part.getContent());   
        }   
    }   
  
    /**   
     * �����ø������·����   
     */   
  
    public void setAttachPath(String attachpath) {   
        this.saveAttachPath = attachpath;   
    }   
  
    /**  
     * ������������ʾ��ʽ��  
     */  
    public void setDateFormat(String format) throws Exception {   
        this.dateformat = format;   
    }   
  
    /**  
     * ����ø������·����  
     */  
    public String getAttachPath() {   
        return saveAttachPath;   
    }   
  
    /**  
     * �������ı��渽����ָ��Ŀ¼�  
     */  
    private void saveFile(String fileName, InputStream in) throws Exception {   
        String osName = System.getProperty("os.name");   
        String storedir = getAttachPath();   
        String separator = "";   
        if (osName == null)   
            osName = "";   
        if (osName.toLowerCase().indexOf("win") != -1) {   
            separator = "\\";  
            if (storedir == null || storedir.equals(""))  
                storedir = "c:\\tmp";  
        } else {  
            separator = "/";  
            storedir = "/tmp";  
        }  
        File storefile = new File(storedir + separator + fileName);  
        System.out.println("storefile's path: " + storefile.toString());  
        // for(int i=0;storefile.exists();i++){  
        // storefile = new File(storedir+separator+fileName+i);  
        // }  
        BufferedOutputStream bos = null;  
        BufferedInputStream bis = null;  
        try {  
            bos = new BufferedOutputStream(new FileOutputStream(storefile));  
            bis = new BufferedInputStream(in);  
            int c;  
            while ((c = bis.read()) != -1) {  
                bos.write(c);  
                bos.flush();  
            }  
        } catch (Exception exception) {  
            exception.printStackTrace();  
            throw new Exception("�ļ�����ʧ��!");  
        } finally {  
            bos.close();  
            bis.close();  
        }  
    }  
 
    /**  
     * PraseMimeMessage�����  
     */  
    public static void main(String args[]) throws Exception {  
        Properties props = System.getProperties();  
        props.put("mail.smtp.host", "smtp.163.com");  
        props.put("mail.smtp.auth", "true");  
        Session session = Session.getDefaultInstance(props, null);  
        URLName urln = new URLName("pop3", "pop3.126.com", 110, null,  
                "diudiudewo", "minnie");  
        Store store = session.getStore(urln);  
        store.connect();  
        Folder folder = store.getFolder("INBOX");  
        folder.open(Folder.READ_ONLY);  
        Message message[] = folder.getMessages();  
        System.out.println("Messages's length: " + message.length);  
        ReceiveOneMail pmm = null;  
//        String s = "�ռ��� dongcheng3297370@163.com";
//    	Pattern p = Pattern.compile("�ռ���\\s*[A-Za-z0-9-_.+%]+@[A-Za-z0-9-.]+\\.[A-Za-z]{2,4}$");
//    	Matcher m = p.matcher(s);
//    	while(m.find()){
//    		System.out.println(m.group());
//    	}
        for (int i = 0; i < message.length; i++) {  
            
            pmm = new ReceiveOneMail((MimeMessage) message[i]);  
//            System.out.println("Message " + i + " subject: " + pmm.getSubject());  
//            System.out.println("Message " + i + " sentdate: "+ pmm.getSentDate());  
//            System.out.println("Message " + i + " replysign: "+ pmm.getReplySign());  
//            System.out.println("Message " + i + " hasRead: " + pmm.isNew());  
//            System.out.println("Message " + i + "  containAttachment: "+ pmm.isContainAttach((Part) message[i]));  
//            System.out.println("Message " + i + " form: " + pmm.getFrom());  
            
//            pmm.setDateFormat("yy��MM��dd�� HH:mm");  
//            System.out.println("Message " + i + " sentdate: "+ pmm.getSentDate());  
//            System.out.println("Message " + i + " Message-ID: "+ pmm.getMessageId());  
//            // ����ʼ�����===============  
            try{
	            pmm.getMailContent((Part) message[i]);  
	            if(pmm.getBodyText().contains("���˵���һȦ��������Ȼ�޷��ҵ���Ҫ���͵��ʼ���ַ���ʼ�ֻ���ȸ����ӻ����ˡ�")){
	//            	System.out.println("Message " + i + " to: "+ pmm.getMailAddress("to"));  
	//                System.out.println("Message " + i + " cc: "+ pmm.getMailAddress("cc"));  
	//                System.out.println("Message " + i + " bcc: "+ pmm.getMailAddress("bcc"));  
	//            	System.out.println("Message " + i + " bodycontent: \r\n"  
	//	                    + pmm.getBodyText());  
	            	System.out.println("======================");  
	            	String content = pmm.getBodyText();
	//            	System.out.println(content);
	//            	String s = "�ռ��� dongcheng3297370@163.com";
	            	Pattern p = Pattern.compile("�ռ���\\s*([A-Za-z0-9-_.+%]+@[A-Za-z0-9-.]+\\.[A-Za-z]{2,4})");
	            	Matcher m = p.matcher(content);
	            	while(m.find()){
	            		File logFile = FileUtils.getFile("c:/mailList.txt");
	            		FileUtils.writeStringToFile(logFile, m.group(1)+System.getProperty("line.separator"), true);
//	            		System.out.println(m.group(1));
	            	}
	            }            
	//            pmm.setAttachPath("c:\\");   
	//            pmm.saveAttachMent((Part) message[i]);   
            }catch(Exception e){
            	continue;
            }
        } 
    }   
}