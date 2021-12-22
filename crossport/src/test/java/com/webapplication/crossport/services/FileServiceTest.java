package com.webapplication.crossport.services;

import com.webapplication.crossport.domain.services.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.Assert.*;

/**
 * Testing File Service methods
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@SpringBootTest
@AutoConfigureMockMvc
public class FileServiceTest {

	FileService fs = new FileService();

	@Test
	public void isAnAuthorizedExtension_goodExtension_Success(){

		MockMultipartFile image
				= new MockMultipartFile("image", "hello.png",  MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());

		assertTrue(fs.isAnAuthorizedExtension(image));
	}

	@Test
	public void isAnAuthorizedExtension_badExtension_Fail(){

		MockMultipartFile image
				= new MockMultipartFile("image", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

		Assertions.assertFalse(fs.isAnAuthorizedExtension(image));
	}

	@Test
	public void getExtension(){

		MockMultipartFile image
				= new MockMultipartFile("image", "hello.png",  MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());

		Assertions.assertEquals(fs.getExtension(image), ".png");
	}
}
