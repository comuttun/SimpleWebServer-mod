/**
 *
 */
package com.comutt.simplewebserver.main;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Test for {@link Argument}
 * @author skomatsu
 *
 */
public class ArgumentTest {

    private final Argument parser = new Argument();

    /**
     * Test for {@link Argument#parse(String[])}
     */
    @Test
    public void testParse_NoArguments() {
        parser.parse(new String[]{});
        assertThat(parser.getRoot(), nullValue());
        assertThat(parser.getPort(), equalTo(0));
    }

    /**
     * Test for {@link Argument#parse(String[])}
     */
    @Test
    public void testParse_RootOnly() {
        parser.parse(new String[]{"-root", "/tmp"});
        assertThat(parser.getRoot(), equalTo("/tmp"));
        assertThat(parser.getPort(), equalTo(0));
    }

    /**
     * Test for {@link Argument#parse(String[])}
     */
    @Test
    public void testParse_PortOnly() {
        parser.parse(new String[]{"-port", "8080"});
        assertThat(parser.getRoot(), nullValue());
        assertThat(parser.getPort(), equalTo(8080));
    }

    /**
     * Test for {@link Argument#parse(String[])}
     */
    @Test
    public void testParse_RootAndPort() {
        parser.parse(new String[]{"-port", "8080", "-root", "/tmp"});
        assertThat(parser.getRoot(), equalTo("/tmp"));
        assertThat(parser.getPort(), equalTo(8080));
    }

}
