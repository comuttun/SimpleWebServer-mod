/**
 *
 */
package com.comutt.simplewebserver.main.argument;

import com.comutt.simplewebserver.exception.InvalidArgumentException;

/**
 * Parseer for root argument
 * @author skomatsu
 *
 */
public class RootParserImpl implements Parser {

    private final Callback callback;

    /**
     * Constructor
     * @param callback Callback to receive the argument parsed
     */
    public RootParserImpl(Callback callback) {
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
            if (args[i].equals("-root")) {
                if (args.length <= i + 1 || args[i + 1].startsWith("-")) {
                    throw new InvalidArgumentException("-root option require parameter");
                }

                callback.onParsedRoot(args[i + 1]);

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
        void onParsedRoot(String root);
    }

}
