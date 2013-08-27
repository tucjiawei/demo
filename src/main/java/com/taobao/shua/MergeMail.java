package com.taobao.shua;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class MergeMail {
	public static void main(String[] args) throws Exception {
		/*String newDir = "e:/allmail.txt";
		File newFile = new File(newDir);
		String dir = "E:/taobao/mail/receive";
		File dirFile = new File(dir);
		File[] files = dirFile.listFiles();
		Set<String> allMailSet = new HashSet<String>();
		Set<String> allMailSet2 = new HashSet<String>();
		for(File file:files){
			List<String> lines = FileUtils.readLines(file);
			allMailSet.addAll(lines);
		}
		for(String line:allMailSet){
			allMailSet2.add(StringUtils.trim(line));
		}
		System.out.println(allMailSet2.size());
		FileUtils.writeLines(newFile, allMailSet2, true);*/
		mergeMail();
	}
	public static void mergeMail() throws Exception{
		String s = "^[\\w+\\.-]+@[\\w\\.-]+\\.\\w{2,4}$";
		
		String newDir = "E:/taobao/mail/allMail.txt";
		File newFile = new File(newDir);
		String dir = "E:/taobao/mail/xh-2.txt";
		File dirFile = new File(dir);
//		List<String> lines = FileUtils.readLines(dirFile);
//		System.out.println(lines.size());
		Set<String> allMail = new HashSet<String>();
		int i=0;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dirFile)));
		String line = null;
		while((line=br.readLine())!=null){
			String[] result = line.split("\\s+");
			for(String single:result){
				System.out.println(single);
				Pattern p = Pattern.compile(s);
				Matcher m = p.matcher(StringUtils.trim(single));
				if(single.length()>35){
					continue;
				}
				if(m.find()){
					allMail.add(m.group());
				}
				System.out.println("===");
			}
			System.out.println("ÐÐÊý:"+(i++));
		}
		System.out.println("END");
		FileUtils.writeLines(newFile, allMail, false);
	}
}
