package net.lafox.io;

import net.lafox.io.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IoApplication.class)
@WebAppConfiguration
public class IoApplicationTests {
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
    public void testApiResponse() throws Exception {
        mockMvc.perform(get("/api/test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.isAlive").value("yes"))
                .andExpect(jsonPath("$.isAliveBool").value(true))
        ;
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

}
