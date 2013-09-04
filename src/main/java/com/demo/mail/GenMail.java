package com.demo.mail;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class GenMail {
	public static void main(String[] args) throws IOException {
		String encoding = "utf-8";
		String baseDir = "E:/taobao/mail";
		String contentDir = baseDir+"/content/1";
		String content = FileUtils.readFileToString(new File(contentDir+"/html.txt"),encoding);
		List<String> lines = FileUtils.readLines(new File(contentDir+"/replace.txt"), encoding);
		lines.remove(0);
		List<String[]> lineSplit = new ArrayList<String[]>();
		for(String line:lines){
			lineSplit.add(line.split("\\+{5}"));
		}
		for(String[] replace:lineSplit){
			content = StringUtils.replaceOnce(content, "{href}",	StringUtils.trim(replace[0]));
		}
		for(String[] replace:lineSplit){
			content = StringUtils.replaceOnce(content, "{title}",	replace[1]);
		}
		FileUtils.write(new File(contentDir+"/test.html"), content, encoding);
		
		List<String> senders = FileUtils.readLines(new File(baseDir+"/sender/sender.txt"));
		
		int position = Integer.parseInt(FileUtils.readFileToString(new File(baseDir+"/position.txt")));
		int ccCount = 39;
		int crlfSize = System.getProperty("line.separator").length();
		RandomAccessFile ccFile= new RandomAccessFile(baseDir+"/receive/allmail.txt","r");
		ccFile.seek(position);
		String str = null;
		for(int t=0;t<6;t++){
			String ccAddr = "tucjiawei@126.com;";
			int index=0;
			while(index<ccCount && (str=ccFile.readLine())!=null){
				ccAddr+=str+";";
				position+=str.length()+crlfSize;
				index++;
			}
			FileUtils.write(new File(baseDir+"/position.txt"), position+"");
			ccAddr = StringUtils.chomp(ccAddr, ";");
			for(String sender:senders){
				MailInfo mailInfo = new MailInfo();
				mailInfo.setMailServerHost("smtp.126.com");
				mailInfo.setMailServerPort("25");
				mailInfo.setValidate(true);
				mailInfo.setUserName(sender);
				mailInfo.setPassword("diudiumofawu1");
				mailInfo.setFromAddress(sender);
				mailInfo.setToAddress(ccAddr);
				mailInfo.setSubject("[分享]---又一波潘多拉来袭");
				mailInfo.setContent(content);
				String[] attachments = new String[lineSplit.size()];
				int i=0;
				for(String[] image:lineSplit){
					attachments[i++]=contentDir+"/"+image[2];
				}
				mailInfo.setAttachFileNames(Arrays.asList(attachments));
				EmailSend.sendHtmlMail(mailInfo);
			}
		}
		
	}
	

}
