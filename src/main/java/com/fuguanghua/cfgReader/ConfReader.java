package com.fuguanghua.cfgReader;

import java.io.FileInputStream;
import java.io.InputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class ConfReader {
	static Document document;
	static Element rootElement;
	private static String serverType = "";
	private static String serverIP = "";
	private static String serverPort = "";
	private static String fromPath = "";
	private static String toPath = "";
	private static String SfileName = "";
	private static String RfileName = "";
	private static String username = "";
	private static String passwd = "";
	private static String scheduleTimer = "";
	
	static {
		try {
			SAXBuilder builder = new SAXBuilder();
			InputStream is = new FileInputStream("conf/esim.xml");
			document = builder.build(is);
			rootElement = document.getRootElement();
			serverType = rootElement.getChildText("serverType");
			serverIP = rootElement.getChildText("serverIP");
			serverPort = rootElement.getChildText("serverPort");
			fromPath = rootElement.getChildText("fromPath");
			toPath = rootElement.getChildText("toPath");
			SfileName = rootElement.getChildText("sfilename");
			RfileName = rootElement.getChildText("rfilename");
			username = rootElement.getChildText("username");
			passwd = rootElement.getChildText("passwd");
			scheduleTimer = rootElement.getChildText("scheduletimer");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getserverType() {
		return serverType;
	}
	
	public static String getserverIP() {
		return serverIP;
	}
	
	public static String getserverPort() {
		return serverPort;
	}
	
	public static String getfromPath() {
		return fromPath;
	}
	
	public static String gettoPath() {
		return toPath;
	}
	
	public static String getSfileName() {
		return SfileName;
	}
	
	public static String getRfileName() {
		return RfileName;
	}
	
	public static String getUserName() {
		return username;
	}
	
	public static String getPasswd() {
		return passwd;
	}
	
	public static String getscheduleTimer() {
		return scheduleTimer;
	}
}
