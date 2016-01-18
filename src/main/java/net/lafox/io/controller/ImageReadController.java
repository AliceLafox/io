package net.lafox.io.controller;

import net.bull.javamelody.MonitoredWithSpring;
import net.lafox.io.service.ImageReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alice Lafox <alice@lafox.net> on 23.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

@RestController
@RequestMapping("image")
@MonitoredWithSpring
@CrossOrigin(origins = "*")
public class ImageReadController {

    @Autowired
    ImageReadService imageReadService;

    @RequestMapping(value = "test")
    public Object test(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("isAlive", "yes");
        map.put("isAliveBool", true);
        return map;
    }

    @RequestMapping(value = "list/{readToken}", method = RequestMethod.GET)
    public synchronized Map<String, Object> getImages(@PathVariable String readToken) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
            try {
                map.put("images", imageReadService.getImagesByReadToken(readToken));
            } catch (Exception e) {
                map.put("status", "ERROR");
                map.put("details", e.getMessage());
            }
        return map;
    }
    @RequestMapping(value = "avatar/{readToken}", method = RequestMethod.GET)
    public synchronized Map<String, Object> avatar(@PathVariable String readToken) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        try {
            map.put("avatar", imageReadService.getAvatarByReadToken(readToken));
        } catch (Exception e) {
            map.put("status", "ERROR");
            map.put("details", e.getMessage());
        }
        return map;
    }
}
