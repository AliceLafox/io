package net.lafox.io.dao;

import net.lafox.io.entity.Image;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Alice Lafox <alice@lafox.net> on 23.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */


public interface SiteDao {
    @Select("select count(*)>0 from site_ip where site_name=#{siteName} AND network >> #{ip}::inet;")
    boolean checkIp(@Param("siteName") String siteName, @Param("ip") String ip);
}
