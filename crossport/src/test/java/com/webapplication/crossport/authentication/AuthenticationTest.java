package com.webapplication.crossport.authentication;

import com.webapplication.crossport.config.CustomAuthenticationProvider;
import com.webapplication.crossport.service.AuthService;
import com.webapplication.crossport.service.DefaultMemberService;
import com.webapplication.crossport.service.MemberRegistrationData;
import com.webapplication.crossport.service.exception.RegistrationException;
import org.junit.jupiter.api.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for authentication. This test class checks multiple users inputs for registration and login.
 * For registration: checks if DefaultMemberService behave correctly depending on authentication server response.
 * For login: checks if authenticate method of customAuthenticationProvider works
 * @author Herzig Melvyn
 */
public class AuthenticationTest extends DefaultMemberService{

    /**
     * Mocking of authentication container
     */
    private static GenericContainer authContainer = new GenericContainer(
            new ImageFromDockerfile().withDockerfile(Paths.get("../mocking/auth-service/Dockerfile")))
            .withExposedPorts(8081);

    /**
     * Instance of CustomAuthenticationProvider to simulate login
     */
    private static CustomAuthenticationProvider cap = new CustomAuthenticationProvider();


    /**
     * Stats the authentication container
     */
    @BeforeAll
    static void onceOnStart() {
        authContainer.start();
        AuthService.setAddressAndPort("localhost", authContainer.getFirstMappedPort());
    }

    /**
     * Stops the container the authentication container
     */
    @AfterAll
    static void onceOnFinish() {
        authContainer.stop();
    }

    /**
     * With a non existing user, authentication should succed
     */
    @Test
    void Register_NonExistingUser_Success() {
        assertTrue(tryRegister("melvyn", "agreatpassword", "agreatpassword"));
    }

    /**
     * With an existing user, authentication should fail
     */
    @Test
    void testRegister_existingUser_Fail() {

        tryRegister("melvyn2", "agreatpassword", "agreatpassword");
        Assertions.assertFalse(tryRegister("melvyn2", "agreatpassword", "agreatpassword"));
    }

    /**
     * With a non existing user, authentication should fail if password and confirmation don't match
     */
    @Test
    void Register_nonExistingUserPasswordsNotMatching_Fail() {

        Assertions.assertFalse(tryRegister("melvyn2", "agreatpassword1", "agreatpassword2"));
    }

    /**
     * With a non existing user, authentication should fail if password is not strong enough (less than 8 chars)
     */
    @Test
    void Register_nonExistingUserPasswordNotEnoughSecured_Fail() {

        Assertions.assertFalse(tryRegister("melvyn2", "small", "small"));
    }

    /**
     * With a non existing user, authentication should fail if password is contains non alphanumeric chars
     */
    @Test
    void Register_nonExistingUserPasswordInvalidChar_Fail() {

        Assertions.assertFalse(tryRegister("melvyn2", "a spaced password", "a spaced password"));
    }

    /**
     * Tries to login with unknow credentials
     */
    @Test
    void Login_UnknownCredentials_Fail(){
        try {
            cap.authenticate(makeAuthentication("UnknownUser", "badPass"));
        }
        catch (AuthenticationException e){
            assertTrue(true);
            return;
        }
        assertTrue(false);

    }

    /**
     * Tries to login with unknow credentials
     */
    @Test
    void Login_AdminAccount_Success(){

        org.springframework.security.core.Authentication auth = null;

        try {
            auth = cap.authenticate(makeAuthentication("admin", "admin"));
        }
        catch (AuthenticationException e){
            assertTrue(false);
            return;
        }

        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    /**
     * Tries to login with unknow credentials
     */
    @Test
    void Login_UserAccount_Success(){

        Authentication auth = null;

        try {
            auth = cap.authenticate(makeAuthentication("toto", "toto"));
        }
        catch (AuthenticationException e){
            assertTrue(false);
            return;
        }

        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    /**
     * Creates an authentication object in order to try the CustomAuthenticationProvider
     * @param username Username given
     * @param password Password given
     * @return An authentication containing the credentials above
     */
    private Authentication makeAuthentication(String username, String password){
        return new UsernamePasswordAuthenticationToken(username, password);
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
            dms.authenticate(member);
        } catch (RegistrationException e) {
            return false;
        }
        return true;
    }
}
