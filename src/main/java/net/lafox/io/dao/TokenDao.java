package net.lafox.io.dao;

import net.lafox.io.entity.Token;
import org.apache.ibatis.annotations.*;

/**
 * Created by Alice Lafox <alice@lafox.net> on 22.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

public interface TokenDao {
    @Select("SELECT * FROM token WHERE site_name = #{siteName} and owner_name=#{ownerName} and owner_id=#{ownerId}")
    Token findBySiteNameAndOwnerNameAndOwnerId(@Param("siteName") String siteName,
                                               @Param("ownerName") String ownerName,
                                               @Param("ownerId") Long ownerId);



    @Insert("Insert into token (site_name, owner_name, owner_id, ip, write_token, read_token) " +
            "values(#{siteName},#{ownerName}, #{ownerId}, #{ip}, #{writeToken}, #{readToken})")
    @Options(useGeneratedKeys = true, keyProperty = "id", flushCache=true)
    void insert(Token token);

    @Select("SELECT * FROM token WHERE id = #{tokenId}")
    Token findByTokenId(@Param ("tokenId") Long tokenId);

    @Select("SELECT * FROM token WHERE write_token = #{writeToken}")
    Token findByWriteToken(@Param("writeToken") String writeToken);

    @Select("SELECT * FROM token WHERE read_token = #{readToken}")
    Token findByReadToken(@Param("readToken") String readToken);

    @Delete("DELETE from token where site_name='test-domain'")
    void cleanupAfterTests();
}
