package net.lafox.io.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api")
public class ImageRestController {

    @RequestMapping(value = "test")
    public Object test(HttpServletRequest request) {
        Map<String, Object> map=new HashMap<>();
        map.put("1", "1");
        map.put("2", "2");
        return map;

    }


}
