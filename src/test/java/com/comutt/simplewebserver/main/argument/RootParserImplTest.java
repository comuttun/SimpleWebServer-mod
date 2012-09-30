/**
 *
 */
package com.comutt.simplewebserver.main.argument;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.comutt.simplewebserver.exception.InvalidArgumentException;

/**
 * Test for {@link RootParserImpl}
 * @author skomatsu
 *
 */
public class RootParserImplTest implements RootParserImpl.Callback {

    private final RootParserImpl parserImpl = new RootParserImpl(this);

    private String root;


    /**
     * Test method for {@link RootParserImpl#parse(String[])}.
     */
    @Test
    public void testParse_None() {
        final boolean parsed = parserImpl.parse(new String[]{});

        assertThat(parsed, equalTo(false));
        assertThat(root, nullValue());
    }

    /**
     * Test method for {@link RootParserImpl#parse(String[])}.
     */
    @Test
    public void testParse_Success1() {
        final boolean parsed = parserImpl.parse(new String[]{"-root", "/tmp"});

        assertThat(parsed, equalTo(true));
        assertThat(root, equalTo("/tmp"));
    }

    /**
     * Test method for {@link RootParserImpl#parse(String[])}.
     */
    @Test
    public void testParse_Success2() {
        final boolean parsed = parserImpl.parse(new String[]{"-port", "80", "-root", "/tmp"});

        assertThat(parsed, equalTo(true));
        assertThat(root, equalTo("/tmp"));
    }

    /**
     * Test method for {@link RootParserImpl#parse(String[])}.
     */
    @Test
    public void testParse_Success3() {
        final boolean parsed = parserImpl.parse(new String[]{"-root", "/tmp", "-port", "80"});

        assertThat(parsed, equalTo(true));
        assertThat(root, equalTo("/tmp"));
    }

    /**
     * Test method for {@link RootParserImpl#parse(String[])}.
     */
    @Test(expected=InvalidArgumentException.class)
    public void testParse_Failure1() {
        parserImpl.parse(new String[]{"-root"});
    }

    /**
     * Test method for {@link RootParserImpl#parse(String[])}.
     */
    @Test(expected=InvalidArgumentException.class)
    public void testParse_Failure2() {
        parserImpl.parse(new String[]{"-root", "-port", "80"});
    }

    /**
     * @see RootParserImpl.Callback#onParsedRoot(String)
     */
    @Override
    public void onParsedRoot(String root) {
        this.root = root;
    }

}
