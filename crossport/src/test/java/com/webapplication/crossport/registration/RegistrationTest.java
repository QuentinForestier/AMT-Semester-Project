package com.webapplication.crossport.registration;

import com.webapplication.crossport.service.DefaultMemberService;
import com.webapplication.crossport.service.MemberRegistrationData;
import com.webapplication.crossport.service.exception.RegistrationException;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for registration. This test class checks multiple users inputs
 * and checks if DefaultMemberService behave correctly depending on authentication server response
 * @author Herzig Melvyn
 */
public class RegistrationTest extends DefaultMemberService{

    /**
     * Mocking of authentication container
     */
    private static GenericContainer authContainer = new GenericContainer(
            new ImageFromDockerfile().withDockerfile(Paths.get("../mocking/auth-service/Dockerfile")))
            .withExposedPorts(8081);


    /**
     * Stats the authentication container
     */
    @BeforeAll
    static void onceOnStart() {
        authContainer.start();
    }

    /**
     * With a non existing user, authentication should succed
     */
    @Test
    void nonExistingUserShouldSuccess() {

        assertTrue(tryRegister("melvyn", "agreatpassword", "agreatpassword"));
    }

    /**
     * With an existing user, authentication should fail
     */
    @Test
    void existingUserShouldFail() {

        tryRegister("melvyn2", "agreatpassword", "agreatpassword");
        assertFalse(tryRegister("melvyn2", "agreatpassword", "agreatpassword"));
    }

    /**
     * With a non existing user, authentication should fail if password and confirmation don't match
     */
    @Test
    void nonExistingUserPasswordNotMatchingShouldFail() {

        assertFalse(tryRegister("melvyn2", "agreatpassword1", "agreatpassword2"));
    }

    /**
     * With a non existing user, authentication should fail if password is not strong enough (less than 8 chars)
     */
    @Test
    void nonExistingUserPasswordNotEnoughSecuredShouldFail() {

        assertFalse(tryRegister("melvyn2", "small", "small"));
    }

    /**
     * With a non existing user, authentication should fail if password is contains non alphanumeric chars
     */
    @Test
    void nonExistingUserPasswordInvalidChar() {

        assertFalse(tryRegister("melvyn2", "a spaced password", "a spaced password"));
    }

    /**
     * Tries to authenticate a user.
     * @param username Provided username
     * @param password Provided password
     * @param passwordConfirmation Provided confirmation password
     * @return True if authentication is a success else false
     */
    private boolean tryRegister(String username, String password, String passwordConfirmation) {
        MemberRegistrationData member = new MemberRegistrationData();
        member.setUsername(username);
        member.setPassword(password);
        member.setPasswordConfirmation(passwordConfirmation);

        DefaultMemberService dms = new DefaultMemberService();
        try {
            dms.authenticate(member, "localhost", authContainer.getFirstMappedPort());
        } catch (RegistrationException e) {
            return false;
        }
        return true;
    }
}
