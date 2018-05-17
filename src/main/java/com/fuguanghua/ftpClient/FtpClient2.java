package com.fuguanghua.ftpClient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.fuguanghua.cfgReader.ConfReader;

public class FtpClient2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final File localfile = new File(ConfReader.getfromPath() + "/" + ConfReader.getSfileName());
		final String server = ConfReader.getserverIP();
		final String port = ConfReader.getserverPort();
		final String username = ConfReader.getUserName();
		final String password = ConfReader.getPasswd();
		//if(!upload(server, port, username, password, localfile )) {
		//	System.out.println("upload file failed");
		//};
		Runnable runnable = new Runnable() {
			public void run() {
				upload(server, port, username, password, localfile);
			}
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 10, Integer.parseInt(ConfReader.getscheduleTimer()), TimeUnit.SECONDS);
		//service.schedule(runnable, 0, TimeUnit.MILLISECONDS);//首次立即执行
	}

	public static boolean upload(String server, String port, String username,String password,File localfile ){
        boolean Store=false;
        try{
        FTPClient ftp = new FTPClient();
               // ftp.connect(server);
    /* you can use either code which is written above above or below code as ftp port 20 is used for the data transfer and port 21 is used for command and controlls */
           ftp.connect(InetAddress.getByName(server), Integer.valueOf(port)); 
    //here 'server' is your domain name of ftp server or url
                if(!ftp.login(username, password))
                {
                	System.out.println("login failed");
                    ftp.logout();
                    return false;
                }
            ftp.sendNoOp();//used so server timeout exception will not rise
                int reply = ftp.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply))
                {
                	System.out.println("No reply...");
                    ftp.disconnect();
                    return false;
                }
              ftp.enterLocalPassiveMode(); /* just include this line here and your code will work fine */
              InputStream in = new FileInputStream(localfile);
              // ftp.setFileType(ftp.BINARY_FILE_TYPE, ftp.BINARY_FILE_TYPE);
               ftp.setFileType(FTP.BINARY_FILE_TYPE);
               // ftp.setFileTransferMode(ftp.BINARY_FILE_TYPE);
	           //assertEquals("/", ftp.printWorkingDirectory());
	           //System.out.println(client.printWorkingDirectory());
	           assertTrue(ftp.changeWorkingDirectory(ConfReader.gettoPath()));
	           //System.out.println(ftp.printWorkingDirectory());
	           //assertEquals(ConfReader.gettoPath(), ftp.printWorkingDirectory());
               Store = ftp.storeFile(ConfReader.getRfileName(), in);
               System.out.println(ftp.getReplyCode());
               System.out.println(ftp.getReplyString());
               in.close();
             //ftp.disconnect();
     //here logout will close the connection for you
               ftp.logout();

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return false;
            }
        return Store;
    }
}
