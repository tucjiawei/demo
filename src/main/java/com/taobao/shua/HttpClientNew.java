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
        // 如果有参数的就拼装起来 三目运算  
        url = url + (null == params ? "" : assemblyParameter(params));  
        // 这是实例化一个get请求  
        HttpGet hp = new HttpGet(url);  
        // 如果需要头部就组装起来  
        if (null != headers)  
            hp.setHeaders(assemblyHeader(headers));  
        // 封装返回的参数  
        Result result = new Result();  
        // 执行请求后返回一个HttpResponse  
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
            //获取百度token  
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
        // 如果为true则断掉这个get请求  
        if (duan)  
            hp.abort();  
        // 返回一个HttpEntity  
        HttpEntity entity = response.getEntity();  
  
        // 设置返回的cookie  
        result.setCookie(client.getCookieStore().getCookies()); 
        cookies.addAll(client.getCookieStore().getCookies());
        // 设置返回的状态  
        result.setStatusCode(response.getStatusLine().getStatusCode());  
        // 设置返回的头部信心  
        result.setHeaders(response.getAllHeaders());  
        // 设置返回的信息  
        result.setHttpEntity(entity);  
        return result;  
    }
	public static Result sendPost(String url, Map<String, String> headers,  
            Map<String, String> params, String encoding)  
            throws ClientProtocolException, IOException {  
        // 实例化一个post请求  
		
        HttpPost post = new HttpPost(url);  
        DefaultHttpClient client = new DefaultHttpClient();  
//        HttpHost proxy = new HttpHost("111.1.36.166", 83);
//        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        // 设置需要提交的参数  
        List<NameValuePair> list = new ArrayList<NameValuePair>();  
        if (params != null) {  
            for (String temp : params.keySet()) {  
                list.add(new BasicNameValuePair(temp, params.get(temp)));  
            }  
        }  
  
        post.setEntity(new UrlEncodedFormEntity(list, encoding));  
  
        // 设置头部  
        if (null != headers)  
            post.setHeaders(assemblyHeader(headers));  
  
        // 实行请求并返回  
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
        // 封装返回的参数  
        Result result = new Result();  
        while (tempLine != null) {  
            //返回获取请求地址  
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
  
        // 设置返回状态代码  
        result.setStatusCode(response.getStatusLine().getStatusCode());  
        // 设置返回的头部信息  
        result.setHeaders(response.getAllHeaders());  
        // 设置返回的cookie信心  
        result.setCookie(client.getCookieStore().getCookies());  
        cookies.addAll(client.getCookieStore().getCookies());
        // 设置返回到信息  
        result.setHttpEntity(entity);  
        return result;  
    }  
  
    // 这是组装头部  
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
  
    // 这是组装cookie  
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
  
    // 这是组装参数  
    public static String assemblyParameter(Map<String, String> parameters) {  
        String para = "?";  
        for (String str : parameters.keySet()) {  
            para += str + "=" + parameters.get(str) + "&";  
        }  
        return para.substring(0, para.length() - 1);  
    } 
}
