package com;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

public class Main {

	public static void main(String[] args) {
		PybClient pybClient=new PybClient();
		pybClient.login();
		HttpResponse response=pybClient.getHttpResponse(new HttpGet(Config.SCORE_URL));
		try {
			List<Course> list=ParseScoreInfo.parse(EntityUtils.toString(response.getEntity()));
			for(Course course:list){
				System.out.println(course);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
