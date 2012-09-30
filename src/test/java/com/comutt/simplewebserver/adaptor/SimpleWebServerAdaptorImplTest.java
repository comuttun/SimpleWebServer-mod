/**
Copyright KOMATSU Seiji (comutt), 2012, https://github.com/comutt/SimpleWebServer-mod

This software is modification of the original from http://www.jibble.org/miniwebserver/,
and licensed under the GNU General Public License (GPL) from the Free Software Foundation, http://www.fsf.org/.

Thanks to Paul James Mutton, the author of the original.

 */
package com.comutt.simplewebserver.adaptor;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.jibble.simplewebserver.SimpleWebServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Test class for {@link SimpleWebServerAdaptorImpl}
 * @author skomatsu
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SimpleWebServerAdaptorImpl.class)
public class SimpleWebServerAdaptorImplTest {

    private final SimpleWebServerAdaptorImpl adaptorImpl =
            new SimpleWebServerAdaptorImpl();

    /**
     * Test method for {@link SimpleWebServerAdaptorImpl#createServer(File, int)}.
     */
    @Test
    public void testCreateServer_NotSet() throws Exception {
        final File rootDir = PowerMockito.mock(File.class);
        final int port = 12345;

        final SimpleWebServer server = PowerMockito.mock(SimpleWebServer.class);
        PowerMockito.whenNew(SimpleWebServer.class)
            .withArguments(rootDir, port)
            .thenReturn(server);

        final SimpleWebServer createdServer = adaptorImpl.createServer(rootDir, port);
        PowerMockito.verifyNew(SimpleWebServer.class).withArguments(rootDir, port);
        PowerMockito.verifyNoMoreInteractions(SimpleWebServer.class);

        assertThat(createdServer, sameInstance(server));
    }

    /**
     * Test method for {@link SimpleWebServerAdaptorImpl#createServer(File, int)}.
     */
    @Test
    public void testCreateServer_Set() throws Exception {
        final File rootDir = PowerMockito.mock(File.class);
        final int port = 12345;

        final SimpleWebServer server = PowerMockito.mock(SimpleWebServer.class);
        adaptorImpl.setServer(server);

        final SimpleWebServer createdServer = adaptorImpl.createServer(rootDir, port);
        PowerMockito.verifyZeroInteractions(SimpleWebServer.class);

        assertThat(createdServer, sameInstance(server));
    }

}
