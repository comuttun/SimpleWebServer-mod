/**
 *
 */
package com.comutt.simplewebserver.main;

import com.comutt.simplewebserver.exception.InvalidArgumentException;
import com.comutt.simplewebserver.main.argument.Parser;
import com.comutt.simplewebserver.main.argument.PortParserImpl;
import com.comutt.simplewebserver.main.argument.RootParserImpl;

/**
 * Command-line argument parser
 * @author skomatsu
 *
 */
public class Argument implements RootParserImpl.Callback, PortParserImpl.Callback {

    private final Parser[] parsers = {
            new RootParserImpl(this),
            new PortParserImpl(this),
    };

    private String root;
    private int port;

    /**
     * Parse arguments
     * @param args Command-line arguments passed to main method
     * @throws InvalidArgumentException If arguments are invalid
     */
    public void parse(String[] args) throws InvalidArgumentException {
        for (Parser parser : parsers) {
            parser.parse(args);
        }
    }

    /**
     * @see PortParserImpl.Callback#onParsedPort(int)
     */
    @Override
    public void onParsedPort(int port) {
        this.port = port;
    }

    /**
     * @see RootParserImpl.Callback#onParsedRoot(String)
     */
    @Override
    public void onParsedRoot(String root) {
        this.root = root;
    }

    /**
     * Return the server root
     * @return the root
     */
    public String getRoot() {
        return root;
    }

    /**
     * Return the server port to listen
     * @return the port
     */
    public int getPort() {
        return port;
    }

}
