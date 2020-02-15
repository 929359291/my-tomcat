package com.socket;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.utils.StreamUtils;

public class TcpClient {
	
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("127.0.0.1", 8080);
		OutputStream os = socket.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeBytes("Project1/MyServlet" + System.getProperty("line.separator"));
		dos.flush();
		InputStream is = socket.getInputStream();
		String res = StreamUtils.readStreamToStr(is);
		System.out.println("doPost response = " + res);
		is.close();
		dos.close();
	}

}
