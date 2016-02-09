package net.lafox.ioClient;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;

/**
 * Created by Alice Lafox <alice@lafox.net> on 21.01.16
 * Lafox.Net Software developers Team http://dev.lafox.net
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IoClientApplication.class)
@ActiveProfiles(profiles = {"test"})
public class TokenServiceTest extends TestCase {
    private final Random random = new Random();
    @Autowired
    TokenService tokenService;

    @Test
    public void testGetToken() throws Exception {
        String siteName = "test-domain";
        String ownerName = "item";
        Long ownerId = random.nextLong();

       Token token= tokenService.getToken(siteName, ownerName, ownerId);
        Assert.assertNotNull(token.getReadToken());
        Assert.assertNotNull(token.getWriteToken());

    }
}