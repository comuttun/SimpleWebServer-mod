/**
 *
 */
package com.comutt.simplewebserver.main.argument;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.comutt.simplewebserver.exception.InvalidArgumentException;

/**
 * Test for {@link PortParsetImpl}
 * @author skomatsu
 *
 */
public class PortParserImplTest implements PortParserImpl.Callback {

    private final PortParserImpl parserImpl = new PortParserImpl(this);

    private int port;

    /**
     * Test method for {@link PortParsetImpl#parse(String[])}.
     */
    @Test
    public void testParse_None() {
        final boolean parsed = parserImpl.parse(new String[]{});

        assertThat(parsed, equalTo(false));
        assertThat(port, equalTo(0));
    }

    /**
     * Test method for {@link PortParsetImpl#parse(String[])}.
     */
    @Test
    public void testParse_Success1() {
        final boolean parsed = parserImpl.parse(new String[]{"-port", "80"});

        assertThat(parsed, equalTo(true));
        assertThat(port, equalTo(80));
    }

    /**
     * Test method for {@link PortParsetImpl#parse(String[])}.
     */
    @Test
    public void testParse_Success2() {
        final boolean parsed = parserImpl.parse(new String[]{"-port", "80", "-root", "/tmp"});

        assertThat(parsed, equalTo(true));
        assertThat(port, equalTo(80));
    }

    /**
     * Test method for {@link PortParsetImpl#parse(String[])}.
     */
    @Test
    public void testParse_Success3() {
        final boolean parsed = parserImpl.parse(new String[]{"-root", "/tmp", "-port", "80"});

        assertThat(parsed, equalTo(true));
        assertThat(port, equalTo(80));
    }

    /**
     * Test method for {@link PortParsetImpl#parse(String[])}.
     */
    @Test(expected=InvalidArgumentException.class)
    public void testParse_Failure1() {
        parserImpl.parse(new String[]{"-port"});
    }

    /**
     * Test method for {@link PortParsetImpl#parse(String[])}.
     */
    @Test(expected=InvalidArgumentException.class)
    public void testParse_Failure2() {
        parserImpl.parse(new String[]{"-port", "-root", "/tmp"});
    }

    /**
     * @see com.comutt.simplewebserver.main.argument.PortParserImpl.Callback#onParsedPort(int)
     */
    @Override
    public void onParsedPort(int port) {
        this.port = port;
    }

}
