package net.lafox.io.dao;

import net.lafox.io.entity.Image;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Alice Lafox <alice@lafox.net> on 23.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */



public interface ImageDao {
    String SELECT_FIELDS=" id, sort_index, version, width, height, created, modified, content_type, file_name, title, description, size, active, avatar, token_id ";
    String UPDATE_FIELDS=" " +
            "version = version+1, " +
            "modified = NOW(), " +
            "width = #{width}, " +
            "height = #{height}, " +
            "content_type = #{contentType}, " +
            "file_name = #{fileName}, " +
            "size = #{size}, " +
            "content = #{content}";
    String INSERT_FIELDS=" (width, height, content_type, file_name, size, token_id, content) " +
            "VALUES (#{width}, #{height}, #{contentType}, #{fileName}, #{size}, #{tokenId}, #{content}) ";

    @Select("SELECT " + SELECT_FIELDS + " FROM image WHERE token_id=#{tokenId} order by sort_index ASC")
    List<Image> findByTokenId(@Param("tokenId") Long tokenId);


    @Select("SELECT " + SELECT_FIELDS + "FROM image WHERE id=#{id}")
    Image findOne(@Param("id")Long id);

    @Select("SELECT content FROM image WHERE id=#{id}")
    Object getImageContent(@Param("id")Long id);

    @Update("UPDATE image SET "+UPDATE_FIELDS + "WHERE id=#{id}")
    void update(Image image);

    @Insert("INSERT into image "+INSERT_FIELDS)
    @Options(useGeneratedKeys = true, keyProperty = "id", flushCache=true)
    void insert(Image image);

    @Update("UPDATE image SET active = false WHERE id=#{id}")
    void delete(@Param("id")Long id);

    @Update("select setAvatar(#{id})")
    void avatar(@Param("id")Long id);

    @Update("UPDATE image SET title = #{title} WHERE id=#{id}")
    void title(@Param("id")Long id, @Param("title") String title);

    @Update("UPDATE image SET description = #{description} WHERE id=#{id}")
    void description(@Param("id")Long id, @Param("description") String description);

}
