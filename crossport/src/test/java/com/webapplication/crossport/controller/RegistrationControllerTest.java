package com.webapplication.crossport.controller;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.io.entity.EntityUtils;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.message.BasicNameValuePair;
import com.webapplication.crossport.service.AuthService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Paths;
import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test to get registration page
 * @author Herzig Melvyn
 */
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

	/**
	 * Mocking of authentication container
	 */
	private static GenericContainer authContainer = new GenericContainer(
			new ImageFromDockerfile().withDockerfile(Paths.get("../mocking/auth-service/Dockerfile")))
			.withExposedPorts(8081);

	@Autowired
	private MockMvc mvc;

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


	@Test
	@WithAnonymousUser
	public void getRegister_Anonymous_Success() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/register"))
				.andExpect(status().isOk())
				.andExpect(view().name("register"));
	}

	@Test
	@WithMockUser(roles = {"USER"})
	public void getRegister_Registered_Fail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/register"))
				.andExpect(status().is4xxClientError());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void getRegister_Admin_Fail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/register"))
				.andExpect(status().is4xxClientError());
	}

	@Test
	@WithAnonymousUser
	public void postRegister_Anonymous_Success() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post("/register")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
								new BasicNameValuePair("username", "NewUser"),
								new BasicNameValuePair("password", "AReallyL0ngP@$$word"),
								new BasicNameValuePair("passwordConfirmation", "AReallyL0ngP@$$word")
						)))))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/login"));
	}

	@Test
	@WithAnonymousUser
	public void postRegister_Anonymous_PasswordsMissmatch_Fail() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post("/register")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
								new BasicNameValuePair("username", "NewUser"),
								new BasicNameValuePair("password", "AReallyL0ngP@$$word"),
								new BasicNameValuePair("passwordConfirmation", "AReallyL0ngP@$$word123")
						)))))
				.andExpect(status().isOk())
				.andExpect(view().name("register"));
	}

	@Test
	@WithAnonymousUser
	public void postRegister_Anonymous_EmptyUsername_Fail() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post("/register")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
								new BasicNameValuePair("username", ""),
								new BasicNameValuePair("password", "AReallyL0ngP@$$word"),
								new BasicNameValuePair("passwordConfirmation", "AReallyL0ngP@$$word123")
						)))))
				.andExpect(status().isOk())
				.andExpect(view().name("register"));
	}

	@Test
	@WithAnonymousUser
	public void postRegister_Anonymous_EmptyPassword_Fail() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post("/register")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
								new BasicNameValuePair("username", "NewUser"),
								new BasicNameValuePair("password", ""),
								new BasicNameValuePair("passwordConfirmation", "AReallyL0ngP@$$word123")
						)))))
				.andExpect(status().isOk())
				.andExpect(view().name("register"));
	}

	@Test
	@WithAnonymousUser
	public void postRegister_Anonymous_EmptyPasswordConfirmation_Fail() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post("/register")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
								new BasicNameValuePair("username", "NewUser"),
								new BasicNameValuePair("password", "AReallyL0ngP@$$word123"),
								new BasicNameValuePair("passwordConfirmation", "")
						)))))
				.andExpect(status().isOk())
				.andExpect(view().name("register"));
	}

	@Test
	@WithAnonymousUser
	public void postRegister_Anonymous_AlreadyExisting_Fail() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post("/register")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
								new BasicNameValuePair("username", "NewUser1"),
								new BasicNameValuePair("password", "AReallyL0ngP@$$word123"),
								new BasicNameValuePair("passwordConfirmation", "AReallyL0ngP@$$word123")
						)))));

		mvc.perform(MockMvcRequestBuilders.post("/register")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
								new BasicNameValuePair("username", "NewUser1"),
								new BasicNameValuePair("password", "AReallyL0ngP@$$word123"),
								new BasicNameValuePair("passwordConfirmation", "AReallyL0ngP@$$word123")
						)))))
				.andExpect(status().isOk())
				.andExpect(view().name("register"));
	}
}
