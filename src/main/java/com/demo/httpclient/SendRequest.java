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
 * �������� 
 *  
 * @author Legend�� 
 *  
 */  
  
public class SendRequest {  
    // ʵ����һ��Httpclient��  
//  static DefaultHttpClient client = new DefaultHttpClient();  
    static {  
  
//      // ���ô���  
//      HttpHost proxy = new HttpHost("192.168.13.19", 7777);  
//      client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);  
    }  
  
    // ����ģ��get����  
    public static Result sendGet(String url, Map<String, String> headers,  
            Map<String, String> params, String encoding, boolean duan)  
            throws ClientProtocolException, IOException {  
        DefaultHttpClient client = new DefaultHttpClient();  
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
        BufferedReader rd = new BufferedReader(new InputStreamReader(  
                entityRsp.getContent(), HTTP.UTF_8));  
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
        System.out.println(result1.toString());  
        // ���Ϊtrue��ϵ����get����  
        if (duan)  
            hp.abort();  
        // ����һ��HttpEntity  
        HttpEntity entity = response.getEntity();  
  
        // ���÷��ص�cookie  
        result.setCookie(assemblyCookie(client.getCookieStore().getCookies()));  
        // ���÷��ص�״̬  
        result.setStatusCode(response.getStatusLine().getStatusCode());  
        // ���÷��ص�ͷ������  
        result.setHeaders(response.getAllHeaders());  
        // ���÷��ص���Ϣ  
        result.setHttpEntity(entity);  
        return result;  
    }  
  
    public static Result sendGet(String url, Map<String, String> headers,  
            Map<String, String> params, String encoding)  
            throws ClientProtocolException, IOException {  
        return sendGet(url, headers, params, encoding, false);  
    }  
  
    // ����ģ��post����  
    public static Result sendPost(String url, Map<String, String> headers,  
            Map<String, String> params, String encoding)  
            throws ClientProtocolException, IOException {  
        // ʵ����һ��post����  
        HttpPost post = new HttpPost(url);  
        DefaultHttpClient client = new DefaultHttpClient();  
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
        BufferedReader rd = new BufferedReader(new InputStreamReader(  
                entityRsp.getContent(), HTTP.UTF_8));  
        String tempLine = rd.readLine();  
        // ��װ���صĲ���  
        Result result = new Result();  
        while (tempLine != null) {  
            //���ػ�ȡ�����ַ  
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
  
        // ���÷���״̬����  
        result.setStatusCode(response.getStatusLine().getStatusCode());  
        // ���÷��ص�ͷ����Ϣ  
        result.setHeaders(response.getAllHeaders());  
        // ���÷��ص�cookie����  
        result.setCookie(assemblyCookie(client.getCookieStore().getCookies()));  
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
  
    // ������װ����  
    public static String assemblyParameter(Map<String, String> parameters) {  
        String para = "?";  
        for (String str : parameters.keySet()) {  
            para += str + "=" + parameters.get(str) + "&";  
        }  
        return para.substring(0, para.length() - 1);  
    } 
    public static void main(String[] args) throws Exception {
    	  Result r1 = SendRequest.sendGet("https://passport.baidu.com/v2/?login", null, null, "GBK");
          //���ò���
          Map<String, String> params = new HashMap<String, String>();
          params.put("class", "login");
          params.put("tpl", "mn");
          params.put("tangram", "true");
          //��ȡͷ����Ϣ
          Map<String, Header> headers = r1.getHeaders();
          //����ͷ������
          Map<String, String> headerMap = new HashMap<String, String>();
          for(String str : headers.keySet()){
          headerMap.put(str, headers.get(str).toString().substring(str.length()+2));
          }
          //����cookie
          headerMap.put("Cookie", r1.getCookie());
          //�����ȡtoken
          Result r = SendRequest.sendGet("https://passport.baidu.com/v2/api/?getapi&class=login&tpl=mn&tangram=true", headerMap, params, "GBK");
          //���õ�¼����
          Map<String, String> params2 = new HashMap<String, String>();
          params2.put("class", "login");
          params2.put("tpl", "mn");
          params2.put("tangram", "true");
          params2.put("username", "�˺�");
          params2.put("password", "����");
          
          params2.put("token", r.getToken());
          params2.put("isPhone", "false");
          params2.put("loginType", "1");
          params2.put("verifycode", "");
          
          params2.put("callback", "parent.bdPass.api.login._postCallback");
           
          //����cookie   ֻ������cookie  ��������header�����ᵼ�±���
          Map<String, String> headerMap2 = new HashMap<String, String>();
          headerMap2.put("Cookie", r.getCookie());
          //post��¼����
          Result result = SendRequest.sendPost("https://passport.baidu.com/v2/api/?login", headerMap2, params2, "UTF-8");
          //�ж��Ƿ��¼�ɹ�
          Map<String, String> headerMap3 = new HashMap<String, String>();
          //һ��Ҫ����cookie  �����δ��¼
          headerMap3.put("Cookie", result.getCookie());
          //����ٶ���ҳ
          SendRequest.sendGet("http://www.baidu.com", headerMap3, null, "UTF-8");
	}
} 