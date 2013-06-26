package com.demo.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

  
/** 
 * 发送请求 
 *  
 * @author Legend、 
 *  
 */  
  
public class SendRequest {  
    // 实例化一个Httpclient的  
//  static DefaultHttpClient client = new DefaultHttpClient();  
    static {  
  
//      // 设置代理  
//      HttpHost proxy = new HttpHost("192.168.13.19", 7777);  
//      client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);  
    }  
  
    // 这是模拟get请求  
    public static Result sendGet(String url, Map<String, String> headers,  
            Map<String, String> params, String encoding, boolean duan)  
            throws ClientProtocolException, IOException {  
        DefaultHttpClient client = new DefaultHttpClient();  
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
        BufferedReader rd = new BufferedReader(new InputStreamReader(  
                entityRsp.getContent(), HTTP.UTF_8));  
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
        System.out.println(result1.toString());  
        // 如果为true则断掉这个get请求  
        if (duan)  
            hp.abort();  
        // 返回一个HttpEntity  
        HttpEntity entity = response.getEntity();  
  
        // 设置返回的cookie  
        result.setCookie(assemblyCookie(client.getCookieStore().getCookies()));  
        // 设置返回的状态  
        result.setStatusCode(response.getStatusLine().getStatusCode());  
        // 设置返回的头部信心  
        result.setHeaders(response.getAllHeaders());  
        // 设置返回的信息  
        result.setHttpEntity(entity);  
        return result;  
    }  
  
    public static Result sendGet(String url, Map<String, String> headers,  
            Map<String, String> params, String encoding)  
            throws ClientProtocolException, IOException {  
        return sendGet(url, headers, params, encoding, false);  
    }  
  
    // 这是模拟post请求  
    public static Result sendPost(String url, Map<String, String> headers,  
            Map<String, String> params, String encoding)  
            throws ClientProtocolException, IOException {  
        // 实例化一个post请求  
        HttpPost post = new HttpPost(url);  
        DefaultHttpClient client = new DefaultHttpClient();  
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
        BufferedReader rd = new BufferedReader(new InputStreamReader(  
                entityRsp.getContent(), HTTP.UTF_8));  
        String tempLine = rd.readLine();  
        // 封装返回的参数  
        Result result = new Result();  
        while (tempLine != null) {  
            //返回获取请求地址  
            if (tempLine.contains("encodeURI('")) {  
                // System.out.println(tempLine.substring("bdPass.api.params.login_token=".length()+1,tempLine.length()-2));  
                // result.setToken(tempLine.substring("bdPass.api.params.login_token=".length()+1,tempLine.length()-2));  
                result.setToken(tempLine.substring(  
                        tempLine.indexOf("encodeURI('") + 11,  
                        tempLine.indexOf("');")));  
            }  
            result1.append(tempLine);  
            tempLine = rd.readLine();  
        }  
        System.out.println(result1.toString());  
  
        // 设置返回状态代码  
        result.setStatusCode(response.getStatusLine().getStatusCode());  
        // 设置返回的头部信息  
        result.setHeaders(response.getAllHeaders());  
        // 设置返回的cookie信心  
        result.setCookie(assemblyCookie(client.getCookieStore().getCookies()));  
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
    public static String assemblyCookie(List<Cookie> cookies) {  
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
    public static void main(String[] args) throws Exception {
    	  Result r1 = SendRequest.sendGet("https://passport.baidu.com/v2/?login", null, null, "GBK");
          //设置参数
          Map<String, String> params = new HashMap<String, String>();
          params.put("class", "login");
          params.put("tpl", "mn");
          params.put("tangram", "true");
          //获取头部信息
          Map<String, Header> headers = r1.getHeaders();
          //设置头部参数
          Map<String, String> headerMap = new HashMap<String, String>();
          for(String str : headers.keySet()){
          headerMap.put(str, headers.get(str).toString().substring(str.length()+2));
          }
          //设置cookie
          headerMap.put("Cookie", r1.getCookie());
          //请求获取token
          Result r = SendRequest.sendGet("https://passport.baidu.com/v2/api/?getapi&class=login&tpl=mn&tangram=true", headerMap, params, "GBK");
          //设置登录参数
          Map<String, String> params2 = new HashMap<String, String>();
          params2.put("class", "login");
          params2.put("tpl", "mn");
          params2.put("tangram", "true");
          params2.put("username", "账号");
          params2.put("password", "密码");
          
          params2.put("token", r.getToken());
          params2.put("isPhone", "false");
          params2.put("loginType", "1");
          params2.put("verifycode", "");
          
          params2.put("callback", "parent.bdPass.api.login._postCallback");
           
          //设置cookie   只需设置cookie  设置其他header参数会导致报错
          Map<String, String> headerMap2 = new HashMap<String, String>();
          headerMap2.put("Cookie", r.getCookie());
          //post登录请求
          Result result = SendRequest.sendPost("https://passport.baidu.com/v2/api/?login", headerMap2, params2, "UTF-8");
          //判断是否登录成功
          Map<String, String> headerMap3 = new HashMap<String, String>();
          //一定要设置cookie  否则会未登录
          headerMap3.put("Cookie", result.getCookie());
          //请求百度首页
          SendRequest.sendGet("http://www.baidu.com", headerMap3, null, "UTF-8");
	}
} 