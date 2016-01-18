package net.lafox.io.controller;

import net.bull.javamelody.MonitoredWithSpring;
import net.coobird.thumbnailator.Thumbnails;
import net.lafox.io.entity.Image;
import net.lafox.io.service.ImageReadService;
import net.lafox.io.utils.ImgProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alice Lafox <alice@lafox.net> on 31.12.15
 * Lafox.Net Software developers Team http://dev.lafox.net
 */
@Controller
@MonitoredWithSpring
@RequestMapping("")
public class RootController {

    @Autowired
    ImageReadService imageReadService;

    private static final String ETAG = "\"%d-w%d-h%d-o%s-v%d-q%d.%s\"";
    private static final long EXP = ZonedDateTime.now().plusYears(1).toInstant().toEpochMilli();
    private static final Map<String, MediaType> TYPES = new HashMap<String, MediaType>() {{
        put("png", MediaType.IMAGE_PNG);
        put("gif", MediaType.IMAGE_GIF);
        put("jpg", MediaType.IMAGE_JPEG);
    }};

    @RequestMapping(value = "{id:\\d+}-w{w:\\d+}-h{h:\\d+}-o{op:[wheco]}-q{quality:\\d+}-v{ver:\\d+}.{ext:png|jpg|gif}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> imgNew(
            @PathVariable long id,
            @PathVariable int w,
            @PathVariable int h,
            @PathVariable char op,
            @PathVariable long ver,
            @PathVariable long quality,
            @PathVariable String ext
    ) throws IOException {
        HttpStatus httpStatus = HttpStatus.OK;

        Image image = imageReadService.findOne(id);
        byte[] file;

        if (image == null) {//to return 404.png
            image = new Image();
            httpStatus = HttpStatus.NOT_FOUND;
            file = Files.readAllBytes(new File(this.getClass().getClassLoader().getResource("404.png").getFile()).toPath());
        } else {
            file = imageReadService.getImageContent(id);
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setExpires(EXP);
        headers.setETag(String.format(ETAG, id, w, h, op, ver, quality, ext));
        headers.setLastModified(image.getModified().getTime());
        headers.setContentType(TYPES.get(ext));

        Thumbnails.Builder res;
        switch (op) {
            case 'w':
                res = ImgProcessing.width(file, w);
                break;
            case 'h':
                res = ImgProcessing.height(file, h);
                break;
            case 'c':
                res = ImgProcessing.crop(file, w, h);
                break;
            case 'e':
                res = ImgProcessing.expand(file, w, h);
                break;
            default://orig
                headers.setContentType(MediaType.parseMediaType(image.getContentType()));
                return new ResponseEntity<>(file, headers, httpStatus);
        }

        byte[] imgBytes = null;

        if (res != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            res.outputQuality(0.01*quality).outputFormat(ext).toOutputStream(baos);
            imgBytes = baos.toByteArray();
        }
        return new ResponseEntity<>(imgBytes, headers, httpStatus);
    }
}
