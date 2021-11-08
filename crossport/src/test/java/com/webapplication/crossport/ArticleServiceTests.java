package com.webapplication.crossport;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ArticleServiceTests {
    @BeforeAll
    static void onceOnStart() {
        // This is a test called once at the beginning of the sequence.
    }

    @BeforeEach
    void eachTimeStart() {
        // This is a test executed before each test case (@Test)
    }

    @AfterEach
    void eachTimeEnd() {
        // This is a test executed after each test case (@Test)
    }

    @AfterAll
    static void onceOnEnd() {
        // Called once all test are done
    }

    @Test
    void firstTest() {
        // This is a simple test case, executed once.
        assertTrue(true);
    }
}
