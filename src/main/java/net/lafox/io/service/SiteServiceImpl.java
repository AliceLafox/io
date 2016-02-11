package net.lafox.io.service;

import net.bull.javamelody.MonitoredWithSpring;
import net.lafox.io.dao.SiteDao;
import net.lafox.io.exceptions.RollBackException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by tsyma on 1/21/16.
 */
@Service
@MonitoredWithSpring
@Transactional(rollbackFor = RollBackException.class)
public class SiteServiceImpl implements SiteService {
    @Autowired
    SiteDao siteDao;
    @Override
    public boolean checkIp(String siteName, String ip) {
        if (siteName==null||siteName.trim().isEmpty()||ip==null||ip.trim().isEmpty()) {
            return false;
        } else {
            try {
                return siteDao.checkIp(siteName, ip);
            }catch (Exception e){
                return false;
            }
        }
    }
}
