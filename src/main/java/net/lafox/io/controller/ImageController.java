package net.lafox.io.controller;

import net.lafox.io.exceptions.EmptyFieldException;
import net.lafox.io.service.ImageService;
import net.lafox.io.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alice Lafox <alice@lafox.net> on 23.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

@RestController
@RequestMapping("image")
public class ImageController {

    @Autowired
    ImageService imageService;

    @Autowired
    TokenService tokenService;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public synchronized Map<String, Object> upload(MultipartHttpServletRequest request,
                                                   HttpServletResponse response,
                                                   @RequestParam(defaultValue = "") String token,
                                                   @RequestParam(value = "data", required = false) List<MultipartFile> files
    ) {
        Map<String, Object> map = new HashMap<>();

        for (MultipartFile mpf : files) {
            try {
                map.put("status", "OK");
                map.put("images", imageService.upload(token, mpf));
            } catch (IOException e) {
                map.put("status", "ERROR");
                map.put("details", e.getMessage() + " IOException");

            } catch (EmptyFieldException e) {
                map.put("status", "ERROR");
                map.put("details", e.getMessage() + " EmptyFieldException");
            }
        }
        return map;
    }

    @RequestMapping(value = "test")
    public Object test(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("isAlive", "yes");
        map.put("isAliveBool", true);
        return map;
    }

}
