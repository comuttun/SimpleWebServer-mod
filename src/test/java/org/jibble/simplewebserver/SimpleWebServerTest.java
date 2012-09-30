/**
Copyright KOMATSU Seiji (comutt), 2012, https://github.com/comutt/SimpleWebServer-mod

This software is modification of the original from http://www.jibble.org/miniwebserver/,
and licensed under the GNU General Public License (GPL) from the Free Software Foundation, http://www.fsf.org/.

Thanks to Paul James Mutton, the author of the original.

*/

package org.jibble.simplewebserver;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Logger;

import org.junit.Test;


/**
 * Test class for {@link SimpleWebServer}
 * @author skomatsu
 *
 */
public class SimpleWebServerTest {

	private final static Logger LOG = Logger.getLogger(SimpleWebServerTest.class.getName());

	/**
	 * Test for {@link SimpleWebServer#SimpleWebServer(File, int)}
	 */
	@Test
	public void testRun_IsNotDirectory() throws Exception {
		final File tempFile = File.createTempFile("testRun", "IsNotDirectory");
		tempFile.deleteOnExit();
		Thread serverThread = null;
		try {
			serverThread = new SimpleWebServer(tempFile, 0);
			serverThread.start();
			fail("Never reach here");
		} catch (Exception ex) {
			if (!(ex instanceof IOException)) {
				throw new RuntimeException("Unexpected exception", ex);
			}
		} finally {
			if (serverThread != null) {
				// this case is bug, and server might be started up
				// so terminate it.

				while (serverThread.isAlive()) {
					LOG.severe("Server thread is currently running!");
					serverThread.interrupt();
					LOG.info("Waiting for server thread exit...");
					if (serverThread.isInterrupted()) {
						break;
					} else {
						Thread.sleep(1000);
					}
				}
			}
		}
	}

	/**
	 * Test for {@link SimpleWebServer#SimpleWebServer(File, int)}
	 */
	@Test
	public void testRun_IsDirectory() throws Exception {
		final File tempDirectory = TestUtil.createTemporaryDirectory("testRun", "IsDirectory");
		tempDirectory.deleteOnExit();

		final int freeLocalPort = TestUtil.getFreeLocalPort();

		final SimpleWebServer serverThread = new SimpleWebServer(tempDirectory, freeLocalPort);
		serverThread.start();

		boolean allPassed = false;
		try {
			assertThat(serverThread, notNullValue());
			assertThat(serverThread.isAlive(), is(true));
			assertThat(serverThread.isDaemon(), is(false));
			assertThat(serverThread.isInterrupted(), is(false));

			final Socket clientSocket = new Socket();

			final SocketAddress endpont = new InetSocketAddress("127.0.0.1", freeLocalPort);
			clientSocket.connect(endpont, 5000);
			assertThat(clientSocket.isConnected(), is(true));

			clientSocket.close();
			Thread.sleep(1000);

			assertThat(clientSocket.isClosed(), is(true));

			allPassed = true;
		} finally {
			if (serverThread != null) {
				while (serverThread.isAlive()) {
					LOG.info("Server thread is alive");
					serverThread.interrupt();
					LOG.info("Waiting for server thread exit...");
					if (serverThread.isInterrupted()) {
						break;
					} else {
						Thread.sleep(1000);
					}
				}
			}
		}

		assertThat(allPassed, is(true));
	}

	/**
	 * Test for {@link SimpleWebServer}
	 */
	@Test
	public void testGetExtension() throws Exception {
		final File pngFile = File.createTempFile("testGetExtensions", ".png");
		pngFile.deleteOnExit();
		assertThat(SimpleWebServer.getExtension(pngFile), is(".png"));

		final File jpgFile = File.createTempFile("testGetExtensions", ".JPG");
		jpgFile.deleteOnExit();
		assertThat(SimpleWebServer.getExtension(jpgFile), is(".jpg"));

		final File noExtFile = File.createTempFile("testGetExtensions", "");
		noExtFile.deleteOnExit();
		assertThat(SimpleWebServer.getExtension(noExtFile), is(""));
	}

}
