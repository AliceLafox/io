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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IoApplication.class)
@ActiveProfiles( profiles={"test"})
@WebAppConfiguration
public class TokenControllerTests {
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
    public void testAddToken() throws Exception {
        String siteName = "lafox.net";
        String ownerName = "item";
        Long ownerId = 100L;
        mockMvc.perform(post("/api/token/add")
                        .param("siteName", siteName)
                        .param("ownerName", ownerName)
                        .param("ownerId", ownerId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.token").exists())
        ;
        String token = tokenService.findBySiteNameAndOwnerNameAndOwnerId(siteName, ownerName, ownerId);

        mockMvc.perform(post("/api/token/add")
                        .param("siteName", siteName)
                        .param("ownerName", ownerName)
                        .param("ownerId", ownerId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.token").value(token))
        ;
    }

    @Test
    public void testEmptyFieldsWhenAddToken() throws Exception {
        mockMvc.perform(post("/api/token/add"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().reason(containsString("Required String parameter")))
                .andExpect(status().reason("Required String parameter 'siteName' is not present"))
        ;

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
