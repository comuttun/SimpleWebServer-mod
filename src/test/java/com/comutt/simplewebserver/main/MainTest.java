/**
Copyright KOMATSU Seiji (comutt), 2012, https://github.com/comutt/SimpleWebServer-mod

This software is modification of the original from http://www.jibble.org/miniwebserver/,
and licensed under the GNU General Public License (GPL) from the Free Software Foundation, http://www.fsf.org/.

Thanks to Paul James Mutton, the author of the original.

 */
package com.comutt.simplewebserver.main;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;

import org.jibble.simplewebserver.SimpleWebServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.comutt.simplewebserver.adaptor.SimpleWebServerAdaptor;

/**
 * Test class for {@link Main}
 * @author skomatsu
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class MainTest {

    @Mock
    private SimpleWebServerAdaptor adaptor;

    @Mock
    private SimpleWebServer server;

    @Before
    public void setUp() throws Exception {
        Main.setAdaptor(adaptor);
        Main.setArgument(new Argument());
    }

    /**
     * Test method for {@link Main#main(String[])}.
     */
    @Test
    public void testMain_NoArguments() throws Exception {
        when(adaptor.createServer(isA(File.class), anyInt())).thenReturn(server);
        doNothing().when(server).start();

        Main.main(new String[]{});
        final ArgumentCaptor<File> file = ArgumentCaptor.forClass(File.class);
        verify(adaptor).createServer(file.capture(), eq(80));
        verifyNoMoreInteractions(adaptor);

        assertThat(file.getValue().getPath(), equalTo("."));

        verify(server).start();
        verifyNoMoreInteractions(server);
    }

    /**
     * Test method for {@link Main#main(String[])}.
     */
    @Test
    public void testMain_RootArg() throws Exception {
        when(adaptor.createServer(isA(File.class), anyInt())).thenReturn(server);
        doNothing().when(server).start();

        Main.main(new String[]{"-root", "/tmp"});
        final ArgumentCaptor<File> file = ArgumentCaptor.forClass(File.class);
        verify(adaptor).createServer(file.capture(), eq(80));
        verifyNoMoreInteractions(adaptor);

        assertThat(file.getValue().getPath(), equalTo("/tmp"));

        verify(server).start();
        verifyNoMoreInteractions(server);
    }

    /**
     * Test method for {@link Main#main(String[])}.
     */
    @Test
    public void testMain_PortArg() throws Exception {
        when(adaptor.createServer(isA(File.class), anyInt())).thenReturn(server);
        doNothing().when(server).start();

        Main.main(new String[]{"-port", "8080"});
        final ArgumentCaptor<File> file = ArgumentCaptor.forClass(File.class);
        verify(adaptor).createServer(file.capture(), eq(8080));
        verifyNoMoreInteractions(adaptor);

        assertThat(file.getValue().getPath(), equalTo("."));

        verify(server).start();
        verifyNoMoreInteractions(server);
    }

    /**
     * Test method for {@link Main#main(String[])}.
     */
    @Test
    public void testMain_PortArg_RootArg() throws Exception {
        when(adaptor.createServer(isA(File.class), anyInt())).thenReturn(server);
        doNothing().when(server).start();

        Main.main(new String[]{"-port", "8080", "-root", "/tmp"});
        final ArgumentCaptor<File> file = ArgumentCaptor.forClass(File.class);
        verify(adaptor).createServer(file.capture(), eq(8080));
        verifyNoMoreInteractions(adaptor);

        assertThat(file.getValue().getPath(), equalTo("/tmp"));

        verify(server).start();
        verifyNoMoreInteractions(server);
    }

}
