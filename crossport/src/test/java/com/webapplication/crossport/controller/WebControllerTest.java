package com.webapplication.crossport.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Test class that check access to home page
 * @author Herzig Melvyn
 */
@SpringBootTest
@AutoConfigureMockMvc
public class WebControllerTest {

	@Autowired
	private MockMvc mvc;


	@Test
	@WithAnonymousUser
	public void getRegister_Anonymous_Success() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"));
	}

	@Test
	@WithMockUser(roles = {"USER"})
	public void getRegister_Registered_Fail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"));
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void getRegister_Admin_Fail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"));
	}
}
