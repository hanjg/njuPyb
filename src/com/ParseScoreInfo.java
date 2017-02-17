package com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseScoreInfo {
	public static List<Course> parse(String page){
		List<Course> list=new ArrayList<>();
		Document document=Jsoup.parse(page);
		Elements tableBody=document.select("body");
		Elements rows=tableBody.select("tr[class!=title_tr]");
		for(Element row:rows){
			Elements rowData=row.select("td");
			Course course=new Course();
			course.setId(Integer.parseInt(rowData.get(0).text()));
			course.setYear(rowData.get(1).text());
			course.setTerm(rowData.get(2).text());
			course.setName(rowData.get(3).text());
			course.setCredit(rowData.get(6).text());
			course.setNature(rowData.get(7).text());
			course.setGrade(rowData.get(8).text());
			list.add(course);
		}
		return list;
	}
	public static void main(String[] args) {
		StringBuilder builder=new StringBuilder();
		try(Scanner in=new Scanner(new FileInputStream("temp.txt"))){
			while(in.hasNextLine()){
				builder.append(in.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(parse(builder.toString()));
	}
}
