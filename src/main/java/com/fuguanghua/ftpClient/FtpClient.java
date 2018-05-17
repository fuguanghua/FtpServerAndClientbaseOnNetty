package com.fuguanghua.ftpClient;

import static org.apache.commons.net.ftp.FTPReply.isPositiveCompletion;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.nio.charset.Charset;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.fuguanghua.cfgReader.ConfReader;

public class FtpClient {

	public static void main(String[] args) throws NumberFormatException, SocketException, IOException {
		// TODO Auto-generated method stub
		FTPClient client = new FTPClient();
		if(ConfReader.getserverType().equals("active")) {		
		       client.connect(ConfReader.getserverIP(), Integer.parseInt(ConfReader.getserverPort()));
		       if(!client.login("hp", "tangting0417")) {
		    	   System.out.println("login failed");
		       };
		       File file = new File(ConfReader.getfromPath() + "/" + ConfReader.getSfileName());
		       InputStream in = new FileInputStream(file);
		       //assertTrue(isPositiveCompletion(client.getReplyCode()));
		       //client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
		       //client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
	           assertTrue(client.setFileType(FTP.BINARY_FILE_TYPE));
	           //assertEquals("/", client.printWorkingDirectory());
	           //System.out.println(client.printWorkingDirectory());
	           assertTrue(client.changeWorkingDirectory(ConfReader.gettoPath()));
	           System.out.println(client.printWorkingDirectory());
	           assertEquals(ConfReader.gettoPath(), client.printWorkingDirectory());
	           assertEquals(0, client.listFiles().length);
	           assertTrue(client.allocate(42));
	           System.out.println(ConfReader.getfromPath() + "/" + ConfReader.getSfileName());
	           
	           if(!client.storeFile(ConfReader.getRfileName(), in)) {
	        	   System.out.println(client.getReplyCode());
	           };
	           assertTrue(client.rename(ConfReader.getSfileName(), "baz"));
	           assertFalse(client.deleteFile("baz"));
		}
		else {
		       client.connect(ConfReader.getserverIP(), Integer.parseInt(ConfReader.getserverPort()));
		       assertTrue(isPositiveCompletion(client.getReplyCode()));
		       assertTrue(client.setFileType(FTP.BINARY_FILE_TYPE));
		       client.enterLocalPassiveMode();
		       assertEquals("/", client.printWorkingDirectory());
		       assertTrue(client.changeWorkingDirectory("/foo"));
		       assertEquals("/foo", client.printWorkingDirectory());
		       assertEquals(0, client.listFiles("/foo").length);
		       assertTrue(client.allocate(42));
		       assertTrue(client.storeFile("bar", new ByteArrayInputStream("content".getBytes(Charset.forName("UTF-8")))));
		       assertTrue(client.rename("bar", "baz"));
		       assertFalse(client.deleteFile("baz"));
		}
	    assertTrue(client.logout());
	    client.disconnect();
	}
}
