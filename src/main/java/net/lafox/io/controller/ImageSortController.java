package net.lafox.io.controller;

import net.bull.javamelody.MonitoredWithSpring;
import net.lafox.io.service.ImageWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alice Lafox <alice@lafox.net> on 18.01.16
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

@RestController
@RequestMapping("image")
@MonitoredWithSpring
@CrossOrigin(origins = "*")
public class ImageSortController {

    @Autowired
    ImageWriteService imageWriteService;

    @RequestMapping(value = "sortIndex/{id:\\d+}", method = RequestMethod.POST)
    public Map<String, Object> sortIndex(
            @PathVariable Long id,
            @RequestParam(defaultValue = "") String token,
            @RequestParam(defaultValue = "") String op) {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("status", "OK");
            switch (op) {
                case "plus":
                    imageWriteService.sortIndexPlus(id,token);
                    break;
                case "minus":
                    imageWriteService.sortIndexMinus(id,token);
                    break;
                case "toFirst":
                    imageWriteService.sortIndexToFirst(id,token);
                    break;
                case "toLast":
                    imageWriteService.sortIndexToLast(id,token);
                    break;
                default:
                    map.put("status", "ERROR");
                    map.put("details", "Unsupported operation '" + op + "' you can: plus|minus|toFirst|toLast");
            }
        } catch (Exception e) {
            map.put("status", "ERROR");
            map.put("details", e.getMessage());
        }
        return map;
    }

}
