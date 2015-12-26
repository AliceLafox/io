package net.lafox.io.service;


import net.lafox.io.entity.Image;
import net.lafox.io.entity.Token;
import net.lafox.io.exceptions.RollBackException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Alice Lafox <alice@lafox.net> on 23.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

public interface ImageService {
    List<Image> getImages(Token token);

    Image getImage(Long id, String token) throws RollBackException;

    Long addImage(String token, MultipartFile mpf) throws RollBackException;
    void updateImage(Long id, String token, MultipartFile mpf) throws RollBackException;

}
