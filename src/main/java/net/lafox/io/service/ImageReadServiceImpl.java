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
public class ImageReadServiceImpl implements ImageReadService {

    @Autowired
    ImageDao imageDao;
    @Autowired
    TokenService tokenService;


    @Override
    public List<Image> getImages(Token token) {
        return imageDao.findByTokenId(token.getId());
    }

    @Override
    public byte[] getImageContent(Long id) {
        return (byte[]) imageDao.getImageContent(id);
    }

    @Override
    public List<Image> getImagesByReadToken(String readToken) throws RollBackException {
        return imageDao.findImageListByReadToken(readToken);
    }
    @Override
    public Image getAvatarByReadToken(String readToken) throws RollBackException {
        return imageDao.findAvatarByReadToken(readToken);
    }

    @Override
    public Image findOne(Long id) {
        if (id == null || id < 1) return null;
        return imageDao.findOne(id);
    }

}
