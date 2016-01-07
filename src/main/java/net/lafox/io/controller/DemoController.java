package net.lafox.io.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Alice Lafox <alice@lafox.net> on 07.01.16
 * Lafox.Net Software developers Team http://dev.lafox.net
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

@RequestMapping("angularJS")
    public String show(Model model) {
        model.addAttribute("message", "Hello there!");
        model.addAttribute("rToken", "ro-token");
        model.addAttribute("wToken", "rw-token");
        return "demo/angular";
    }

}
