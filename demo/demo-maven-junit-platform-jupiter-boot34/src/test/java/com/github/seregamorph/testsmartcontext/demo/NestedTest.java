package com.github.seregamorph.testsmartcontext.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;


public class NestedTest extends AbstractIntegrationTest {

    @Nested
    @SpringBootTest
    @ContextConfiguration(classes = TestConfiguration.class)
    class NestedOne {
        @Test
        void test() {
            Assertions.assertTrue(true);
        }
    }

    @Nested
    @SpringBootTest
    @ContextConfiguration(classes = TestConfiguration.class)
    @TestPropertySource(properties = {"needs.another.context=1"})
    class NestedTwo {
        @Test
        void test() {
            Assertions.assertTrue(true);
        }

        @Nested
        @ContextConfiguration(classes = DeeplyNested.Configuration2.class)
        class DeeplyNested {
            @Test
            void innerTest() {
                Assertions.assertTrue(true);
            }

            @Configuration
            public static class Configuration2 {

            }

        }
    }

    @WebAppConfiguration
    @ContextConfiguration(classes = {
        com.github.seregamorph.testsmartcontext.demo.Integration1Test.Configuration.class
    })
    @Nested
    class SameContextAsIntegration1Test {
        @Test
        public void test() {
            System.out.println("SameContextAsIntegration1Test.test");
        }

    }

    @Configuration
    public static class TestConfiguration {

    }

}
