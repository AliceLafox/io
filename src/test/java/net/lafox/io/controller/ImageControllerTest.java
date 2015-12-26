package net.lafox.io.controller;

import net.lafox.io.IoApplication;
import net.lafox.io.entity.Image;
import net.lafox.io.service.ImageService;
import net.lafox.io.service.TokenService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IoApplication.class)
@ActiveProfiles(profiles = {"test"})
@WebAppConfiguration
public class ImageControllerTest {
    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private String siteName ;
    private String ownerName;
    private Long ownerId ;
    private String ip;

    @Autowired
    TokenService tokenService;

    @Autowired
    ImageService imageService;

    @Before
    public void setUp() {

        mockMvc = webAppContextSetup(webApplicationContext).build();
         siteName = "lafox.net";
         ownerName = "item";
         ownerId = 102L;
         ip = "10.10.10.10";
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

    @Test
    public void testImageUpload() throws Exception {

        String token = tokenService.addToken(siteName, ownerName, ownerId, ip);

        MockMultipartFile image1 = new MockMultipartFile("data", "testImage1.jpg", "image/jpg", new FileInputStream(UPLOAD_DIR + "/testImage1.jpg"));
        MockMultipartFile image2 = new MockMultipartFile("data", "testImage2.jpg", "image/jpg", new FileInputStream(UPLOAD_DIR + "/testImage2.jpg"));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image/upload")
                .file(image1)
                .file(image2)
                .param("token", token))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.status").value("OK"))
        ;
    }
    @Test
    public void testImageUpdate() throws Exception {

        String token = tokenService.addToken(siteName, ownerName, ownerId, ip);
        MockMultipartFile image1 = new MockMultipartFile("data", "testImage1.jpg", "image/jpg", new FileInputStream(UPLOAD_DIR + "/testImage1.jpg"));
        MockMultipartFile image2 = new MockMultipartFile("data", "testImage2.jpg", "image/jpg", new FileInputStream(UPLOAD_DIR + "/testImage2.jpg"));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image/upload")
                .file(image1)
                .file(image2)
                .param("token", token))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.status").value("OK"))
        ;

        Image img=imageService.getImages(tokenService.findByToken(token)).get(1);

        File file3=new File(UPLOAD_DIR + "/testImage3.jpg");
        MockMultipartFile image3 = new MockMultipartFile("data", "testImage3.jpg", "image/jpg", new FileInputStream(file3));

        Thread.sleep(100);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image/update/" + img.getId())
                .file(image3)
                .param("token", token))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.status").value("OK"))
        ;
        Image imgNew=imageService.getImage(img.getId(),token);

        Assert.assertEquals(img.getVersion()+1,imgNew.getVersion());
        Assert.assertTrue(img.getModified().compareTo(imgNew.getModified()) == -1);
        Assert.assertEquals(file3.length(), 0+ imgNew.getSize());
        Assert.assertEquals(file3.getName(), imgNew.getFileName());

        File fileOnDisk=new File(UPLOAD_DIR + "/" + tokenService.findByToken(token).getSiteName() + "/" + imgNew.getId());
        Assert.assertEquals(fileOnDisk.length(), 0+imgNew.getSize());

    }


}