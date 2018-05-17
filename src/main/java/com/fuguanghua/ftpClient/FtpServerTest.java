package com.fuguanghua.ftpClient;

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static org.apache.commons.net.ftp.FTPReply.isPositiveCompletion;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: alexkasko
 * Date: 12/28/12
 */
public class FtpServerTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        FTPClient client = new FTPClient();
        // active
        client.connect("127.0.0.1", 2121);
        //assertTrue(isPositiveCompletion(client.getReplyCode()));
        assertTrue(client.setFileType(FTP.BINARY_FILE_TYPE));
        //assertEquals("/", client.printWorkingDirectory());
        //assertTrue(client.changeWorkingDirectory("/foo"));
        //assertEquals("/foo", client.printWorkingDirectory());
        //assertEquals(0, client.listFiles("/foo").length);
        //assertTrue(client.allocate(42));
        assertTrue(client.storeFile("bar", new ByteArrayInputStream("content".getBytes(Charset.forName("UTF-8")))));
        //assertTrue(client.rename("bar", "baz"));
        //assertFalse(client.deleteFile("baz"));
        //assertTrue(client.logout());
        //client.disconnect();
        // passive
        /*client.connect("127.0.0.1", 2121);
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
        client.disconnect();*/
    }
}
