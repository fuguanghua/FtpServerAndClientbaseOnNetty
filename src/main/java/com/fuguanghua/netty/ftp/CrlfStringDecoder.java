package com.fuguanghua.netty.ftp;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * {@link FrameDecoder} implementation, that accumulates input strings until {@code \r\n}
 * and sends accumulated string upstream.
 * @author fuguanghua
 * Date: 2018/05/10
 */
public class CrlfStringDecoder extends FrameDecoder{
	private static final byte CR = 13;
	private static final byte LF = 10;
	
	private final int maxRequestLengthBytes;
	private final Charset encoding;
	
    /**
     * Constructor, uses {@code 256} max string length and {@code UTF-8} encoding
     */
	public CrlfStringDecoder() {
		this(1<< 8, "UTF-8");
	}
	
    /**
     * Constructor
     *
     * @param maxRequestLengthBytes max length of accumulated string in bytes
     * @param encoding string encoding to use before sending it upstream
     */
	public CrlfStringDecoder(int maxRequestLengthBytes, String encoding) {
		// TODO Auto-generated constructor stub
		if(maxRequestLengthBytes < 0) throw new IllegalArgumentException("Provided maxRequestLengthBytes: [" + maxRequestLengthBytes +"] must be positive");
		this.maxRequestLengthBytes = maxRequestLengthBytes;
		this.encoding = Charset.forName(encoding);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		// TODO Auto-generated method stub
		byte[] data = new byte[maxRequestLengthBytes];
		int linelength = 0;
		while(true) {
			if(!buffer.readable()) {
				buffer.resetReaderIndex();
				return null;
			}
			byte nextByte = buffer.readByte();
			if(nextByte == CR) {
				nextByte = buffer.readByte();
				if(nextByte == LF) {
					return new String(data, encoding);
				}
			} else if(nextByte == LF) {
				return new String(data, encoding);
			} else {
				if(linelength >= maxRequestLengthBytes) throw new IllegalArgumentException
				("Request size threshold exceeded: [" + maxRequestLengthBytes + "]");
				data[linelength] = nextByte;
				linelength++;
			}
		}
	}

}
