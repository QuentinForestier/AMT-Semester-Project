package com.webapplication.crossport.registration;

import com.webapplication.crossport.service.DefaultMemberService;
import com.webapplication.crossport.service.MemberRegistrationData;
import com.webapplication.crossport.service.exception.RegistrationException;
import org.junit.jupiter.api.*;
//import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.images.builder.ImageFromDockerfile;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for registration. This test class checks multiple users inputs
 * and checks if DefaultMemberService behave correctly depending on authentication server response
 * @author Herzig Melvyn
 */
public class RegistrationTest extends DefaultMemberService{

    /*private GenericContainer authContainer = new GenericContainer(
            new ImageFromDockerfile().withDockerfile(Paths.get("../mocking/auth-service/Dockerfile")));


    @BeforeAll
    static void onceOnStart() {
        // This is a test called once at the beginning of the sequence.
    }

    @Test
    void validLoginTest() {
        System.out.println(authContainer.getDockerImageName());


        MemberRegistrationData member = new MemberRegistrationData();
        member.setUsername("admin");
        member.setPassword("admin");
        member.setPasswordConfirmation("admin");

        DefaultMemberService dms = new DefaultMemberService();
        try {
            dms.register(member);
        } catch (RegistrationException e) {
            e.printStackTrace();
        }

        // This is a simple test case, executed once.
        assertTrue(true);
    }*/
}
