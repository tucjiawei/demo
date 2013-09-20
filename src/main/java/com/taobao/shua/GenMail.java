package com.taobao.shua;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.demo.mail.EmailSend;
import com.demo.mail.MailInfo;

public class GenMail {
	public static void main(String[] args) throws IOException {
		String encoding = "utf-8";
		String baseDir = "E:/taobao/mail";
		
		List<String> senders = FileUtils.readLines(new File(baseDir+"/sender/sender.txt"));
		
		int position = Integer.parseInt(FileUtils.readFileToString(new File(baseDir+"/position.txt")));
		position = 0;
		int ccCount = 39;
		int crlfSize = System.getProperty("line.separator").length();
		RandomAccessFile ccFile= new RandomAccessFile(baseDir+"/receive/allmail.txt","r");
		ccFile.seek(position);
		String str = null;
		int senderIndex=0;
		for(String sender:senders){
			MailInfo mailInfo = new MailInfo();
			mailInfo.setMailServerHost("smtp.126.com");
			mailInfo.setMailServerPort("25");
			mailInfo.setValidate(true);
			mailInfo.setUserName(sender);
			mailInfo.setPassword("diudiumofawu1");
			mailInfo.setFromAddress(sender);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String contentDir = baseDir+"/content/"+sdf.format(new Date())+"/"+(++senderIndex);
			//没有内容要发送，退出
			if(!new File(contentDir).exists()){
				break;
			}
			List<String> lines = null;
			String content = "";
			try{
				content = FileUtils.readFileToString(new File(contentDir+"/html.txt"),encoding);
				lines = FileUtils.readLines(new File(contentDir+"/replace.txt"), encoding);
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
			if(CollectionUtils.isNotEmpty(lines)){
				lines.remove(0);
			}
			List<String[]> lineSplit = new ArrayList<String[]>();
			for(String line:lines){
				String[] arr = line.split("\\+{5}");
				if(ArrayUtils.getLength(arr)!=3){
					arr = new String[3];
				}
				lineSplit.add(arr);
			}
			for(String[] replace:lineSplit){
				content = StringUtils.replaceOnce(content, "{href}",	StringUtils.trimToEmpty(replace[0]));
			}
			for(String[] replace:lineSplit){
				content = StringUtils.replaceOnce(content, "{title}",	StringUtils.trimToEmpty(replace[1]));
			}
			
			for(int t=0;t<7;t++){
				String ccAddr = "tucjiawei@163.com;";
				int index=0;
				while(index<ccCount && (str=ccFile.readLine())!=null){
					ccAddr+=str+";";
					position+=str.length()+crlfSize;
					index++;
				}
				ccAddr = StringUtils.chomp(ccAddr, ";");
				if(!StringUtils.contains(ccAddr,";")){
					break;
				}
				
				mailInfo.setToAddress(ccAddr);
				mailInfo.setSubject("[分享]---又一波潘多拉来袭");
				mailInfo.setContent(content);
				String[] attachments = new String[lineSplit.size()];
				int i=0;
				for(String[] image:lineSplit){
					attachments[i++]=contentDir+"/"+StringUtils.trimToEmpty(image[2]);
				}
				mailInfo.setAttachFileNames(Arrays.asList(attachments));
				EmailSend.sendHtmlMail(mailInfo);
				
				FileUtils.write(new File(baseDir+"/position.txt"), position+"");
			}
		}
		
	}
	

}
