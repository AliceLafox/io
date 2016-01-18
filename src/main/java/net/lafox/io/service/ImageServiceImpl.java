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
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageDao imageDao;
    @Autowired
    TokenService tokenService;

    @Override
    public void updateImage(Long id, String writeToken, MultipartFile mpf) throws RollBackException {
        tokenService.checkWriteToken(writeToken);

        try {
            Image image = this.getImageCheckByWriteToken(id, writeToken);
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
    public List<Image> getImages(Token token) {
        return imageDao.findByTokenId(token.getId());
    }

    @Override
    public Image getImageCheckByWriteToken(Long id, String writeToken) throws RollBackException {
        if (writeToken == null) throw new RollBackException("token is NULL for image id=" + id);
        if (writeToken.isEmpty()) throw new RollBackException("token is EMPTY for image id=" + id);

        Image image = imageDao.findOne(id);

        if (image == null) throw new RollBackException("no image found with id="+id);

        Token token = tokenService.findByTokenId(image.getTokenId());

        if (!writeToken.equals(token.getWriteToken())) throw new RollBackException("incorrect token: " + writeToken + " for image id=" + id);

        return image;
    }

    @Override
    public Image getImage(Long id) {
        if (id == null || id < 1) return null;
        return imageDao.findOne(id);
    }

    @Override
    public byte[] getImageContent(Long id) {
        return (byte[]) imageDao.getImageContent(id);
    }

    private String contentType(MultipartFile mpf){

        return "image/jpg".equals(mpf.getContentType())?"image/jpeg": mpf.getContentType();
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
    public void deleteImage(Long id, String writeToken) throws RollBackException {
        Image image= getImageCheckByWriteToken(id, writeToken);
        imageDao.delete(image.getId());
    }

    @Override
    public void setAvatar(Long id, String writeToken) throws RollBackException {

        if (id==null) throw new RollBackException("id can not be null");
        tokenService.checkWriteToken(writeToken);

        imageDao.avatar(id);
    }

    @Override
    public void getImagesByReadToken(String readToken, Map<String, Object> map) throws RollBackException {

        try {
            map.put("images", new ArrayList<>());
            map.put("imagesDeleted", new ArrayList<>());
            map.put("avatar", null);

            Token token=tokenService.findByReadToken(readToken);

            for (Image image : imageDao.findByTokenId(token.getId())) {
                if (image.isActive()) {
                    ((List) map.get("images")).add(image);
                } else {
                    ((List) map.get("imagesDeleted")).add(image);
                }
                if (image.isAvatar()) map.put("avatar", image);

            }

        } catch (Exception e) {
            throw new RollBackException(e);
        }

    }

    @Override
    public void setTitle(Long id, String writeToken, String title) throws RollBackException {
        if (title == null || title.isEmpty()) return;
        if (id == null) throw new RollBackException("id can not be null");
        tokenService.checkWriteToken(writeToken);

        getImageCheckByWriteToken(id, writeToken);
        imageDao.title(id, title);
    }

    @Override
    public void setDescription(Long id, String rwToken, String description) throws RollBackException {
        if (description == null || description.isEmpty()) return;
        if (id == null) throw new RollBackException("id can not be null");
        tokenService.checkWriteToken(rwToken);

        getImageCheckByWriteToken(id, rwToken);
        imageDao.description(id, description);
    }

}
