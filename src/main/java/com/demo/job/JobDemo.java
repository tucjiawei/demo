package com.demo.job;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
@Component("jobDemo")
public class JobDemo {
	public void execute() {
	}
	public static void main(String[] args) {
		String s = "^[\\w+\\.-]+@[\\w\\.-]+$";
		
		String t = "wangdongya90@sina.com05126821425wj";
		Pattern.compile(s).matcher(t).find();
	}

}
