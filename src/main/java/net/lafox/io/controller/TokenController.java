package net.lafox.io.controller;

import net.lafox.io.entity.Token;
import net.lafox.io.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alice Lafox <alice@lafox.net> on 22.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

@RestController
@RequestMapping("api/token")
public class TokenController {

    @Autowired
    TokenService tokenService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object add(HttpServletRequest request,
                      @RequestParam(defaultValue = "") String siteName,
                      @RequestParam(defaultValue = "") String ownerName,
                      @RequestParam(defaultValue = "0") Long ownerId) {
        Map<String, Object> map = new HashMap<>();
        try {
            Token token=tokenService.addToken(siteName, ownerName, ownerId, request.getRemoteAddr());
            map.put("readToken", token.getReadToken());
            map.put("writeToken", token.getWriteToken());
            map.put("status", "OK");

        } catch (Exception e) {
            map.put("status", "ERROR");
            map.put("details", e.getMessage());
        }
        return map;

    }
}
