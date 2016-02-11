package net.lafox.io.controller;

import net.bull.javamelody.MonitoredWithSpring;
import net.lafox.io.exceptions.RollBackException;
import net.lafox.io.service.ImageWriteService;
import net.lafox.io.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alice Lafox <alice@lafox.net> on 18.01.16
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

@RestController
@RequestMapping("image")
@MonitoredWithSpring
@CrossOrigin(origins = "*")
public class ImageWriteController {

    @Autowired
    ImageWriteService imageWriteService;

    @Autowired
    TokenService tokenService;



    @RequestMapping(value = "update/{id:[a-z0-9]{8}}", method = RequestMethod.POST)
    public synchronized Map<String, Object> update(@PathVariable String id,
                                                   @RequestParam(defaultValue = "") String token,
                                                   @RequestParam(defaultValue = "") List<MultipartFile> data
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        for (MultipartFile mpf : data) {
            try {
                imageWriteService.updateImage(id,token, mpf);
            } catch (RollBackException e) {
                map.put("status", "ERROR");
                map.put("details", e.getMessage());
            }
        }
        return map;
    }

    @RequestMapping(value = "delete/{id:[a-z0-9]{8}}", method = RequestMethod.DELETE)
    public synchronized Map<String, Object> update(@PathVariable String id,
                                                   @RequestParam(defaultValue = "") String token

    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
            try {
                imageWriteService.deleteImage(id,token);
            } catch (RollBackException e) {
                map.put("status", "ERROR");
                map.put("details", e.getMessage());
            }
        return map;
    }

    @RequestMapping(value = "avatar/{id:[a-z0-9]{8}}", method = RequestMethod.POST)
    public synchronized Map<String, Object> avatar(@PathVariable String id,
                                                   @RequestParam(defaultValue = "") String token

    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
            try {
                imageWriteService.setAvatar(id,token);
            } catch (RollBackException e) {
                map.put("status", "ERROR");
                map.put("details", e.getMessage());
            }
        return map;
    }

    @RequestMapping(value = "title/{id:[a-z0-9]{8}}", method = RequestMethod.POST)
    public synchronized Map<String, Object> setTitle(@PathVariable String id,
                                                     @RequestParam(defaultValue = "") String token,
                                                     @RequestParam(defaultValue = "") String title

    ) throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        try {
            imageWriteService.setTitle(id, token, title);
        } catch (RollBackException e) {
            map.put("status", "ERROR");
            map.put("details", e.getMessage());
        }
        return map;
    }

    @RequestMapping(value = "description/{id:[a-z0-9]{8}}", method = RequestMethod.POST)
    public synchronized Map<String, Object> setDescription(@PathVariable String id,
                                                           @RequestParam(defaultValue = "") String token,
                                                           @RequestParam(defaultValue = "") String description

    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        try {
            imageWriteService.setDescription(id, token, description);
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
        if (data.size() == 0) {
            map.put("status", "ERROR");
            map.put("details", "file list (data) is empty");
            return map;
        }
        map.put("status", "OK");
        for (MultipartFile mpf : data) {
            try {
                imageWriteService.addImage(token, mpf);
            } catch (RollBackException e) {
                map.put("status", "ERROR");
                map.put("details", e.getMessage());
            }
        }
        return map;
    }



}
