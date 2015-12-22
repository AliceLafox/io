package net.lafox.io.controller;

import net.lafox.io.IoApplication;
import net.lafox.io.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IoApplication.class)
@ActiveProfiles( profiles={"test"})
@WebAppConfiguration
public class ImageControllerTest {
//    MockMultipartFile image = new MockMultipartFile("image", "", "application/json", "{\"image\": \"C:\\Users\\Public\\Pictures\\Sample Pictures\\Penguins.jpg\"}".getBytes());

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    TokenService tokenService;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testImageResponse() throws Exception {
        mockMvc.perform(get("/image/test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.isAlive").value("yes"))
                .andExpect(jsonPath("$.isAliveBool").value(true))
        ;
    }

}