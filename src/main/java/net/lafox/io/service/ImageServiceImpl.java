package net.lafox.io.service;

import net.lafox.io.dao.ImageDao;
import net.lafox.io.entity.Image;
import net.lafox.io.entity.Token;
import net.lafox.io.exceptions.EmptyFieldException;
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
@Transactional
public class ImageServiceImpl implements ImageService {
    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Autowired
    ImageDao imageDao;
    @Autowired
    TokenService tokenService;

    @Override
    public List<Image> findByToken(Token token) {
        return imageDao.findByToken(token);
    }

    @Override
    public List<Image> upload(String token, MultipartFile mpf) throws EmptyFieldException, IOException {

        Token checkedToken = tokenService.findByToken(token);
        if (checkedToken == null) throw new EmptyFieldException("token not found");

        List<Image> list = this.findByToken(checkedToken);

        Image image = new Image(checkedToken,mpf.getOriginalFilename(),mpf.getContentType(),mpf.getSize());
        imageDao.save(image);

        mpf.transferTo(new File(UPLOAD_DIR + "/" + checkedToken.getSiteName() + "/" + image.getId()));
        list.add(image);
        return list;
    }
}
