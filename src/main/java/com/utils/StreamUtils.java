package com.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.management.BadBinaryOpValueExpException;

import org.json.JSONObject;
import org.json.XML;

public class StreamUtils {

	public static String readStreamToStr(InputStream is) {
		
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			return br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	public static JSONObject readXmlInfo(String filePath) {
		try {
			Path path = Paths.get(filePath);
			FileInputStream is = new FileInputStream(path.toFile());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				builder.append(line);
			}
			return XML.toJSONObject(builder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
