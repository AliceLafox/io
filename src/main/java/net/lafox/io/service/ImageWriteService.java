package net.lafox.io.service;


import net.lafox.io.entity.Image;
import net.lafox.io.entity.Token;
import net.lafox.io.exceptions.RollBackException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by Alice Lafox <alice@lafox.net> on 18.01.16
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

public interface ImageWriteService {

    void checkImagePermissionByImageIdAndWriteToken(String id, String token) throws RollBackException;

    String addImage(String writeToken, MultipartFile mpf) throws RollBackException;

    void updateImage(String id, String writeToken, MultipartFile mpf) throws RollBackException;

    void deleteImage(String id, String writeToken) throws RollBackException;

    void setAvatar(String id, String writeToken) throws RollBackException;

    void setTitle(String id, String writeToken, String title) throws RollBackException;

    void setDescription(String id, String writeToken, String description) throws RollBackException;

    void sortIndexPlus(String id, String writeToken) throws RollBackException ;
    void sortIndexMinus(String id, String writeToken) throws RollBackException ;
    void sortIndexToFirst(String id, String writeToken) throws RollBackException ;
    void sortIndexToLast(String id, String writeToken) throws RollBackException ;
}
