package net.lafox.io.service;


import net.lafox.io.entity.Image;
import net.lafox.io.entity.Token;
import net.lafox.io.exceptions.RollBackException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by Alice Lafox <alice@lafox.net> on 23.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

public interface ImageReadService {

    List<Image> getImages(Token token);
    List<Image> getImagesByReadToken(String readToken) throws RollBackException;
    Image getAvatarByReadToken(String readToken) throws RollBackException;
    Image findOne(String id);
    byte[] getImageContent(String id);

}
