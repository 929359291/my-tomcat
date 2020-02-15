package com.socket;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;


public class UrlTest {

	public static void main(String[] args) throws Exception {
		URL url = new URL("file:D:/eclipse_workspace/MyTomcat/Project1/WEB-INF/classes/");
		URLClassLoader loader = new URLClassLoader(new URL[] { url });
		Class<?> c = loader.loadClass("edu.encu.MyServletImpl");
		Method method = c.getMethod("doPost");
		Object res = method.invoke(c.newInstance());
		System.out.println("end---------------" + System.getProperty("line.separator") + res);
		
		ClassPath classPath = ClassPath.from(c.getClassLoader());
		ImmutableSet<ClassInfo> allClasses = classPath.getAllClasses();
		for (ClassInfo classInfo : allClasses) {
			System.out.println(classInfo.getName());
		}
		
		
		
	}
}
