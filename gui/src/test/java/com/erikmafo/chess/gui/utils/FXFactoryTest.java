package com.erikmafo.chess.gui.utils;

import org.junit.Before;
import org.junit.Test;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by erikmafo on 08.01.17.
 */
public class FXFactoryTest {

    public static class TestObject {

        @Inject
        private String stringField;

        private boolean destroyed;

        public String getStringField() {
            return stringField;
        }

        @PreDestroy
        public void destroy() {
            destroyed = true;
        }

        public boolean isDestroyed() {
            return destroyed;
        }

    }

    private FXFactory fxFactory;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> context = new HashMap<>();
        context.put("stringField", "Hello World!");
        fxFactory = new FXFactory(context);
    }


    @Test
    public void shouldInjectHelloWorldString() throws Exception {

        TestObject testObject = (TestObject) fxFactory.call(TestObject.class);

        assertThat(testObject.getStringField(), is("Hello World!"));

    }


    @Test
    public void shouldDestroyTestObject() throws Exception {

        TestObject testObject = (TestObject) fxFactory.call(TestObject.class);

        assertFalse(testObject.isDestroyed());

        fxFactory.destroy();

        assertTrue(testObject.isDestroyed());

    }
}