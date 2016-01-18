package net.lafox.io.service;

import net.bull.javamelody.MonitoredWithSpring;
import net.lafox.io.dao.ImageDao;
import net.lafox.io.entity.Image;
import net.lafox.io.entity.Token;
import net.lafox.io.exceptions.RollBackException;
import net.lafox.io.utils.ImgUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alice Lafox <alice@lafox.net> on 23.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

@Service
@MonitoredWithSpring
@Transactional(rollbackFor = RollBackException.class)
public class ImageWriteServiceImpl implements ImageWriteService {

    @Autowired
    ImageDao imageDao;
    @Autowired
    TokenService tokenService;

    private String contentType(MultipartFile mpf){
        return "image/jpg".equals(mpf.getContentType())?"image/jpeg": mpf.getContentType();
    }

    @Override
    public void checkImagePermissionByImageIdAndWriteToken(Long id, String writeToken) throws RollBackException {
        if (id == null) throw new RollBackException("image_id is NULL");
        if (id < 1) throw new RollBackException("image_id (" + id + ") is less that 1");
        if (writeToken == null) throw new RollBackException("token is NULL for image id=" + id);
        if (writeToken.isEmpty()) throw new RollBackException("token is EMPTY for image id=" + id);
        if (imageDao.countByImageIdAndWriteToken(id, writeToken) <1) throw new RollBackException("no image found with image_id=" + id + " and writeToken=" + writeToken);
    }

    @Override
    public Long addImage(String token, MultipartFile mpf) throws RollBackException {
        Token checkedToken = tokenService.checkWriteToken(token);
        try {
            Image image = new Image(checkedToken.getId(),contentType(mpf), mpf.getOriginalFilename(), mpf.getSize());
            Dimension dim = ImgUtils.imgDimension(mpf.getBytes());
            image.setWidth(dim.width);
            image.setHeight(dim.height);
            image.setContent(mpf.getBytes());
            imageDao.insert(image);
            return image.getId();
        } catch (IOException e) {
            throw new RollBackException(e);
        }
    }

    @Override
    public void updateImage(Long id, String writeToken, MultipartFile mpf) throws RollBackException {
        checkImagePermissionByImageIdAndWriteToken(id, writeToken);
        try {
            Image image = imageDao.findOne(id);
            image.setContentType(contentType(mpf));
            image.setFileName(mpf.getOriginalFilename());
            image.setSize(mpf.getSize());

            Dimension dim = ImgUtils.imgDimension(mpf.getBytes());
            image.setWidth(dim.width);
            image.setHeight(dim.height);
            image.setContent(mpf.getBytes());

            imageDao.update(image);
        } catch (IOException e) {
            throw new RollBackException(e);
        }
    }

    @Override
    public void deleteImage(Long id, String writeToken) throws RollBackException {
        checkImagePermissionByImageIdAndWriteToken(id, writeToken);
        imageDao.delete(id);
    }

    @Override
    public void setAvatar(Long id, String writeToken) throws RollBackException {
        checkImagePermissionByImageIdAndWriteToken(id, writeToken);
        imageDao.avatar(id);
    }

    @Override
    public void setTitle(Long id, String writeToken, String title) throws RollBackException {
        checkImagePermissionByImageIdAndWriteToken(id, writeToken);
        imageDao.title(id, title);
    }

    @Override
    public void setDescription(Long id, String rwToken, String description) throws RollBackException {
        checkImagePermissionByImageIdAndWriteToken(id, rwToken);
        imageDao.description(id, description);
    }

}
