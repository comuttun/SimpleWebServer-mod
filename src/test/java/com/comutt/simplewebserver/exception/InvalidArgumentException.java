/**
 *
 */
package com.comutt.simplewebserver.exception;

/**
 * Exception that command-line arguments are invalid
 * @author skomatsu
 *
 */
public class InvalidArgumentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * @param message Message
     */
    public InvalidArgumentException(String message) {
        super(message);
    }

    /**
     * Constructor
     * @param message Message
     * @param cause Cause
     */
    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

}
