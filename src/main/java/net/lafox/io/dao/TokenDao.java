package net.lafox.io.dao;

import net.lafox.io.entity.Token;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Alice Lafox <alice@lafox.net> on 22.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

public interface TokenDao {
    @Select("SELECT * FROM token WHERE site_name = #{siteName} and owner_name=#{ownerName} and owner_id=#{ownerId}")
    Token findBySiteNameAndOwnerNameAndOwnerId(@Param("siteName") String siteName,
                                               @Param("ownerName") String ownerName,
                                               @Param("ownerId") Long ownerId);

    @Select("SELECT * FROM token WHERE r_roken = #{rToken}")
    Token findByRwToken(@Param ("rToken") String rToken);

    @Insert("Insert into token (site_name, owner_name, owner_id, ip, write_token, read_token) " +
            "values(#{siteName},#{ownerName}, #{ownerId}, #{ip}, #{writeToken}, #{readToken})")
    @Options(useGeneratedKeys = true, keyProperty = "id", flushCache=true)
    void insert(Token token);
}
