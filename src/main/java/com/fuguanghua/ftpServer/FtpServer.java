package com.fuguanghua.ftpServer;

import org.apache.commons.io.IOUtils;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import com.fuguanghua.cfgReader.ConfReader;
import com.fuguanghua.netty.ftp.CrlfStringDecoder;
import com.fuguanghua.netty.ftp.DataReceiver;
import com.fuguanghua.netty.ftp.FtpServerHandler;

import static java.util.concurrent.Executors.newCachedThreadPool;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class FtpServer {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChannelFactory factory = new NioServerSocketChannelFactory(newCachedThreadPool(), newCachedThreadPool());
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(new PipelineFactory());
		bootstrap.bind(new InetSocketAddress(Integer.parseInt(ConfReader.getserverPort())));
	}
	
	private static class PipelineFactory implements ChannelPipelineFactory {
        public ChannelPipeline getPipeline() throws Exception {
            ChannelPipeline pipe = Channels.pipeline();
            pipe.addLast("decoder", new CrlfStringDecoder());
            pipe.addLast("executor", new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(1, 0, 0)));
            pipe.addLast("handler", new FtpServerHandler(new ConsoleReceiver()));
            return pipe;
        }
	}
	
	private static class ConsoleReceiver implements DataReceiver {
	    public void receive(String directory, String name, InputStream data) throws IOException {
	        //System.out.println("receiving file: [" + name + "] to directory: [" + directory + "]");
	        //System.out.println("receiving data:");
	        //IOUtils.copy(data, System.out);
	        //System.out.println("");
	        writeFile(data);	        
	    }
	}
	
	
    public static void writeFile(InputStream data) {  
        try {
//        	if(directory.substring(directory.length()-1, directory.length()-1).equals("\"))
        	String dataFile = IOUtils.toString(data, "utf-8");
        	System.out.println(dataFile + "oooooooooooooooooooooo");
            FileOutputStream out = new FileOutputStream(ConfReader.gettoPath() + "/" + ConfReader.getRfileName()); // 输出文件路径  
            out.write(dataFile.getBytes());
            out.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
    
    public static String convertStreamToString(InputStream is) {
    	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    	StringBuilder sb = new StringBuilder();
    	String line = null;
    	try {
    		while ((line = reader.readLine()) != null) {
    			sb.append(line + "/n");
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    			} finally {
    				try {
    					is.close();
    					} catch (IOException e) {
    						e.printStackTrace();
    						}
    				}
    	return sb.toString();
    	}   
}
