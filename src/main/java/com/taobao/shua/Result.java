package com.taobao.shua;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.cookie.Cookie;

public class Result {

	private List<Cookie> cookie;
	private int statusCode;
	private Header[] headers;
	private HttpEntity httpEntity;
	private String token;
	private String content;
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public Map<String, Header> getHeaders() {
		Map<String,Header> result = new HashMap<String, Header>();
		for(Header header:headers){
			result.put(header.getName(), header);
		}
		return result;
	}
	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public HttpEntity getHttpEntity() {
		return httpEntity;
	}
	public void setHttpEntity(HttpEntity httpEntity) {
		this.httpEntity = httpEntity;
	}
	public List<Cookie> getCookie() {
		return cookie;
	}
	public void setCookie(List<Cookie> cookie) {
		this.cookie = cookie;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
