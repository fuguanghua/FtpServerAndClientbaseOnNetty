package com.fuguanghua.netty.ftp;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation should read all required data from provided FTP file-upload stream,
 * stream will be closed immediately after {@link #receive(String, java.io.InputStream)} call
 * @author fuguanghua
 * Date: 2018/05/10
 */
public interface DataReceiver {
	/**
	 * Implementation should read provided FTP file-upload data
	 * @param directory current directory set by client
     * @param name name of uploaded file
     * @param data uploaded file stream
     * @throws IOException on IO error
	 */
	void receive(String directory, String name, InputStream data) throws IOException;
}
