package com.demo.httpclient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {

	private static  final Logger logger = LoggerFactory
			.getLogger(HttpClientUtils.class);

	private HttpContext context = new BasicHttpContext();

	private HttpClient httpClient = new DefaultHttpClient();

	private List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

	private HttpResponse response = null;
	
	private List<Header> headers = new ArrayList<Header>();
	
	private CookieStore cs = new BasicCookieStore();
	
	public HttpClientUtils(){
		context.setAttribute(ClientContext.COOKIE_STORE, cs);
	}
	
	public HttpClientUtils addHeader(String name,String value){
		headers.add(new BasicHeader(name, value));
		return this;
	}
	
	public HttpClientUtils addCookie(String name,String value){
		cs.addCookie(new BasicClientCookie(name, value));
		return this;
	}
	
	public HttpClientUtils setProxy(String host,int port,String protocol){
		HttpHost proxy = new HttpHost(host,port,protocol);
		this.httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		return this;
	}
	
	public HttpClientUtils setSocketTimeout(int timeout){
		this.httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
		return this;
	}
	public HttpClientUtils setConnectionTimeout(int timeout){
		this.httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
		return this;
	}
	public HttpClientUtils send(String url, String encoding) {
		return this.send(url, encoding,HttpMethod.GET);
	}
	public HttpClientUtils send(String url, String encoding,HttpMethod httpMethod) {
		HttpUriRequest request = getMethod(url, encoding, httpMethod);
		try {
			for(Header header:headers){
				request.addHeader(header);
			}
			response = httpClient.execute(request, context);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this;

	}
	private HttpUriRequest getMethod(String url, String encoding,
			HttpMethod httpMethod) {
		HttpUriRequest request = null;
		switch(httpMethod){
		case GET:request = sendGet(url, encoding);break;
		case POST:request = sendPost(url, encoding);break;
		}
		return request;
	}
	private HttpUriRequest sendGet(String url, String encoding) {
		if (!params.isEmpty()) {
			url = url + "?" + URLEncodedUtils.format(params, encoding);
		}
		HttpGet method = new HttpGet(url);
		return method;
	}
	private HttpUriRequest sendPost(String url, String encoding){
		HttpPost httpPost = new HttpPost(url); 
		if(!params.isEmpty()){
			UrlEncodedFormEntity entity=null;
			try {
				entity = new UrlEncodedFormEntity(params,encoding);
				httpPost.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			
		}
		return httpPost;
	}
	public String getContent(){
		try {
			return EntityUtils.toString(this.response.getEntity());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	public String getContent(String encoding){
		try {
			return EntityUtils.toString(this.response.getEntity(),encoding);
		}  catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	public int getStatusCode(){
		return this.response.getStatusLine().getStatusCode();
	}
	public List<Cookie> getCookies(){
		return ((CookieStore)this.context.getAttribute(ClientContext.COOKIE_STORE)).getCookies();
	}
	public HttpClientUtils setParam(Map<String, Object> paramsMap) {
		for (Entry<String, Object> entry : paramsMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof List) {
				for (Object o : (List<?>) value) {
					params.add(new BasicNameValuePair(key, o.toString()));
				}
			} else {
				params.add(new BasicNameValuePair(key, value.toString()));
			}
		}
		return this;
	}
	
	public HttpClientUtils setParam(String key,String value) {
		params.add(new BasicNameValuePair(key, value));
		return this;
	}
	public HttpClientUtils setParam(String key,List<Object> value) {
		for (Object o :value) {
			params.add(new BasicNameValuePair(key, o.toString()));
		}
		return this;
	}
	
	public static enum HttpMethod{
		GET,POST
	}


	public static void main(String[] args) throws Exception {
		HttpClientUtils result = new HttpClientUtils()
//		.setProxy("localhost", 8087, "http")
		.addCookie("bid", "bFVylItI27M")
		.addCookie("ck", "fzPq")
		.addCookie("dbcl2", "41255901:s6rzNs7KFKU")
		.send("http://sourceforge.net/", "UTF-8",HttpMethod.POST);
		System.out.println(result.getContent());
		System.out.println(result.getCookies());
	}

}
