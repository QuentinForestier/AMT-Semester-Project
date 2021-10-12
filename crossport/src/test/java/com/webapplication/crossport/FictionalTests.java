package com.webapplication.crossport;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is a showcase test class.
 * More information are available on: https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html
 * @author Herzig Melvyn
 */
@SpringBootTest
public class FictionalTests {

    @BeforeAll
    static void onceOnStart() {
        // This is a test called once at the beginning of the sequence.
    }

    @BeforeEach
    void eachTimeStart() {
        // This is a test executed before each test case (@Test)
    }

    @Test
    void firstTest() {
        // This is a simple test case, executed once.
        assertTrue(true);
    }

    @Repeat(5)
    @Test
    void testFiveTimes() {
        // This test will be repeated 10 times
        assertTrue(true);
    }

    @AfterEach
    void eachTimeEnd() {
        // This is a test executed after each test case (@Test)
    }

    @AfterAll
    static void onceOnEnd() {
        // Called once all test are done
    }
}
