package com.taobao.shua;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;


public class HttpClientNew {
	private static Set<Cookie> cookies = new HashSet<Cookie>();
	public static void main(String[] args) throws Exception {
//		  httpclient.getHostConfiguration().setProxy("myproxyhost", 8080);
//		  httpclient.getState().setProxyCredentials("my-proxy-realm", " myproxyhost",
//		  new UsernamePasswordCredentials("my-proxy-username", "my-proxy-password"))
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("Accept-Language", "en-US,en;q=0.8");
		headers.put("Cookie", "ue=tucjiawei@126.com; as=http://www.douban.com/");
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		headers.put("Accept-Encoding", "gzip,deflate,sdch");
		headers.put("Cache-Control", "max-age=0");
		headers.put("Connection", "keep-alive");
		headers.put("Host", "www.douban.com");
		headers.put("Origin", "www.douban.com");
		headers.put("Referer", "www.douban.com");
		headers.put("User-Agent", "Mozilla/5.0");
//				Cookie:ue="tucjiawei@126.com"; as="http://www.douban.com/"
		Map<String,String> params = new HashMap<String, String>();
		params.put("source", "index_nav");
		params.put("form_email", "tucjiawei@126.com");
		params.put("remember", "on");
		params.put("form_password", "sunday&MONDAY");
//		Result result = HttpClientNew.sendPost("https://www.douban.com/accounts/login", headers, params, "utf-8");
//		String cookieLogin = assemblyCookie(result.getCookie());
//		FileUtils.writeStringToFile(new File("d:/cookies.txt"), cookieLogin);
		headers.put("Cookie", FileUtils.readFileToString(new File("d:/cookies.txt")));
		Result result = HttpClientNew.sendGet("http://www.douban.com", headers, null, "UTF-8",false);
		if(result.getStatusCode()==302){
			System.out.println(result.getHeaders().get("Location"));
			String location = result.getHeaders().get("Location").getValue();
			System.out.println(location);
			headers.put("Cookie", assemblyCookie(cookies));
			result = HttpClientNew.sendGet("http://www.douban.com", headers, null, "UTF-8",false);
			if(result.getStatusCode()==302){
				location = result.getHeaders().get("Location").getValue();
				headers.put("Cookie", assemblyCookie(cookies));
				result = HttpClientNew.sendPost(location, headers, params, "UTF-8");
				System.out.println(result.getStatusCode());
			}else{
				System.out.println(result.getContent());
			}
		}else{
			System.out.println(result.getContent());
		}
		
	}
	public static Result sendGet(String url, Map<String, String> headers,  
            Map<String, String> params, String encoding, boolean duan)  
            throws ClientProtocolException, IOException {  
        DefaultHttpClient client = new DefaultHttpClient();  
        HttpHost proxy = new HttpHost("111.1.36.166", 83);
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        // ����в����ľ�ƴװ���� ��Ŀ����  
        url = url + (null == params ? "" : assemblyParameter(params));  
        // ����ʵ����һ��get����  
        HttpGet hp = new HttpGet(url);  
        // �����Ҫͷ������װ����  
        if (null != headers)  
            hp.setHeaders(assemblyHeader(headers));  
        // ��װ���صĲ���  
        Result result = new Result();  
        // ִ������󷵻�һ��HttpResponse  
        HttpResponse response = client.execute(hp);  
        HttpEntity entityRsp = response.getEntity();  
        StringBuffer result1 = new StringBuffer();  
        BufferedReader rd = null;
        if(response.getLastHeader("Content-Encoding")!= null  
                && response.getLastHeader("Content-Encoding").getValue().toLowerCase().indexOf("gzip") > -1){
        	 rd = new BufferedReader(new InputStreamReader(  
        			 new GZIPInputStream(entityRsp.getContent()), encoding));  
        }else{
        	 rd = new BufferedReader(new InputStreamReader(  
             		(entityRsp.getContent()), encoding));  
        }  
        String tempLine = rd.readLine();  
        while (tempLine != null) {  
            //��ȡ�ٶ�token  
            if (tempLine.contains("bdPass.api.params.login_token=")) {  
                // System.out.println(tempLine.substring("bdPass.api.params.login_token=".length()+1,tempLine.length()-2));  
                result.setToken(tempLine.substring(  
                        "bdPass.api.params.login_token=".length() + 1,  
                        tempLine.length() - 2));  
            }  
            result1.append(tempLine);  
            tempLine = rd.readLine();  
        }  
        result.setContent(result1.toString());  
        // ���Ϊtrue��ϵ����get����  
        if (duan)  
            hp.abort();  
        // ����һ��HttpEntity  
        HttpEntity entity = response.getEntity();  
  
        // ���÷��ص�cookie  
        result.setCookie(client.getCookieStore().getCookies()); 
        cookies.addAll(client.getCookieStore().getCookies());
        // ���÷��ص�״̬  
        result.setStatusCode(response.getStatusLine().getStatusCode());  
        // ���÷��ص�ͷ������  
        result.setHeaders(response.getAllHeaders());  
        // ���÷��ص���Ϣ  
        result.setHttpEntity(entity);  
        return result;  
    }
	public static Result sendPost(String url, Map<String, String> headers,  
            Map<String, String> params, String encoding)  
            throws ClientProtocolException, IOException {  
        // ʵ����һ��post����  
		
        HttpPost post = new HttpPost(url);  
        DefaultHttpClient client = new DefaultHttpClient();  
//        HttpHost proxy = new HttpHost("111.1.36.166", 83);
//        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        // ������Ҫ�ύ�Ĳ���  
        List<NameValuePair> list = new ArrayList<NameValuePair>();  
        if (params != null) {  
            for (String temp : params.keySet()) {  
                list.add(new BasicNameValuePair(temp, params.get(temp)));  
            }  
        }  
  
        post.setEntity(new UrlEncodedFormEntity(list, encoding));  
  
        // ����ͷ��  
        if (null != headers)  
            post.setHeaders(assemblyHeader(headers));  
  
        // ʵ�����󲢷���  
        HttpResponse response = client.execute(post);  
        HttpEntity entity = response.getEntity();  
        HttpEntity entityRsp = response.getEntity();  
        StringBuffer result1 = new StringBuffer();  
        BufferedReader rd = null;
        if(response.getLastHeader("Content-Encoding")!= null  
                && response.getLastHeader("Content-Encoding").getValue().toLowerCase().indexOf("gzip") > -1){
        	 rd = new BufferedReader(new InputStreamReader(  
        			 new GZIPInputStream(entityRsp.getContent()), encoding));  
        }else{
        	 rd = new BufferedReader(new InputStreamReader(  
             		(entityRsp.getContent()), encoding));  
        }
       
        String tempLine = rd.readLine();  
        // ��װ���صĲ���  
        Result result = new Result();  
        while (tempLine != null) {  
            //���ػ�ȡ�����ַ  
           /* if (tempLine.contains("encodeURI('")) {  
                // System.out.println(tempLine.substring("bdPass.api.params.login_token=".length()+1,tempLine.length()-2));  
                // result.setToken(tempLine.substring("bdPass.api.params.login_token=".length()+1,tempLine.length()-2));  
                result.setToken(tempLine.substring(  
                        tempLine.indexOf("encodeURI('") + 11,  
                        tempLine.indexOf("');")));  
            } */ 
            result1.append(tempLine);  
            tempLine = rd.readLine();  
        }  
        result.setContent(result1.toString());  
  
        // ���÷���״̬����  
        result.setStatusCode(response.getStatusLine().getStatusCode());  
        // ���÷��ص�ͷ����Ϣ  
        result.setHeaders(response.getAllHeaders());  
        // ���÷��ص�cookie����  
        result.setCookie(client.getCookieStore().getCookies());  
        cookies.addAll(client.getCookieStore().getCookies());
        // ���÷��ص���Ϣ  
        result.setHttpEntity(entity);  
        return result;  
    }  
  
    // ������װͷ��  
    public static Header[] assemblyHeader(Map<String, String> hashMap) {  
        Header[] allHeader = new BasicHeader[hashMap.size()];  
        int i = 0;  
        for (String str : hashMap.keySet()) {  
            Header header = new BasicHeader(str, hashMap.get(str));  
            allHeader[i++] = header;  
            // i++;  
            // i = i +1;s  
        }  
        return allHeader;  
    }  
  
    // ������װcookie  
    public static String assemblyCookie(Collection<Cookie> cookies) {  
        StringBuffer sbu = new StringBuffer();  
        for (Cookie cookie : cookies) {  
            sbu.append(cookie.getName()).append("=").append(cookie.getValue())  
                    .append(";");  
        }  
        if (sbu.length() > 0)  
            sbu.deleteCharAt(sbu.length() - 1);  
        return sbu.toString();  
    }  
  
    // ������װ����  
    public static String assemblyParameter(Map<String, String> parameters) {  
        String para = "?";  
        for (String str : parameters.keySet()) {  
            para += str + "=" + parameters.get(str) + "&";  
        }  
        return para.substring(0, para.length() - 1);  
    } 
}
