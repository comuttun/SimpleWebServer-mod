package org.jibble.simplewebserver;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
	public void testRun_EmptyRequest() throws Exception {
		final ByteArrayInputStream socketInputStream =
				new ByteArrayInputStream("".getBytes());
		final ByteArrayOutputStream socketOutputStream =
				new ByteArrayOutputStream();

		stub(socket.getInputStream()).toReturn(socketInputStream);
		stub(socket.getOutputStream()).toReturn(socketOutputStream);

		final File rootDir = TestUtil.createTemporaryDirectory("testRun", "");
		rootDir.deleteOnExit();
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

		final File rootDir = TestUtil.createTemporaryDirectory("testRun", "");
		rootDir.deleteOnExit();
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

	/**
	 * Test for {@link RequestThread#run()}
	 */
	@Test
	public void testRun_InvalidHTTPVersion() throws Exception {
		final StringBuilder inputStreamBuilder = new StringBuilder();
		inputStreamBuilder.append("GET / HTTP/0.9\n");

		final ByteArrayInputStream socketInputStream =
				new ByteArrayInputStream(
						inputStreamBuilder.toString().getBytes());
		final ByteArrayOutputStream socketOutputStream =
				new ByteArrayOutputStream();

		stub(socket.getInputStream()).toReturn(socketInputStream);
		stub(socket.getOutputStream()).toReturn(socketOutputStream);

		final File rootDir = TestUtil.createTemporaryDirectory("testRun", "");
		rootDir.deleteOnExit();

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

	/**
	 * Test for {@link RequestThread#run()}
	 */
	@Test
	public void testRun_GETRoot_ExistsIndex() throws Exception {
		final StringBuilder inputStreamBuilder = new StringBuilder();
		inputStreamBuilder.append("GET / HTTP/1.0\n");

		final ByteArrayInputStream socketInputStream =
				new ByteArrayInputStream(
						inputStreamBuilder.toString().getBytes());
		final ByteArrayOutputStream socketOutputStream =
				new ByteArrayOutputStream();

		stub(socket.getInputStream()).toReturn(socketInputStream);
		stub(socket.getOutputStream()).toReturn(socketOutputStream);

		final File rootDir = TestUtil.createTemporaryDirectory("testRun", "");

		final File indexFile = new File(rootDir.getCanonicalFile(), "index.html");
		indexFile.createNewFile();
		indexFile.deleteOnExit();

		final PrintWriter indexFileWriter = new PrintWriter(indexFile);
		indexFileWriter.println("index contents");
		indexFileWriter.flush();
		indexFileWriter.close();

		final RequestThread thread = new RequestThread(socket, rootDir);

		thread.run();

		verify(socket).setSoTimeout(anyInt());
		verify(socket).getInputStream();
		verify(socket).getOutputStream();
		verifyNoMoreInteractions(socket);

		final Scanner outputStreamScanner= new Scanner(socketOutputStream.toString());
		assertThat(outputStreamScanner.nextLine(), startsWith("HTTP/1.0 200"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Date: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Server: "));
		assertThat(outputStreamScanner.nextLine(), equalTo("Content-Type: text/html"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Expires: "));
		assertThat(outputStreamScanner.nextLine(), equalTo("Content-Length: " + indexFile.length()));
		assertThat(outputStreamScanner.nextLine(), startsWith("Last-modified: "));
		assertThat(outputStreamScanner.nextLine(), equalTo(""));
		assertThat(outputStreamScanner.nextLine(), equalTo("index contents"));
	}

	/**
	 * Test for {@link RequestThread#run()}
	 */
	@Test
	public void testRun_GETRoot_NotExistsIndex() throws Exception {
		final StringBuilder inputStreamBuilder = new StringBuilder();
		inputStreamBuilder.append("GET / HTTP/1.1\n");

		final ByteArrayInputStream socketInputStream =
				new ByteArrayInputStream(
						inputStreamBuilder.toString().getBytes());
		final ByteArrayOutputStream socketOutputStream =
				new ByteArrayOutputStream();

		stub(socket.getInputStream()).toReturn(socketInputStream);
		stub(socket.getOutputStream()).toReturn(socketOutputStream);

		final File rootDir = TestUtil.createTemporaryDirectory("testRun", "");
		final RequestThread thread = new RequestThread(socket, rootDir);

		thread.run();

		verify(socket).setSoTimeout(anyInt());
		verify(socket).getInputStream();
		verify(socket).getOutputStream();
		verifyNoMoreInteractions(socket);

		final Scanner outputStreamScanner= new Scanner(socketOutputStream.toString());
		assertThat(outputStreamScanner.nextLine(), startsWith("HTTP/1.0 200"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Date: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Server: "));
		assertThat(outputStreamScanner.nextLine(), equalTo("Content-Type: text/html"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Expires: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Last-modified: "));
		assertThat(outputStreamScanner.nextLine(), equalTo(""));
		final String title = "Index of /";
		assertThat(outputStreamScanner.nextLine(), startsWith("<html><head><title>" + title + "</title></head>"));
	}

	/**
	 * Test for {@link RequestThread#run()}
	 */
	@Test
	public void testRun_GETHtml() throws Exception {
		final StringBuilder inputStreamBuilder = new StringBuilder();
		inputStreamBuilder.append("GET /test.html HTTP/1.0\n");

		final ByteArrayInputStream socketInputStream =
				new ByteArrayInputStream(
						inputStreamBuilder.toString().getBytes());
		final ByteArrayOutputStream socketOutputStream =
				new ByteArrayOutputStream();

		stub(socket.getInputStream()).toReturn(socketInputStream);
		stub(socket.getOutputStream()).toReturn(socketOutputStream);

		final File rootDir = TestUtil.createTemporaryDirectory("testRun", "");

		final File htmlFile = new File(rootDir.getCanonicalFile(), "test.html");
		htmlFile.createNewFile();
		htmlFile.deleteOnExit();

		final PrintWriter htmlFileWriter = new PrintWriter(htmlFile);
		htmlFileWriter.println("test contents");
		htmlFileWriter.flush();
		htmlFileWriter.close();

		final RequestThread thread = new RequestThread(socket, rootDir);

		thread.run();

		verify(socket).setSoTimeout(anyInt());
		verify(socket).getInputStream();
		verify(socket).getOutputStream();
		verifyNoMoreInteractions(socket);

		final Scanner outputStreamScanner= new Scanner(socketOutputStream.toString());
		assertThat(outputStreamScanner.nextLine(), startsWith("HTTP/1.0 200"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Date: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Server: "));
		assertThat(outputStreamScanner.nextLine(), equalTo("Content-Type: text/html"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Expires: "));
		assertThat(outputStreamScanner.nextLine(), equalTo("Content-Length: " + htmlFile.length()));
		assertThat(outputStreamScanner.nextLine(), startsWith("Last-modified: "));
		assertThat(outputStreamScanner.nextLine(), equalTo(""));
		assertThat(outputStreamScanner.nextLine(), equalTo("test contents"));
	}

	/**
	 * Test for {@link RequestThread#run()}
	 */
	@Test
	public void testRun_GETHtml_NotExists() throws Exception {
		final StringBuilder inputStreamBuilder = new StringBuilder();
		inputStreamBuilder.append("GET /test.html HTTP/1.0\n");

		final ByteArrayInputStream socketInputStream =
				new ByteArrayInputStream(
						inputStreamBuilder.toString().getBytes());
		final ByteArrayOutputStream socketOutputStream =
				new ByteArrayOutputStream();

		stub(socket.getInputStream()).toReturn(socketInputStream);
		stub(socket.getOutputStream()).toReturn(socketOutputStream);

		final File rootDir = TestUtil.createTemporaryDirectory("testRun", "");
		final RequestThread thread = new RequestThread(socket, rootDir);

		thread.run();

		verify(socket).setSoTimeout(anyInt());
		verify(socket).getInputStream();
		verify(socket).getOutputStream();
		verifyNoMoreInteractions(socket);

		final Scanner outputStreamScanner= new Scanner(socketOutputStream.toString());
		assertThat(outputStreamScanner.nextLine(), startsWith("HTTP/1.0 404"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Date: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Server: "));
		assertThat(outputStreamScanner.nextLine(), equalTo("Content-Type: text/html"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Expires: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Content-Length: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Last-modified: "));
		assertThat(outputStreamScanner.nextLine(), equalTo(""));
	}

	/**
	 * Test for {@link RequestThread#run()}
	 */
	@Test
	public void testRun_GETBinary() throws Exception {
		final StringBuilder inputStreamBuilder = new StringBuilder();
		inputStreamBuilder.append("GET /test.bin HTTP/1.0\n");

		final ByteArrayInputStream socketInputStream =
				new ByteArrayInputStream(
						inputStreamBuilder.toString().getBytes());
		final ByteArrayOutputStream socketOutputStream =
				new ByteArrayOutputStream();

		stub(socket.getInputStream()).toReturn(socketInputStream);
		stub(socket.getOutputStream()).toReturn(socketOutputStream);

		final File rootDir = TestUtil.createTemporaryDirectory("testRun", "");

		final File binaryFile = new File(rootDir.getCanonicalFile(), "test.bin");
		binaryFile.createNewFile();
		binaryFile.deleteOnExit();

		final SerializableData serializableData = new SerializableData();
		serializableData.number = 100;
		serializableData.string = "String";

		final ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(binaryFile));
		objectOutputStream.writeObject(serializableData);
		objectOutputStream.flush();
		objectOutputStream.close();

		final RequestThread thread = new RequestThread(socket, rootDir);

		thread.run();

		verify(socket).setSoTimeout(anyInt());
		verify(socket).getInputStream();
		verify(socket).getOutputStream();
		verifyNoMoreInteractions(socket);


		final ByteArrayInputStream socketReadingStream =
				new ByteArrayInputStream(socketOutputStream.toByteArray());

		final List<String> lineList = new ArrayList<String>();

		final byte[] readBuf = new byte[1];
		int headerSize = 0;
		StringBuilder readStringBuilder = new StringBuilder();
		while (socketReadingStream.read(readBuf) != -1) {
			headerSize += 1;
			if (readBuf[0] == '\r') {
				continue;
			} else if (readBuf[0] == '\n') {
				lineList.add(readStringBuilder.toString());
				readStringBuilder = new StringBuilder();
			} else {
				readStringBuilder.append(new String(readBuf));
			}

			if (!lineList.isEmpty() && lineList.get(lineList.size() - 1).isEmpty()) {
				break;
			}
		}

		assertThat(lineList.get(0), startsWith("HTTP/1.0 200"));
		assertThat(lineList.get(1), startsWith("Date: "));
		assertThat(lineList.get(2), startsWith("Server: "));
		assertThat(lineList.get(3), equalTo("Content-Type: application/octet-stream"));
		assertThat(lineList.get(4), startsWith("Expires: "));
		assertThat(lineList.get(5), equalTo("Content-Length: " + binaryFile.length()));
		assertThat(lineList.get(6), startsWith("Last-modified: "));
		assertThat(lineList.get(7), equalTo(""));

		final ByteArrayInputStream binaryInputStream = new ByteArrayInputStream(socketOutputStream.toByteArray());
		binaryInputStream.skip(headerSize);

		final ObjectInputStream objectInputStream = new ObjectInputStream(binaryInputStream);
		final SerializableData data = (SerializableData) objectInputStream.readObject();
		assertThat(data.number, equalTo(100));
		assertThat(data.string, equalTo("String"));
	}

	/**
	 * Test for {@link RequestThread#run()}
	 */
	@Test
	public void testRun_GETDirectory() throws Exception {
		final StringBuilder inputStreamBuilder = new StringBuilder();
		inputStreamBuilder.append("GET /directory HTTP/1.0\n");

		final ByteArrayInputStream socketInputStream =
				new ByteArrayInputStream(
						inputStreamBuilder.toString().getBytes());
		final ByteArrayOutputStream socketOutputStream =
				new ByteArrayOutputStream();

		stub(socket.getInputStream()).toReturn(socketInputStream);
		stub(socket.getOutputStream()).toReturn(socketOutputStream);

		final File rootDir = TestUtil.createTemporaryDirectory("testRun", "");

		final File directory = new File(rootDir.getCanonicalFile(), "directory");
		directory.mkdirs();
		directory.deleteOnExit();

		final RequestThread thread = new RequestThread(socket, rootDir);

		thread.run();

		verify(socket).setSoTimeout(anyInt());
		verify(socket).getInputStream();
		verify(socket).getOutputStream();
		verifyNoMoreInteractions(socket);

		final Scanner outputStreamScanner= new Scanner(socketOutputStream.toString());
		assertThat(outputStreamScanner.nextLine(), startsWith("HTTP/1.0 200"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Date: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Server: "));
		assertThat(outputStreamScanner.nextLine(), equalTo("Content-Type: text/html"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Expires: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Last-modified: "));
		assertThat(outputStreamScanner.nextLine(), equalTo(""));
		final String title = "Index of /directory/";
		assertThat(outputStreamScanner.nextLine(), startsWith("<html><head><title>" + title + "</title></head>"));
	}

	/**
	 * Test for {@link RequestThread#run()}
	 */
	@Test
	public void testRun_GETDirectory_IndexHtmlIsActuallyDirectory() throws Exception {
		final StringBuilder inputStreamBuilder = new StringBuilder();
		inputStreamBuilder.append("GET /directory HTTP/1.0\n");

		final ByteArrayInputStream socketInputStream =
				new ByteArrayInputStream(
						inputStreamBuilder.toString().getBytes());
		final ByteArrayOutputStream socketOutputStream =
				new ByteArrayOutputStream();

		stub(socket.getInputStream()).toReturn(socketInputStream);
		stub(socket.getOutputStream()).toReturn(socketOutputStream);

		final File rootDir = TestUtil.createTemporaryDirectory("testRun", "");

		final File directory = new File(rootDir.getCanonicalFile(), "directory");
		directory.mkdirs();
		directory.deleteOnExit();

		// oh, index.html is not file but directory!
		final File indexFileDirectory = new File(directory.getCanonicalFile(), "index.html");
		indexFileDirectory.mkdirs();
		indexFileDirectory.deleteOnExit();

		final RequestThread thread = new RequestThread(socket, rootDir);

		thread.run();

		verify(socket).setSoTimeout(anyInt());
		verify(socket).getInputStream();
		verify(socket).getOutputStream();
		verifyNoMoreInteractions(socket);

		final Scanner outputStreamScanner= new Scanner(socketOutputStream.toString());
		assertThat(outputStreamScanner.nextLine(), startsWith("HTTP/1.0 200"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Date: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Server: "));
		assertThat(outputStreamScanner.nextLine(), equalTo("Content-Type: text/html"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Expires: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Last-modified: "));
		assertThat(outputStreamScanner.nextLine(), equalTo(""));
		final String title = "Index of /directory/";
		assertThat(outputStreamScanner.nextLine(), startsWith("<html><head><title>" + title + "</title></head>"));
	}

	/**
	 * Test for {@link RequestThread#run()}
	 */
	@Test
	public void testRun_GETDirectory_ParentOfRoot() throws Exception {
		final StringBuilder inputStreamBuilder = new StringBuilder();
		inputStreamBuilder.append("GET /../ HTTP/1.0\n");

		final ByteArrayInputStream socketInputStream =
				new ByteArrayInputStream(
						inputStreamBuilder.toString().getBytes());
		final ByteArrayOutputStream socketOutputStream =
				new ByteArrayOutputStream();

		stub(socket.getInputStream()).toReturn(socketInputStream);
		stub(socket.getOutputStream()).toReturn(socketOutputStream);

		final File rootDir = TestUtil.createTemporaryDirectory("testRun", "");
		final RequestThread thread = new RequestThread(socket, rootDir);

		thread.run();

		verify(socket).setSoTimeout(anyInt());
		verify(socket).getInputStream();
		verify(socket).getOutputStream();
		verifyNoMoreInteractions(socket);

		final Scanner outputStreamScanner= new Scanner(socketOutputStream.toString());
		assertThat(outputStreamScanner.nextLine(), startsWith("HTTP/1.0 403"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Date: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Server: "));
		assertThat(outputStreamScanner.nextLine(), equalTo("Content-Type: text/html"));
		assertThat(outputStreamScanner.nextLine(), startsWith("Expires: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Content-Length: "));
		assertThat(outputStreamScanner.nextLine(), startsWith("Last-modified: "));
		assertThat(outputStreamScanner.nextLine(), equalTo(""));
	}

	/**
	 * Test for {@link RequestThread#run()}
	 */
	@Test
	public void testRun_IOException() throws Exception {
		final StringBuilder inputStreamBuilder = new StringBuilder();
		inputStreamBuilder.append("GET /test.html HTTP/1.0\n");

		final InputStream socketInputStream = mock(InputStream.class);
		final ByteArrayOutputStream socketOutputStream =
				new ByteArrayOutputStream();

		stub(socket.getInputStream()).toReturn(socketInputStream);
		stub(socket.getOutputStream()).toReturn(socketOutputStream);

		stub(socketInputStream.read(any(byte[].class), anyInt(), anyInt())).toThrow(new IOException());

		final File rootDir = TestUtil.createTemporaryDirectory("testRun", "");
		final RequestThread thread = new RequestThread(socket, rootDir);

		thread.run();

		verify(socket).setSoTimeout(anyInt());
		verify(socket).getInputStream();
		verify(socket).getOutputStream();
		verify(socketInputStream, atLeastOnce()).read(any(byte[].class), anyInt(), anyInt());
		verifyNoMoreInteractions(socket);

		final Scanner outputStreamScanner= new Scanner(socketOutputStream.toString());
		assertThat(outputStreamScanner.hasNext(), is(false));
	}

	public static class SerializableData implements Serializable {
		private static final long serialVersionUID = 1L;

		public int number;
		public String string;
	}
}
