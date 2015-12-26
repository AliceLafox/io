package net.lafox.io.dao;

import net.lafox.io.entity.Image;
import net.lafox.io.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Alice Lafox <alice@lafox.net> on 23.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

public interface ImageDao extends JpaRepository<Image, Long> {
    List<Image> findByTokenOrderBySortIndex(Token token);

//    Image findByTokenAndIsAvatarIsTrue(Token token); //TODo
}
