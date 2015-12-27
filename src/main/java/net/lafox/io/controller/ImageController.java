package net.lafox.io.controller;

import net.lafox.io.exceptions.RollBackException;
import net.lafox.io.service.ImageService;
import net.lafox.io.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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



    @RequestMapping(value = "update/{id:\\d+}", method = RequestMethod.POST)
    public synchronized Map<String, Object> update(@PathVariable Long id,
                                                   @RequestParam(defaultValue = "") String token,
                                                   @RequestParam(defaultValue = "") List<MultipartFile> data
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        for (MultipartFile mpf : data) {
            try {
                imageService.updateImage(id,token, mpf);
            } catch (RollBackException e) {
                map.put("status", "ERROR");
                map.put("details", e.getMessage());
            }
        }
        return map;
    }

    @RequestMapping(value = "delete/{id:\\d+}", method = RequestMethod.DELETE)
    public synchronized Map<String, Object> update(@PathVariable Long id,
                                                   @RequestParam(defaultValue = "") String token

    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
            try {
                imageService.deleteImage(id,token);
            } catch (RollBackException e) {
                map.put("status", "ERROR");
                map.put("details", e.getMessage());
            }
        return map;
    }


    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public synchronized Map<String, Object> upload(@RequestParam(defaultValue = "") String token,
                                                   @RequestParam(defaultValue = "") List<MultipartFile> data
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        for (MultipartFile mpf : data) {
            try {
                imageService.addImage(token, mpf);
            } catch (RollBackException e) {
                map.put("status", "ERROR");
                map.put("details", e.getMessage());
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
