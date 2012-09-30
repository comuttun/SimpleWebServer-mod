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
 * Adaptor for {@link SimpleWebServer}
 * @author skomatsu
 *
 */
public interface SimpleWebServerAdaptor {

    /**
     * Create a instance of {@link SimpleWebServer}
     * @param rootDir Root directory of the web server
     * @param port Port the web server listens
     * @return Instance of {@link SimpleWebServer}
     * @throws IOException Problem occurred while reading/writing from/to socket
     */
    SimpleWebServer createServer(File rootDir, int port) throws IOException;

}
