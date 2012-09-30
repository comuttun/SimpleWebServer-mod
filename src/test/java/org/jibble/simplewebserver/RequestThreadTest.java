package org.jibble.simplewebserver;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.Socket;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@link RequestThread}
 * @author skomatsu
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestThreadTest {

	@Mock
	private Socket socket;

	@Before
	public void setUp() throws Exception {

	}

	/**
	 * Test for {@link RequestThread#run()}
	 */
	@Test
	public void testRun_NotGET() throws Exception {
		final StringBuilder inputStreamBuilder = new StringBuilder();
		inputStreamBuilder.append("POST / HTTP/1.0\n");

		final ByteArrayInputStream socketInputStream =
				new ByteArrayInputStream(
						inputStreamBuilder.toString().getBytes());
		final ByteArrayOutputStream socketOutputStream =
				new ByteArrayOutputStream();

		stub(socket.getInputStream()).toReturn(socketInputStream);
		stub(socket.getOutputStream()).toReturn(socketOutputStream);

		final File rootDir = TestUtil.createTemporaryDirectory("testRun", "NotGET");
		final RequestThread thread = new RequestThread(socket, rootDir);

		thread.run();

		verify(socket).setSoTimeout(anyInt());
		verify(socket).getInputStream();
		verify(socket).getOutputStream();
		verifyNoMoreInteractions(socket);

		final Scanner outputStreamScanner= new Scanner(socketOutputStream.toString());
		assertThat(outputStreamScanner.nextLine(), startsWith("HTTP/1.0 500"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Date: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Server: "));
		assertThat(outputStreamScanner.nextLine(), equalTo("Content-Type: text/html"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Expires: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Content-Length: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Last-modified: "));
		assertThat(outputStreamScanner.nextLine(), equalTo(""));
	}

	@Test
	public void testRequestThread() {
		fail("Not yet implemented");
	}

}
