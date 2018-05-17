package com.fuguanghua.netty.ftp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.junit.Test;

import com.fuguanghua.netty.ftp.CrlfStringDecoder;
import com.fuguanghua.netty.ftp.DataReceiver;
import com.fuguanghua.netty.ftp.FtpServerHandler;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static org.apache.commons.net.ftp.FTPReply.isPositiveCompletion;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 
 * @author fuguanghua
 * Date: 2018/05/14
 *
 */
public class FtpServerTest {

	@Test
	public void test() throws IOException, InterruptedException{
		ChannelFactory factory = new NioServerSocketChannelFactory(newCachedThreadPool(), newCachedThreadPool());
        ServerBootstrap bootstrap = new ServerBootstrap(factory);
        bootstrap.setPipelineFactory(new PipelineFactory());
        bootstrap.bind(new InetSocketAddress(2121));
        FTPClient client = new FTPClient();
        //active
       client.connect("127.0.0.1", 2121);
       assertTrue(isPositiveCompletion(client.getReplyCode()));
       assertTrue(client.setFileType(FTP.BINARY_FILE_TYPE));
       assertEquals("/", client.printWorkingDirectory());
       assertTrue(client.changeWorkingDirectory("/foo"));
       assertEquals("/foo", client.printWorkingDirectory());
       assertEquals(0, client.listFiles().length);
       assertTrue(client.allocate(42));
       assertTrue(client.storeFile("bar", new ByteArrayInputStream("content".getBytes(Charset.forName("UTF-8")))));
       assertTrue(client.rename("bar", "baz"));
       assertFalse(client.deleteFile("baz"));
       assertTrue(client.logout());
       client.disconnect();
       
       //passive
       client.connect("127.0.0.1", 2121);
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
       assertTrue(client.logout());
       client.disconnect();
	}
	
	 // testonly, use proper instantiation in production
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
            System.out.println("receiving file: [" + name + "] to directory: [" + directory + "]");
            System.out.println("receiving data:");
            IOUtils.copy(data, System.out);
            System.out.println("");
        }
    }
}
