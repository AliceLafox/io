package net.lafox.io.service;

import net.lafox.io.dao.ImageDao;
import net.lafox.io.entity.Image;
import net.lafox.io.entity.Token;
import net.lafox.io.exceptions.RollBackException;
import net.lafox.io.utils.ImgUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alice Lafox <alice@lafox.net> on 23.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

@Service
@Transactional(rollbackFor = RollBackException.class)
public class ImageServiceImpl implements ImageService {
    @Value("${upload.dir}")
    private String UPLOAD_DIR;


    @Autowired
    ImageDao imageDao;
    @Autowired
    TokenService tokenService;

    @Override
    public void updateImage(Long id, String token, MultipartFile mpf) throws RollBackException {
        tokenService.checkRwToken(token);

        Image image = this.getImage(id, token);
        image.setVersion(image.getVersion()+1);
        image.setContentType(mpf.getContentType());
        image.setFileName(mpf.getOriginalFilename());
        image.setSize(mpf.getSize());
        imageDao.save(image);

        try {
            mpf.transferTo(new File(imagePath(image)));
        } catch (IOException e) {
            throw new RollBackException(e);
        }
    }

    @Override
    public String imagePath(Image image) {
        String ver=image.getVersion()==0?"":"_"+image.getVersion();
        return UPLOAD_DIR + "/" + image.getToken().getSiteName() + "/" + image.getId() + ver + ".jpg";
    }

    @Override
    public List<Image> getImages(Token token) {
        return imageDao.findByTokenOrderBySortIndex(token);
    }

    @Override
    public Image getImage(Long id, String token) throws RollBackException {
        if (token == null) throw new RollBackException("token is NULL for image id="+id);
        if (token.isEmpty()) throw new RollBackException("token is EMPTY for image id="+id);

        Image image = imageDao.findOne(id);

        if (image == null) throw new RollBackException("no image found with id="+id);
        if (!token.equals(image.getToken().getRwToken())) throw new RollBackException("incorrect token: "+token +" for image id="+id);

        return image;
    }

    @Override
    public Long addImage(String token, MultipartFile mpf) throws RollBackException {

        Token checkedToken = tokenService.checkRwToken(token);

        Image image = new Image(checkedToken,mpf.getContentType(),mpf.getOriginalFilename(),mpf.getSize());
        String filename = imagePath(image);

        try {
            mpf.transferTo(new File(filename));

            Dimension dim = ImgUtils.imgDimension(mpf.getBytes());

            image.setWidth(dim.width);
            image.setHeight(dim.height);
            image.setContentType(ImgUtils.getContentType(filename));

            image.setActive(true);
            imageDao.save(image);

        } catch (IOException e) {
            throw new RollBackException(e);
        }
        return image.getId();
    }

    @Override
    public void deleteImage(Long id, String token) throws RollBackException {
        Image image=getImage(id, token);
        image.setActive(false);
        imageDao.save(image);
    }

    @Override
    public void setAvatar(Long id, String rwToken) throws RollBackException {

        if (id==null) throw new RollBackException("id can not be null");
        Token token = tokenService.checkRwToken(rwToken);
        for (Image image : this.getImages(token)) {
            if (!image.isAvatar()) {
                if (id.equals(image.getId())) {
                    image.setAvatar(true);
                    imageDao.save(image);
                }
            } else{
                if (!id.equals(image.getId())) {
                    image.setAvatar(false);
                    imageDao.save(image);
                }
            }
        }
    }

    @Override
    public void getImagesByRoToken(String roToken, Map<String, Object> map) throws RollBackException {

        try {
            map.put("images", new ArrayList<>());
            map.put("imagesDeleted", new ArrayList<>());
            map.put("avatar", null);

            for (Image image : imageDao.getImagesByRoToken(roToken)) {
                if (image.isActive()) {
                    ((List) map.get("images")).add(image.asDto());
                } else {
                    ((List) map.get("imagesDeleted")).add(image.asDto());
                }
                if (image.isAvatar()) map.put("avatar", image.asDto());

            }

        } catch (Exception e) {
            throw new RollBackException(e);
        }

    }

    @Override
    public void setTitle(Long id, String rwToken, String title) throws RollBackException {
        if (title == null || title.isEmpty()) return;
        if (id == null) throw new RollBackException("id can not be null");
        tokenService.checkRwToken(rwToken);

        Image image = getImage(id, rwToken);
        image.setTitle(title);
        imageDao.save(image);
    }

    @Override
    public void setDescription(Long id, String rwToken, String description) throws RollBackException {
        if (description == null || description.isEmpty()) return;
        if (id == null) throw new RollBackException("id can not be null");
        tokenService.checkRwToken(rwToken);

        Image image = getImage(id, rwToken);
        image.setDescription(description);
        imageDao.save(image);
    }

}
