/**
 *
 */
package com.comutt.simplewebserver.main.argument;

import com.comutt.simplewebserver.exception.InvalidArgumentException;

/**
 * Individual argument parser interface
 * @author skomatsu
 *
 */
public interface Parser {

    /**
     * Parse arguments
     * @param args Command-line arguments
     * @return True if processed
     * @throws InvalidArgumentException If arguments are invalid
     */
    boolean parse(String[] args) throws InvalidArgumentException;

}
