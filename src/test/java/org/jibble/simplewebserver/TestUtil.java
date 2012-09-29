package org.jibble.simplewebserver;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Test Utility
 * @author skomatsu
 *
 */
public class TestUtil {

	/**
	 * Creates temporary directory
	 * @param prefix The prefix
	 * @param suffix The suffix
	 * @return The temporary directory created
	 * @throws IOException Exception may occur while deleting/creating the temporary file/directory 
	 */
	public static File createTemporaryDirectory(String prefix, String suffix) throws IOException {
		final File tempFile = File.createTempFile(prefix, suffix);
		
		if (!tempFile.delete()) {
			throw new IllegalStateException("Cannot delete temporary file: " + tempFile.getAbsolutePath());
		}
		
		if (!tempFile.mkdirs()) {
			throw new IllegalStateException("Cannot create temporary directory: " + tempFile.getAbsolutePath());
		}
		
		return tempFile;
	}
	
	/**
	 * Return usable free local port
	 * @return port number
	 * @throws IOException Exception may occur while trying to create the server socket
	 */
	public static int getFreeLocalPort() throws IOException {
		final ServerSocket socket = new ServerSocket(0);
		try {			
			return socket.getLocalPort();
		} finally {
			socket.close();
		}
	}
	
}
