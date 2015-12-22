package net.lafox.io.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("image")
public class ImageController {

    @RequestMapping(value = "test")
    public Object test(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("isAlive", "yes");
        map.put("isAliveBool", true);
        return map;

    }


//TODO @RequestMapping(value = "/upload", method = RequestMethod.POST)

}
