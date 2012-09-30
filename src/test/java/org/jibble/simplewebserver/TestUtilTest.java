/**
Copyright KOMATSU Seiji (comutt), 2012, https://github.com/comutt/SimpleWebServer-mod

This software is modification of the original from http://www.jibble.org/miniwebserver/,
and licensed under the GNU General Public License (GPL) from the Free Software Foundation, http://www.fsf.org/.

Thanks to Paul James Mutton, the author of the original.

*/

package org.jibble.simplewebserver;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.ServerSocket;

import org.junit.Test;


/**
 * Test class for {@link TestUtil}
 * @author skomatsu
 *
 */
public class TestUtilTest {

	/**
	 * Test for {@link TestUtil#createTemporaryDirectory(String, String)}
	 */
	@Test
	public void testCreateTemporaryDirectory() throws Exception {
		final File tempDirectory = TestUtil.createTemporaryDirectory("testprefix", "testsuffix");
		try {
			assertThat(tempDirectory.getName().startsWith("testprefix"), is(true));
			assertThat(tempDirectory.getName().endsWith("testsuffix"), is(true));
			assertThat(tempDirectory.exists(), is(true));
			assertThat(tempDirectory.isDirectory(), is(true));
		} finally {
			tempDirectory.delete();
		}
	}

	/**
	 * Test for {@link TestUtil#getFreeLocalPort()}
	 */
	@Test
	public void testGetFreeLocalPort() throws Exception {
		final int freeLocalPort = TestUtil.getFreeLocalPort();
		assertThat(freeLocalPort, greaterThan(0));

		final ServerSocket socket = new ServerSocket(freeLocalPort);
		try {
			assertThat(socket, notNullValue());
		} finally {
			socket.close();
		}
	}

}
