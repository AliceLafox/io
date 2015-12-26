package net.lafox.io.service;

import net.lafox.io.dao.ImageDao;
import net.lafox.io.entity.Image;
import net.lafox.io.entity.Token;
import net.lafox.io.exceptions.RollBackException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
        Image image = this.getImage(id, token);
        image.setVersion(image.getVersion()+1);
        image.setContentType(mpf.getContentType());
        image.setFileName(mpf.getOriginalFilename());
        image.setSize(mpf.getSize());
        imageDao.save(image);

        Token checkedToken = tokenService.checkToken(token);
        try {
            mpf.transferTo(new File(UPLOAD_DIR + "/" + checkedToken.getSiteName() + "/" + image.getId()));
        } catch (IOException e) {
            throw new RollBackException(e);
        }
    }

    @Override
    public List<Image> getImages(Token token) {
        return imageDao.findByTokenOrderBySortIndex(token);
    }

    @Override
    public Image getImage(Long id, String token) throws RollBackException {
        if (token == null) throw new RollBackException("token is NULL for image id="+id);
        if (token.isEmpty()) throw new RollBackException("toke is EMPTY for image id="+id);

        Image image = imageDao.findOne(id);

        if (image == null) throw new RollBackException("no image found with id="+id);
        if (!token.equals(image.getToken().getToken())) throw new RollBackException("incorrect token: "+token +" for image id="+id);

        return image;
    }

    @Override
    public Long addImage(String token, MultipartFile mpf) throws RollBackException {

        Token checkedToken = tokenService.checkToken(token);

        Image image = new Image(checkedToken,mpf.getContentType(),mpf.getOriginalFilename(),mpf.getSize());
        imageDao.save(image);

        try {
            mpf.transferTo(new File(UPLOAD_DIR + "/" + checkedToken.getSiteName() + "/" + image.getId()));
        } catch (IOException e) {
            throw new RollBackException(e);
        }
        return image.getId();
    }
}
