package net.lafox.io.dao;

import net.lafox.io.entity.Image;
import net.lafox.io.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Alice Lafox <alice@lafox.net> on 23.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

public interface ImageDao extends JpaRepository<Image, Long> {
    List<Image> findByTokenOrderBySortIndex(Token token);

    @Query("select i from Image i join i.token t where t.roToken = :roToken order by i.sortIndex desc")
    List<Image> getImagesByRoToken(@Param(value = "roToken") String roToken);


//    Image findByTokenAndIsAvatarIsTrue(Token token); //TODo
}
