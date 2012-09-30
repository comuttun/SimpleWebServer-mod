/**
Copyright KOMATSU Seiji (comutt), 2012, https://github.com/comutt/SimpleWebServer-mod

This software is modification of the original from http://www.jibble.org/miniwebserver/,
and licensed under the GNU General Public License (GPL) from the Free Software Foundation, http://www.fsf.org/.

Thanks to Paul James Mutton, the author of the original.

*/
package com.comutt.simplewebserver.main;

import java.io.File;
import java.io.IOException;

import com.comutt.simplewebserver.adaptor.SimpleWebServerAdaptor;
import com.comutt.simplewebserver.adaptor.SimpleWebServerAdaptorImpl;

/**
 * Main class
 * @author skomatsu
 *
 */
public class Main {

    /** Default server root */
    public static final String DEFAULT_ROOT = ".";

    /** Default server port to listen */
    public static final int DEFAULT_LISTEN_PORT = 80;

    private static Argument argument = new Argument();
    private static SimpleWebServerAdaptor adaptor =
            new SimpleWebServerAdaptorImpl();


	/**
	 * Set {@link Argument} instance. <br/>
	 * This setter is for unit testing.
     * @param argument The {@link Argument} instance to set
     */
    static void setArgument(Argument argument) {
        Main.argument = argument;
    }

    /**
	 * Set adaptor instance. <br/>
	 * This setter is for unit testing.
     * @param adaptor The adaptor to set
     */
    static void setAdaptor(SimpleWebServerAdaptor adaptor) {
        Main.adaptor = adaptor;
    }



    /**
	 * Main method
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
	    try {
	        argument.parse(args);

	        final String root = argument.getRoot() == null ? DEFAULT_ROOT : argument.getRoot();
	        final int port = argument.getPort() == 0 ? DEFAULT_LISTEN_PORT : argument.getPort();

            adaptor.createServer(new File(root), port).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

}
