package net.lafox.io.controller;

import net.lafox.io.IoApplication;
import net.lafox.io.entity.Image;
import net.lafox.io.entity.Token;
import net.lafox.io.service.ImageReadService;
import net.lafox.io.service.ImageWriteService;
import net.lafox.io.service.TokenService;
import net.lafox.io.utils.ImgUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
/**
 * Created by Alice Lafox <alice@lafox.net> on 18.01.16
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IoApplication.class)
@ActiveProfiles(profiles = {"test"})
@WebAppConfiguration
public class ImageSortControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private String siteName ;
    private String ownerName;
    private static Long ownerId = null;
    private String ip;


    public InputStream getInputStream(String fn){
        return this.getClass().getClassLoader().getResourceAsStream(fn);
    }
    public File getFile(String fn){
        return new File(this.getClass().getClassLoader().getResource(fn).getFile());
    }
    @Autowired
    TokenService tokenService;

    @Autowired
    ImageWriteService imageWriteService;
    @Autowired
    ImageReadService imageReadService;

    private final Random random = new Random();

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
         siteName = "test-domain";
         ownerName = "item";
        ownerId = (ownerId == null) ? random.nextLong() : ownerId++;
         ip = "10.10.10.10";



    }

    @After
    public void tearDown() throws Exception {
        tokenService.cleanupAfterTests();
    }
    private void upload6Files(String rwToken) throws Exception {
        MockMultipartFile image0 = new MockMultipartFile("data", "testImage1.png", "image/png", getInputStream("testImage1.png"));
        MockMultipartFile image1 = new MockMultipartFile("data", "testImage2.png", "image/png", getInputStream("testImage2.png"));
        MockMultipartFile image2 = new MockMultipartFile("data", "testImage3.png", "image/png", getInputStream("testImage3.png"));
        MockMultipartFile image3 = new MockMultipartFile("data", "testImage1.png", "image/png", getInputStream("testImage1.png"));
        MockMultipartFile image4 = new MockMultipartFile("data", "testImage2.png", "image/png", getInputStream("testImage2.png"));
        MockMultipartFile image5 = new MockMultipartFile("data", "testImage3.png", "image/png", getInputStream("testImage3.png"));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image/upload")
                .file(image0)
                .file(image1)
                .file(image1)
                .file(image3)
                .file(image4)
                .file(image5)
                .param("token", rwToken))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.status").value("OK"))
        ;

    }
    @Test
    public void testImagesSortIndexFignya() throws Exception {
        Token token = tokenService.addToken(siteName, ownerName, ++ownerId, ip);
        upload6Files(token.getWriteToken());
        Image img0= imageReadService.getImages(token).get(0);

        mockMvc.perform(post("/image/sortIndex/" + img0.getId())
                        .param("token", token.getWriteToken())
                        .param("op", "toLastFignya")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.details").value("Unsupported operation 'toLastFignya' you can: plus|minus|toFirst|toLast"))
        ;
    }

    @Test
    public void testImagesSortIndexToLast() throws Exception {
        Token token = tokenService.addToken(siteName, ownerName, ++ownerId, ip);

        upload6Files(token.getWriteToken());
        Image img0 = imageReadService.getImages(token).get(0);
        Image img1 = imageReadService.getImages(token).get(1);
        Image img2 = imageReadService.getImages(token).get(2);
        Image img3 = imageReadService.getImages(token).get(3);
        Image img4 = imageReadService.getImages(token).get(4);
        Image img5 = imageReadService.getImages(token).get(5);

        mockMvc.perform(get("/image/list/" + token.getReadToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.images[0].id").value(img0.getId().intValue()))
                .andExpect(jsonPath("$.images[1].id").value(img1.getId().intValue()))
                .andExpect(jsonPath("$.images[2].id").value(img2.getId().intValue()))
                .andExpect(jsonPath("$.images[3].id").value(img3.getId().intValue()))
                .andExpect(jsonPath("$.images[4].id").value(img4.getId().intValue()))
                .andExpect(jsonPath("$.images[5].id").value(img5.getId().intValue()))
                .andExpect(jsonPath("$.images[0].sortIndex").value(1))
                .andExpect(jsonPath("$.images[1].sortIndex").value(2))
                .andExpect(jsonPath("$.images[2].sortIndex").value(3))
                .andExpect(jsonPath("$.images[3].sortIndex").value(4))
                .andExpect(jsonPath("$.images[4].sortIndex").value(5))
                .andExpect(jsonPath("$.images[5].sortIndex").value(6))
        ;


        mockMvc.perform(post("/image/sortIndex/" + img1.getId())
                        .param("token", token.getWriteToken())
                        .param("op", "toLast")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;

        mockMvc.perform(get("/image/list/" + token.getReadToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.images[0].id").value(img0.getId().intValue()))
                .andExpect(jsonPath("$.images[1].id").value(img2.getId().intValue()))
                .andExpect(jsonPath("$.images[2].id").value(img3.getId().intValue()))
                .andExpect(jsonPath("$.images[3].id").value(img4.getId().intValue()))
                .andExpect(jsonPath("$.images[4].id").value(img5.getId().intValue()))
                .andExpect(jsonPath("$.images[5].id").value(img1.getId().intValue()))
                .andExpect(jsonPath("$.images[0].sortIndex").value(1))
                .andExpect(jsonPath("$.images[1].sortIndex").value(3))
                .andExpect(jsonPath("$.images[2].sortIndex").value(4))
                .andExpect(jsonPath("$.images[3].sortIndex").value(5))
                .andExpect(jsonPath("$.images[4].sortIndex").value(6))
                .andExpect(jsonPath("$.images[5].sortIndex").value(7))
        ;
    }
    @Test
    public void testImagesSortIndexToFirst() throws Exception {
        Token token = tokenService.addToken(siteName, ownerName, ++ownerId, ip);

        upload6Files(token.getWriteToken());
        Image img0 = imageReadService.getImages(token).get(0);
        Image img1 = imageReadService.getImages(token).get(1);
        Image img2 = imageReadService.getImages(token).get(2);
        Image img3 = imageReadService.getImages(token).get(3);
        Image img4 = imageReadService.getImages(token).get(4);
        Image img5 = imageReadService.getImages(token).get(5);

        mockMvc.perform(get("/image/list/" + token.getReadToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.images[0].id").value(img0.getId().intValue()))
                .andExpect(jsonPath("$.images[1].id").value(img1.getId().intValue()))
                .andExpect(jsonPath("$.images[2].id").value(img2.getId().intValue()))
                .andExpect(jsonPath("$.images[3].id").value(img3.getId().intValue()))
                .andExpect(jsonPath("$.images[4].id").value(img4.getId().intValue()))
                .andExpect(jsonPath("$.images[5].id").value(img5.getId().intValue()))
                .andExpect(jsonPath("$.images[0].sortIndex").value(1))
                .andExpect(jsonPath("$.images[1].sortIndex").value(2))
                .andExpect(jsonPath("$.images[2].sortIndex").value(3))
                .andExpect(jsonPath("$.images[3].sortIndex").value(4))
                .andExpect(jsonPath("$.images[4].sortIndex").value(5))
                .andExpect(jsonPath("$.images[5].sortIndex").value(6))
        ;


        mockMvc.perform(post("/image/sortIndex/" + img2.getId())
                        .param("token", token.getWriteToken())
                        .param("op", "toFirst")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;

        mockMvc.perform(get("/image/list/" + token.getReadToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.images[0].id").value(img2.getId().intValue()))
                .andExpect(jsonPath("$.images[1].id").value(img0.getId().intValue()))
                .andExpect(jsonPath("$.images[2].id").value(img1.getId().intValue()))
                .andExpect(jsonPath("$.images[3].id").value(img3.getId().intValue()))
                .andExpect(jsonPath("$.images[4].id").value(img4.getId().intValue()))
                .andExpect(jsonPath("$.images[5].id").value(img5.getId().intValue()))
                .andExpect(jsonPath("$.images[0].sortIndex").value(0))
                .andExpect(jsonPath("$.images[1].sortIndex").value(1))
                .andExpect(jsonPath("$.images[2].sortIndex").value(2))
                .andExpect(jsonPath("$.images[3].sortIndex").value(4))
                .andExpect(jsonPath("$.images[4].sortIndex").value(5))
                .andExpect(jsonPath("$.images[5].sortIndex").value(6))
        ;
    }


    @Test
    public void testImagesSortIndexPlus() throws Exception {
        Token token = tokenService.addToken(siteName, ownerName, ++ownerId, ip);

        upload6Files(token.getWriteToken());
        Image img0 = imageReadService.getImages(token).get(0);
        Image img1 = imageReadService.getImages(token).get(1);
        Image img2 = imageReadService.getImages(token).get(2);
        Image img3 = imageReadService.getImages(token).get(3);
        Image img4 = imageReadService.getImages(token).get(4);
        Image img5 = imageReadService.getImages(token).get(5);

        mockMvc.perform(get("/image/list/" + token.getReadToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.images[0].id").value(img0.getId().intValue()))
                .andExpect(jsonPath("$.images[1].id").value(img1.getId().intValue()))
                .andExpect(jsonPath("$.images[2].id").value(img2.getId().intValue()))
                .andExpect(jsonPath("$.images[3].id").value(img3.getId().intValue()))
                .andExpect(jsonPath("$.images[4].id").value(img4.getId().intValue()))
                .andExpect(jsonPath("$.images[5].id").value(img5.getId().intValue()))
                .andExpect(jsonPath("$.images[0].sortIndex").value(1))
                .andExpect(jsonPath("$.images[1].sortIndex").value(2))
                .andExpect(jsonPath("$.images[2].sortIndex").value(3))
                .andExpect(jsonPath("$.images[3].sortIndex").value(4))
                .andExpect(jsonPath("$.images[4].sortIndex").value(5))
                .andExpect(jsonPath("$.images[5].sortIndex").value(6))
        ;


        mockMvc.perform(post("/image/sortIndex/" + img2.getId())
                        .param("token", token.getWriteToken())
                        .param("op", "plus")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;

        mockMvc.perform(get("/image/list/" + token.getReadToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.images[0].id").value(img0.getId().intValue()))
                .andExpect(jsonPath("$.images[1].id").value(img1.getId().intValue()))
                .andExpect(jsonPath("$.images[2].id").value(img3.getId().intValue()))
                .andExpect(jsonPath("$.images[3].id").value(img2.getId().intValue()))
                .andExpect(jsonPath("$.images[4].id").value(img4.getId().intValue()))
                .andExpect(jsonPath("$.images[5].id").value(img5.getId().intValue()))
                .andExpect(jsonPath("$.images[0].sortIndex").value(1))
                .andExpect(jsonPath("$.images[1].sortIndex").value(2))
                .andExpect(jsonPath("$.images[2].sortIndex").value(3))
                .andExpect(jsonPath("$.images[3].sortIndex").value(4))
                .andExpect(jsonPath("$.images[4].sortIndex").value(5))
                .andExpect(jsonPath("$.images[5].sortIndex").value(6))
        ;

        mockMvc.perform(post("/image/sortIndex/" + img2.getId())
                        .param("token", token.getWriteToken())
                        .param("op", "plus")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;

        mockMvc.perform(get("/image/list/" + token.getReadToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.images[0].id").value(img0.getId().intValue()))
                .andExpect(jsonPath("$.images[1].id").value(img1.getId().intValue()))
                .andExpect(jsonPath("$.images[2].id").value(img3.getId().intValue()))
                .andExpect(jsonPath("$.images[3].id").value(img4.getId().intValue()))
                .andExpect(jsonPath("$.images[4].id").value(img2.getId().intValue()))
                .andExpect(jsonPath("$.images[5].id").value(img5.getId().intValue()))
                .andExpect(jsonPath("$.images[0].sortIndex").value(1))
                .andExpect(jsonPath("$.images[1].sortIndex").value(2))
                .andExpect(jsonPath("$.images[2].sortIndex").value(3))
                .andExpect(jsonPath("$.images[3].sortIndex").value(4))
                .andExpect(jsonPath("$.images[4].sortIndex").value(5))
                .andExpect(jsonPath("$.images[5].sortIndex").value(6))
        ;
        mockMvc.perform(post("/image/sortIndex/" + img2.getId())
                        .param("token", token.getWriteToken())
                        .param("op", "plus")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;

        mockMvc.perform(get("/image/list/" + token.getReadToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.images[0].id").value(img0.getId().intValue()))
                .andExpect(jsonPath("$.images[1].id").value(img1.getId().intValue()))
                .andExpect(jsonPath("$.images[2].id").value(img3.getId().intValue()))
                .andExpect(jsonPath("$.images[3].id").value(img4.getId().intValue()))
                .andExpect(jsonPath("$.images[4].id").value(img5.getId().intValue()))
                .andExpect(jsonPath("$.images[5].id").value(img2.getId().intValue()))
                .andExpect(jsonPath("$.images[0].sortIndex").value(1))
                .andExpect(jsonPath("$.images[1].sortIndex").value(2))
                .andExpect(jsonPath("$.images[2].sortIndex").value(3))
                .andExpect(jsonPath("$.images[3].sortIndex").value(4))
                .andExpect(jsonPath("$.images[4].sortIndex").value(5))
                .andExpect(jsonPath("$.images[5].sortIndex").value(6))
        ;
        mockMvc.perform(post("/image/sortIndex/" + img2.getId())
                        .param("token", token.getWriteToken())
                        .param("op", "plus")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;

        mockMvc.perform(get("/image/list/" + token.getReadToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.images[0].id").value(img0.getId().intValue()))
                .andExpect(jsonPath("$.images[1].id").value(img1.getId().intValue()))
                .andExpect(jsonPath("$.images[2].id").value(img3.getId().intValue()))
                .andExpect(jsonPath("$.images[3].id").value(img4.getId().intValue()))
                .andExpect(jsonPath("$.images[4].id").value(img5.getId().intValue()))
                .andExpect(jsonPath("$.images[5].id").value(img2.getId().intValue()))
                .andExpect(jsonPath("$.images[0].sortIndex").value(1))
                .andExpect(jsonPath("$.images[1].sortIndex").value(2))
                .andExpect(jsonPath("$.images[2].sortIndex").value(3))
                .andExpect(jsonPath("$.images[3].sortIndex").value(4))
                .andExpect(jsonPath("$.images[4].sortIndex").value(5))
                .andExpect(jsonPath("$.images[5].sortIndex").value(6))
        ;
    }


    @Test
    public void testImagesSortIndexMinus() throws Exception {
        Token token = tokenService.addToken(siteName, ownerName, ++ownerId, ip);

        upload6Files(token.getWriteToken());
        Image img0 = imageReadService.getImages(token).get(0);
        Image img1 = imageReadService.getImages(token).get(1);
        Image img2 = imageReadService.getImages(token).get(2);
        Image img3 = imageReadService.getImages(token).get(3);
        Image img4 = imageReadService.getImages(token).get(4);
        Image img5 = imageReadService.getImages(token).get(5);

        mockMvc.perform(get("/image/list/" + token.getReadToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.images[0].id").value(img0.getId().intValue()))
                .andExpect(jsonPath("$.images[1].id").value(img1.getId().intValue()))
                .andExpect(jsonPath("$.images[2].id").value(img2.getId().intValue()))
                .andExpect(jsonPath("$.images[3].id").value(img3.getId().intValue()))
                .andExpect(jsonPath("$.images[4].id").value(img4.getId().intValue()))
                .andExpect(jsonPath("$.images[5].id").value(img5.getId().intValue()))
                .andExpect(jsonPath("$.images[0].sortIndex").value(1))
                .andExpect(jsonPath("$.images[1].sortIndex").value(2))
                .andExpect(jsonPath("$.images[2].sortIndex").value(3))
                .andExpect(jsonPath("$.images[3].sortIndex").value(4))
                .andExpect(jsonPath("$.images[4].sortIndex").value(5))
                .andExpect(jsonPath("$.images[5].sortIndex").value(6))
        ;


        mockMvc.perform(post("/image/sortIndex/" + img3.getId())
                        .param("token", token.getWriteToken())
                        .param("op", "minus")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;
        mockMvc.perform(get("/image/list/" + token.getReadToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.images[0].id").value(img0.getId().intValue()))
                .andExpect(jsonPath("$.images[1].id").value(img1.getId().intValue()))
                .andExpect(jsonPath("$.images[2].id").value(img3.getId().intValue()))
                .andExpect(jsonPath("$.images[3].id").value(img2.getId().intValue()))
                .andExpect(jsonPath("$.images[4].id").value(img4.getId().intValue()))
                .andExpect(jsonPath("$.images[5].id").value(img5.getId().intValue()))
                .andExpect(jsonPath("$.images[0].sortIndex").value(1))
                .andExpect(jsonPath("$.images[1].sortIndex").value(2))
                .andExpect(jsonPath("$.images[2].sortIndex").value(3))
                .andExpect(jsonPath("$.images[3].sortIndex").value(4))
                .andExpect(jsonPath("$.images[4].sortIndex").value(5))
                .andExpect(jsonPath("$.images[5].sortIndex").value(6))
        ;
        mockMvc.perform(post("/image/sortIndex/" + img3.getId())
                        .param("token", token.getWriteToken())
                        .param("op", "minus")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;
        mockMvc.perform(get("/image/list/" + token.getReadToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.images[0].id").value(img0.getId().intValue()))
                .andExpect(jsonPath("$.images[1].id").value(img3.getId().intValue()))
                .andExpect(jsonPath("$.images[2].id").value(img1.getId().intValue()))
                .andExpect(jsonPath("$.images[3].id").value(img2.getId().intValue()))
                .andExpect(jsonPath("$.images[4].id").value(img4.getId().intValue()))
                .andExpect(jsonPath("$.images[5].id").value(img5.getId().intValue()))
                .andExpect(jsonPath("$.images[0].sortIndex").value(1))
                .andExpect(jsonPath("$.images[1].sortIndex").value(2))
                .andExpect(jsonPath("$.images[2].sortIndex").value(3))
                .andExpect(jsonPath("$.images[3].sortIndex").value(4))
                .andExpect(jsonPath("$.images[4].sortIndex").value(5))
                .andExpect(jsonPath("$.images[5].sortIndex").value(6))
        ;
        mockMvc.perform(post("/image/sortIndex/" + img3.getId())
                        .param("token", token.getWriteToken())
                        .param("op", "minus")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;
        mockMvc.perform(get("/image/list/" + token.getReadToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.images[0].id").value(img3.getId().intValue()))
                .andExpect(jsonPath("$.images[1].id").value(img0.getId().intValue()))
                .andExpect(jsonPath("$.images[2].id").value(img1.getId().intValue()))
                .andExpect(jsonPath("$.images[3].id").value(img2.getId().intValue()))
                .andExpect(jsonPath("$.images[4].id").value(img4.getId().intValue()))
                .andExpect(jsonPath("$.images[5].id").value(img5.getId().intValue()))
                .andExpect(jsonPath("$.images[0].sortIndex").value(1))
                .andExpect(jsonPath("$.images[1].sortIndex").value(2))
                .andExpect(jsonPath("$.images[2].sortIndex").value(3))
                .andExpect(jsonPath("$.images[3].sortIndex").value(4))
                .andExpect(jsonPath("$.images[4].sortIndex").value(5))
                .andExpect(jsonPath("$.images[5].sortIndex").value(6))
        ;
        mockMvc.perform(post("/image/sortIndex/" + img3.getId())
                        .param("token", token.getWriteToken())
                        .param("op", "minus")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;
        mockMvc.perform(get("/image/list/" + token.getReadToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.images[0].id").value(img3.getId().intValue()))
                .andExpect(jsonPath("$.images[1].id").value(img0.getId().intValue()))
                .andExpect(jsonPath("$.images[2].id").value(img1.getId().intValue()))
                .andExpect(jsonPath("$.images[3].id").value(img2.getId().intValue()))
                .andExpect(jsonPath("$.images[4].id").value(img4.getId().intValue()))
                .andExpect(jsonPath("$.images[5].id").value(img5.getId().intValue()))
                .andExpect(jsonPath("$.images[0].sortIndex").value(1))
                .andExpect(jsonPath("$.images[1].sortIndex").value(2))
                .andExpect(jsonPath("$.images[2].sortIndex").value(3))
                .andExpect(jsonPath("$.images[3].sortIndex").value(4))
                .andExpect(jsonPath("$.images[4].sortIndex").value(5))
                .andExpect(jsonPath("$.images[5].sortIndex").value(6))
        ;
    }
}