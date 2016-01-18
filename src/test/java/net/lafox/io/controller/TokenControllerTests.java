package net.lafox.io.controller;

import net.lafox.io.IoApplication;
import net.lafox.io.service.TokenService;
import org.junit.After;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
/**
 * Created by Alice Lafox <alice@lafox.net> on 18.01.16
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IoApplication.class)
@ActiveProfiles( profiles={"test"})
@WebAppConfiguration
public class TokenControllerTests {
    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final Random random = new Random();

    @Autowired
    TokenService tokenService;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() throws Exception {
        tokenService.cleanupAfterTests();
    }

    @Test
    public void testAddToken() throws Exception {
        String siteName = "test-domain";
        String ownerName = "item";
        Long ownerId = random.nextLong();
        mockMvc.perform(post("/api/token/add")
                        .param("siteName", siteName)
                        .param("ownerName", ownerName)
                        .param("ownerId", ownerId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.writeToken").exists())
                .andExpect(jsonPath("$.readToken").exists())
        ;
        String writeToken = tokenService.findWriteTokenBySiteNameAndOwnerNameAndOwnerId(siteName, ownerName, ownerId);

        mockMvc.perform(post("/api/token/add")
                        .param("siteName", siteName)
                        .param("ownerName", ownerName)
                        .param("ownerId", ownerId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.writeToken").value(writeToken))
        ;
    }

    @Test
    public void testEmptyFieldsWhenAddToken() throws Exception {
        mockMvc.perform(post("/api/token/add")
                        .param("siteName", "")
                        .param("ownerName", "")
                        .param("ownerId", "0")
        )
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.details").value("rejected: required parameters are empty"))
;
    }



}
