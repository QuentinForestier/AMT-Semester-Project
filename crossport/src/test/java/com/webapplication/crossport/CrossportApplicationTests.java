package com.webapplication.crossport;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CrossportApplicationTests {

    @Test
    void contextLoads() {
        assertTrue(true);
    }

    @Test
    void testFail() {
        assertTrue(false);
    }
}