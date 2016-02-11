package net.lafox.io.service;

import junit.framework.TestCase;
import net.lafox.io.IoApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by tsyma on 1/21/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IoApplication.class)
@ActiveProfiles(profiles = {"test"})

public class SiteServiceImplTest extends TestCase {

    @Autowired
    SiteService siteService;
    @Test
    public void testCheckIp() throws Exception {
        Assert.assertEquals(false, siteService.checkIp("",""));
        Assert.assertEquals(false, siteService.checkIp("rrrrrrr","ddddddddd"));
        Assert.assertEquals(false, siteService.checkIp("",null));
        Assert.assertEquals(false, siteService.checkIp(null,""));
        Assert.assertEquals(false, siteService.checkIp(null,null));
        Assert.assertEquals(true, siteService.checkIp("test-domain","127.0.0.1"));
        Assert.assertEquals(false, siteService.checkIp("test-domain","128.0.0.1"));
        Assert.assertEquals(true, siteService.checkIp("test-domain","10.10.10.10"));
        Assert.assertEquals(false, siteService.checkIp("test-domain","11.10.10.10"));


    }
}