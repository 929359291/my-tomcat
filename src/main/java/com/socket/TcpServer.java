package com.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.annotation.MyServletURL;
import com.proxy.ProxyHandler;
import com.utils.StreamUtils;

import edu.encu.MyServlet;

public class TcpServer {

	static ExecutorService pool = Executors.newCachedThreadPool();
	static String classPath1 = "D:/eclipse_workspace/MyTomcat/Project1/WEB-INF/classes/";
	static String classPath2 = "D:/eclipse_workspace/MyTomcat/Project2/WEB-INF/classes/";

	public static void main(String[] args) throws IOException {

		String xmlFilePath = "D:/eclipse_workspace/MyTomcat/Project1/WEB-INF/web.xml";

		ServerSocket serverSocket = new ServerSocket(8080);
		System.out.println("启动服务器........");
		while (true) {
			Socket accept = serverSocket.accept();
//			pool.execute(new HandlerAccept(accept, xmlFilePath, classPath1));
			pool.execute(new HandlerAccept(accept));
		}
	}

	public static class HandlerAccept implements Runnable {
		private Socket socket;
		private String filePath;
		private String classPath;

		public HandlerAccept() {
			super();
		}
		
		public HandlerAccept(Socket socket) {
			super();
			this.socket = socket;
		}

		public HandlerAccept(Socket socket, String filePath, String classPath) {
			super();
			this.socket = socket;
			this.filePath = filePath;
			this.classPath = classPath;
		}

		@Override
		public void run() {
			try {
				String resp = null;
				String reqMsg = StreamUtils.readStreamToStr(socket.getInputStream());
				System.out.println("client request param = " + reqMsg);
				
				// 用web.xml方式
//				JSONObject jsonObject = StreamUtils.readXmlInfo(filePath);
//				JSONObject servletStr = jsonObject.getJSONObject("Servlets").getJSONObject("Servlet");
//				String reqUrl = reqMsg.split("/")[1];
//				String xmlUrl = servletStr.get("url").toString();
//				if (reqUrl != null && reqUrl.equals(xmlUrl)) {
//					String classPkg = servletStr.get("class").toString();
//					resp = (String) executeClassAndProxy(classPath1, classPkg, "doPost");
//				} else {
//					resp = "not found class info";			
//				}
				
				
				// 用注解方式
				String api = reqMsg.split("/")[1];
				String projectName = reqMsg.split("/")[0];
				String className = "edu.encu.MyServletImpl";
				if ("Project1".equals(projectName)){
					resp = (String) executeClassAndProxyAnnotation(classPath1, className, api);
				} else if ("Project2".equals(projectName)){
					resp = (String) executeClassAndProxyAnnotation(classPath2, className, api);
				} else {
					resp = "not found class info";
				}
				
				OutputStream ops = socket.getOutputStream();
				DataOutputStream dos = new DataOutputStream(ops);
				dos.writeBytes(resp + System.getProperty("line.separator"));
				dos.flush();
				dos.close();
			} catch (IOException | SecurityException e) {
				e.printStackTrace();
			}
		}
		
		
		private Object executeClassAndProxyAnnotation(String classPath, String className, String api){
			Class<?> c;
			Method method;
			String result = "no match annotation value";
			try {
				URL url = new URL("file:" + classPath);
				System.out.println(url);
				URLClassLoader loader = new URLClassLoader(new URL[]{url});
				c = loader.loadClass(className);
				
				for (Method m : c.getMethods()) {
					if (m.isAnnotationPresent(MyServletURL.class)) {
						System.out.println("method name = " + m.getName());
						MyServletURL msu = m.getAnnotation(MyServletURL.class);
						System.out.println("annotation value = " + msu.value());
						if (msu.value().equals(api)) {
							ProxyHandler handler = new ProxyHandler(c.newInstance());
							MyServlet proxyMyServlet = (MyServlet) Proxy.newProxyInstance(c.getClassLoader(), c.getInterfaces(), handler);
							return proxyMyServlet.doPost();
						}
					}
				}
				return result;
			} catch (MalformedURLException | ClassNotFoundException | SecurityException | IllegalAccessException | IllegalArgumentException | InstantiationException e) {
				throw new RuntimeException(e);
			}
		}
		
		private Object executeClassAndProxy(String classPath, String className){
			Class<?> c;
			Method method;
			String result;
			try {
				URL url = new URL("file:" + classPath);
				System.out.println(url.toString());
				URLClassLoader loader = new URLClassLoader(new URL[]{url});
				c = loader.loadClass(className);
				ProxyHandler handler = new ProxyHandler(c.newInstance());
				MyServlet proxyMyServlet = (MyServlet) Proxy.newProxyInstance(c.getClassLoader(), c.getInterfaces(), handler);
				return proxyMyServlet.doPost();
			} catch (MalformedURLException | ClassNotFoundException | SecurityException | IllegalAccessException | IllegalArgumentException | InstantiationException e) {
				throw new RuntimeException(e);
			}
		}
		
		private Object executeClass(String classPath, String className, String methodName){
			Class<?> c;
			Method method;
			String result;
			try {
				URL url = new URL("file:" + classPath);
				System.out.println(url.toString());
				URLClassLoader loader = new URLClassLoader(new URL[]{url});
				c = loader.loadClass(className);
				method = c.getMethod(methodName);
				return method.invoke(c.newInstance());
			} catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
