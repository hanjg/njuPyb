package com;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class MyHttpClient {
	private CloseableHttpClient client;
	private HttpClientContext context;
	private HttpResponse response;
	
	public MyHttpClient() {
		client=getMyHttpClient();
		context=getMyHttpClientContext();
		response=null;
	}
	
	/**
	 * 设置httpclient的Cookies策略
	 * @return CloseableHttpClient
	 */
	private static CloseableHttpClient getMyHttpClient(){
		RequestConfig globalConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.STANDARD)
				.build();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultRequestConfig(globalConfig)
				.setDefaultCookieStore(new BasicCookieStore())
				.build();
		return httpClient;
	}
	/**
	 * 设置httpclient的上下文
	 * @return
	 */
	private static HttpClientContext getMyHttpClientContext(){
		return HttpClientContext.create();
	}
	public CloseableHttpClient getCloseableHttpClient(){
		return client;
	}
	public HttpClientContext getHttpclientContext(){
		return context;
	}
	public HttpResponse getHttpResponse(HttpRequestBase request){
		getResponse(request);
		return response;
	}
	private HttpResponse getResponse(HttpUriRequest request){
		try {
			response=client.execute(request, context);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 反序列化CookiesStore
	 * @return 是否序列化成功
	 */
	public boolean deserializeCookieStore(String path){
		try {
			CookieStore cookieStore = (CookieStore)deserializeObject(path);
			context.setCookieStore(cookieStore);
		} catch (IOException e) {
			System.out.println("cookie文件不存在");
			return false;
		}
		return true;
	}

	/**
	 * 序列化对象
	 * @param object 序列化对象
	 * @param filePath 序列化后的存储路径
	 */
	public void serializeObject(Object object,String filePath){
		try(ObjectOutputStream out=new ObjectOutputStream(
				new FileOutputStream(filePath))){
			out.writeObject(object);
			out.flush();
			System.out.println("序列化成功");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 反序列化对象
	 * @param filePath 对象的存储路径
	 * @return 反序列化的对象
	 * @throws IOException
	 */
	private Object deserializeObject(String filePath) throws IOException {
		Object object=null;
		try(ObjectInputStream in=new ObjectInputStream(new FileInputStream(filePath))){
			object=in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		return object;
	}
}
