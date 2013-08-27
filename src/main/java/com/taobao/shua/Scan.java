package com.taobao.shua;

import com.demo.httpclient.HttpClientUtils;

public class Scan {
	public static void main(String[] args) {
		new HttpClientUtils().send("http://item.taobao.com/item.htm?id=21078291107","utf-8");
	}

}
