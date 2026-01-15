package paradigmesdeprogrammation.projetnfp121.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import paradigmesdeprogrammation.projetnfp121.services.*;

@Controller
public class IndexController {

    @GetMapping({"/index", "/"})
    public String home() {
        return "index";
    }

}
