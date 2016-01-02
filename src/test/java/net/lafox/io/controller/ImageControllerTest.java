package net.lafox.io.controller;

import net.lafox.io.IoApplication;
import net.lafox.io.entity.Image;
import net.lafox.io.entity.Token;
import net.lafox.io.service.ImageService;
import net.lafox.io.service.TokenService;
import net.lafox.io.utils.ImgUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.After;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private static Long ownerId = null;
    private String ip;
    File workingDir;


    @Autowired
    TokenService tokenService;

    @Autowired
    ImageService imageService;

    @Before
    public void setUp() {

        mockMvc = webAppContextSetup(webApplicationContext).build();
         siteName = "test-domain";
         ownerName = "item";
        ownerId = (ownerId == null) ? 102L : ownerId++;
         ip = "10.10.10.10";

        workingDir=new File(UPLOAD_DIR+"/"+siteName);
        if (!workingDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            workingDir.mkdir();
        }

    }

    @After
    public void tearDown() throws Exception {
        FileUtils.cleanDirectory(workingDir);
    }


    @Test
    public void testGetOneImageBody() throws Exception {
        Token token = tokenService.addToken(siteName, ownerName, ++ownerId, ip);
        upload2Files(token.getRwToken());
        Image img = imageService.getImages(tokenService.findByRwToken(token.getRwToken())).get(1);

//        "{id:\\d+}-w{w:\\d+}-h{h:\\d+}-o{op:[wheco]}-q{quality:\\d+}-v{ver:\\d}.{ext:png|jpg|gif}"

        mockMvc.perform(get("/" + img.getId() + "-w100-h100-oo-q50-v222.png"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(content().bytes(Files.readAllBytes(new File(imageService.imagePath(img)).toPath())))
        ;

        int w = 100;
        int h = 120;
        MvcResult result = mockMvc.perform(get("/" + img.getId() + "-w" + w + "-h" + h + "-oc-q50-v222.png"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andReturn();

        Dimension dim = ImgUtils.imgDimension(result.getResponse().getContentAsByteArray());

        Assert.assertEquals(dim.getWidth(), w, 0.1);
        Assert.assertEquals(dim.getHeight(), h, 0.1);

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
    public void testGetImages() throws Exception {
        Token token = tokenService.addToken(siteName, ownerName, ++ownerId, ip);
        upload2Files(token.getRwToken());

        mockMvc.perform(get("/image/list/" + token.getRoToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andDo(MockMvcResultHandlers.print())
        ;
        Image img=imageService.getImages(tokenService.findByRwToken(token.getRwToken())).get(1);

        mockMvc.perform(delete("/image/delete/" + img.getId())
                .param("token", img.getToken().getRwToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;

        mockMvc.perform(get("/image/list/" + token.getRoToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.imagesDeleted").isNotEmpty())
                .andDo(MockMvcResultHandlers.print())
        ;

    }

    @Test
    public void testImageUpload() throws Exception {

        Token token = tokenService.addToken(siteName, ownerName, ++ownerId, ip);
        upload2Files(token.getRwToken());

    }

    @Test
    public void testImageUpdate() throws Exception {

        Token token = tokenService.addToken(siteName, ownerName, ++ownerId, ip);
        upload2Files(token.getRwToken());

        Image img=imageService.getImages(tokenService.findByRwToken(token.getRwToken())).get(1);

        File file3=new File(UPLOAD_DIR + "/testImage3.jpg");
        MockMultipartFile image3 = new MockMultipartFile("data", "testImage3.jpg", "image/jpg", new FileInputStream(file3));

        Thread.sleep(100);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image/update/" + img.getId())
                .file(image3)
                .param("token", token.getRwToken()))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.status").value("OK"))
        ;
        Image imgNew=imageService.getImage(img.getId(),token.getRwToken());

        Assert.assertEquals(img.getVersion()+1,imgNew.getVersion());
        Assert.assertTrue(img.getModified().compareTo(imgNew.getModified()) == -1);
        Assert.assertEquals(file3.length(), imgNew.getSize().longValue());
        Assert.assertEquals(file3.getName(), imgNew.getFileName());

        File fileOnDisk=new File(imageService.imagePath(imgNew));
        Assert.assertEquals(fileOnDisk.length(), imgNew.getSize().longValue());

    }

    private void upload2Files(String rwToken) throws Exception {
        MockMultipartFile image1 = new MockMultipartFile("data", "testImage1.jpg", "image/jpg", new FileInputStream(UPLOAD_DIR + "/testImage1.jpg"));
        MockMultipartFile image2 = new MockMultipartFile("data", "testImage2.jpg", "image/jpg", new FileInputStream(UPLOAD_DIR + "/testImage2.jpg"));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/image/upload")
                .file(image1)
                .file(image2)
                .param("token", rwToken))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.status").value("OK"))
        ;

        for (Image img : imageService.getImages(tokenService.findByRwToken(rwToken))) {
            File fileOnDisk = new File(imageService.imagePath(img));
            Assert.assertEquals(img.getSize().longValue(), fileOnDisk.length());
        }
    }

    @Test
    public void testImageDelete() throws Exception {
        Token token = tokenService.addToken(siteName, ownerName, ++ownerId, ip);
        upload2Files(token.getRwToken());
        Image img=imageService.getImages(tokenService.findByRwToken(token.getRwToken())).get(1);

        mockMvc.perform(delete("/image/delete/" + img.getId())
                .param("token", img.getToken().getRwToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;

        Image imgNew=imageService.getImage(img.getId(),token.getRwToken());

        Assert.assertTrue(img.isActive());
        Assert.assertFalse(imgNew.isActive());
    }


    @Test
    public void testSetAvatarImage() throws Exception {
        Token token = tokenService.addToken(siteName, ownerName, ++ownerId, ip);
        upload2Files(token.getRwToken());
        Image img=imageService.getImages(tokenService.findByRwToken(token.getRwToken())).get(1);

        mockMvc.perform(post("/image/avatar/" + img.getId())
                .param("token", img.getToken().getRwToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;

        Image imgNew=imageService.getImage(img.getId(),token.getRwToken());

        Assert.assertFalse(img.isAvatar());
        Assert.assertTrue(imgNew.isAvatar());


    }

    @Test
    public void testSetTitle() throws Exception {
        String title="this is the title of image";
        Token token = tokenService.addToken(siteName, ownerName, ++ownerId, ip);
        upload2Files(token.getRwToken());
        Image img=imageService.getImages(tokenService.findByRwToken(token.getRwToken())).get(1);

        mockMvc.perform(post("/image/title/" + img.getId())
                .param("token", img.getToken().getRwToken())
                .param("title", title)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;

        Image imgNew=imageService.getImage(img.getId(),token.getRwToken());

        Assert.assertNull(img.getTitle());
        Assert.assertTrue(imgNew.getTitle().equals(title));


    }

    @Test
    public void testSetDescription() throws Exception {
        String description="this is the description of image";
        Token token = tokenService.addToken(siteName, ownerName, ++ownerId, ip);
        upload2Files(token.getRwToken());
        Image img=imageService.getImages(tokenService.findByRwToken(token.getRwToken())).get(1);

        mockMvc.perform(post("/image/description/" + img.getId())
                .param("token", img.getToken().getRwToken())
                .param("description", description)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
        ;

        Image imgNew=imageService.getImage(img.getId(),token.getRwToken());

        Assert.assertNull(img.getDescription());
        Assert.assertTrue(imgNew.getDescription().equals(description));


    }
}