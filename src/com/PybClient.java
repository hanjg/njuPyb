package com;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/**
 *
 * @author hjg
 *
 */
public class PybClient extends MyHttpClient{ 
	private String accout;
	private String password;
	
	public PybClient() {
		this.accout = Config.ACCOUNT;  
        this.password = Config.PASSWORD;
    }  
  
    
    public void login() {
    	if (!deserializeCookieStore(Config.COOKIE_PATH)) {
    		firstlogin(getCloseableHttpClient(),getHttpclientContext());
    	}
    }
    
    /**
     * 下线之后序列化的cookie无效，需要删除cookie并且重新登陆
     */
    public void logout(){
    	HttpGet httpGet=new HttpGet(Config.LOGOUT_URL);
    	HttpResponse response=getHttpResponse(httpGet);
    	showResponse(response);
    	try {
			Files.delete(Paths.get(Config.COOKIE_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	/**
	 * @throws ParseException
	 * @throws FileNotFoundException 
	 */
	private void firstlogin(CloseableHttpClient client,HttpClientContext context){

		//向登录地址执行post请求
		HttpPost httpPost = new HttpPost(Config.LOGIN_URL);
		setFormData(client, httpPost);
//		showRequest(httpPost);
		HttpResponse response=getHttpResponse(httpPost);
//		showResponse(response);
//		showContext(context);
		
		//向重定向地址执行get请求
		String location=response.getFirstHeader("Location").getValue();
		HttpGet httpGet=new HttpGet(location);
		httpGet.setHeader("Location", location);
//		showRequest(httpGet);
		response=getHttpResponse(httpGet);
//		showResponse(response);
//		showContext(context);
		
		serializeObject(context.getCookieStore(), Config.COOKIE_PATH);
    }

	/**
	 * @param client
	 * @return
	 */
	private void setFormData(CloseableHttpClient client, HttpPost httpPost) {
		String verifyingCode=getVerifyingCode(client, Config.VERIFYING_CODE_PATH);
    
        ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
        postData.add(new BasicNameValuePair("encoded", "true"));
        postData.add(new BasicNameValuePair("goto", "aHR0cDovL3B5Yi5uanUuZWR1LmNuL2xvZ2lucmVkaXJlY3QuYWN0aW9u"));
        postData.add(new BasicNameValuePair("gotoOnFail", "aHR0cDovL3B5Yi5uanUuZWR1LmNuL2xvZ2luLmFjdGlvbg=="));
        postData.add(new BasicNameValuePair("IDToken0", ""));
        postData.add(new BasicNameValuePair("IDButton", "Submit"));
        postData.add(new BasicNameValuePair("IDToken1", accout));
        postData.add(new BasicNameValuePair("IDToken2", password));
        postData.add(new BasicNameValuePair("inputCode", verifyingCode));//验证码code
        postData.add(new BasicNameValuePair("gx_charset", "UTF-8"));
        UrlEncodedFormEntity formEntity=null;
		try {
			formEntity = new UrlEncodedFormEntity(postData);
			httpPost.setEntity(formEntity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void showResponse(HttpResponse response){
		try {
			int statusCode=response.getStatusLine().getStatusCode();
			System.out.println("status:"+statusCode);
			
			Header[] headers=response.getAllHeaders();
			System.out.println("response headers:");
			for(Header header:headers){
				System.out.println(header.toString());
			}
			
			System.out.println("response entities:");
			try {
				System.out.println(EntityUtils.toString(response.getEntity()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("----------------------------------------------------");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}  
	@SuppressWarnings("unused")
	private void showContext(HttpClientContext context){
		System.out.println("context cookies:");
		CookieStore cookieStore=context.getCookieStore();
		if (cookieStore==null) {
			return;
		}
		for(Cookie cookie:cookieStore.getCookies()){
			System.out.println(cookie.toString());
		}
		System.out.println("----------------------------------------------------");
	}
	@SuppressWarnings("unused")
	private void showRequest(HttpRequest request){
		Header[] headers=request.getAllHeaders();
		System.out.println("request headers:");
		for(Header header:headers){
			System.out.println(header.toString());
		}
		System.out.println("----------------------------------------------------");
	}
      
    /**
     * 获取登录的验证码，存储在verifyCode.jsp
     * @param client
     */
    private String getVerifyingCode(CloseableHttpClient client,String url) {  
        HttpGet httpGet = new HttpGet(Config.VERIFYING_CODE_PATH);  
        try(FileOutputStream out=new FileOutputStream(new File("verifyCode.jsp"))){
        	HttpResponse response = client.execute(httpGet);
	        response.getEntity().writeTo(out);
        } catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println("验证码已经存储在verifyCode.jsp中，请输入: ");
        try(Scanner in=new Scanner(System.in)){
        	String code=in.nextLine();
        	return code;
        }
    }
   
    
}
