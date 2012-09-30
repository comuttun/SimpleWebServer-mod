/**
 *
 */
package com.comutt.simplewebserver.main.argument;

import com.comutt.simplewebserver.exception.InvalidArgumentException;

/**
 * Parseer for port argument
 * @author skomatsu
 *
 */
public class PortParserImpl implements Parser {

    private final Callback callback;

    /**
     * Constructor
     * @param callback Callback to receive the argument parsed
     */
    public PortParserImpl(Callback callback) {
        this.callback = callback;
    }

    /**
     * @see Parser#parse(String[])
     */
    @Override
    public boolean parse(String[] args) throws InvalidArgumentException {
        if (args.length == 0) {
            return false;
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-port")) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    throw new InvalidArgumentException("-port option require parameter");
                }

                try {
                    callback.onParsedPort(Integer.parseInt(args[i + 1]));
                } catch (NumberFormatException ex) {
                    throw new InvalidArgumentException("Invalid port", ex);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Callback to receive the parsed
     * @author skomatsu
     *
     */
    public interface Callback {
        void onParsedPort(int port);
    }

}
