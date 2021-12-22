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
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@SpringBootTest
@AutoConfigureMockMvc
public class WebControllerTest {

	@Autowired
	private MockMvc mvc;


	@Test
	@WithAnonymousUser
	public void getHomePage_Anonymous_Success() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"));
	}

	@Test
	@WithMockUser(roles = {"USER"})
	public void getHomePage_Registered_Success() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"));
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void getHomePage_Admin_Success() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"));
	}

	@Test
	@WithAnonymousUser
	public void getHomePageFromRedirect_Anonymous_Success() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/home"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));
	}

	@Test
	@WithMockUser(roles = {"USER"})
	public void getHomePageFromRedirect_Registered_Fail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/home"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void getHomePagFromRedireceFromRedirect_Admin_Fail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/home"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));
	}
}
