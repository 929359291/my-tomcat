package com.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.annotation.MyServletURL;


public class MyServletImpl implements MyServlet {
	
	@MyServletURL(value = "MyServlet")
	public String doPost() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

//		return "My servlet of project1 is running at " + df.format(new Date());
		return "My servlet of project2 is running at " + df.format(new Date());
	}
}