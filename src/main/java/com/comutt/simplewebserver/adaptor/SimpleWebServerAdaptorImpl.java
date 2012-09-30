/**
Copyright KOMATSU Seiji (comutt), 2012, https://github.com/comutt/SimpleWebServer-mod

This software is modification of the original from http://www.jibble.org/miniwebserver/,
and licensed under the GNU General Public License (GPL) from the Free Software Foundation, http://www.fsf.org/.

Thanks to Paul James Mutton, the author of the original.

 */
package com.comutt.simplewebserver.adaptor;

import java.io.File;
import java.io.IOException;

import org.jibble.simplewebserver.SimpleWebServer;

/**
 * Implementation of {@link SimpleWebServerAdaptor}
 * @author skomatsu
 *
 */
public class SimpleWebServerAdaptorImpl implements SimpleWebServerAdaptor {

    /** Fixed instance to return */
    private SimpleWebServer server;

    /**
     * Set the server instance to return on {@link #createServer(File, int)}. <br/>
     * This method is for unit testing.
     * @param server Instance of {@link SimpleWebServer}
     */
    public void setServer(SimpleWebServer server) {
        this.server = server;
    }

    /**
     * @see SimpleWebServerAdaptor#createServer(File, int)
     */
    @Override
    public SimpleWebServer createServer(File rootDir, int port) throws IOException {
        if (server != null) {
            return server;
        }

        return new SimpleWebServer(rootDir, port);
    }

}
