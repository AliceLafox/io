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
    public List<Image> getImages(Token token) {
        return imageDao.findByTokenOrderBySortIndex(token);
    }


    @Override
    public Long upload(String token, MultipartFile mpf) throws RollBackException {

        Token checkedToken = tokenService.findByToken(token);
        if (checkedToken == null) throw new RollBackException("token not found");

        Image image = new Image(checkedToken,mpf.getOriginalFilename(),mpf.getContentType(),mpf.getSize());
        imageDao.save(image);

        try {
            mpf.transferTo(new File(UPLOAD_DIR + "/" + checkedToken.getSiteName() + "/" + image.getId()));
        } catch (IOException e) {
            throw new RollBackException(e);
        }
        return image.getId();
    }
}
