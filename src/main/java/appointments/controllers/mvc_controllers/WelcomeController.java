package appointments.controllers.mvc_controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yanchenko_evgeniya
 */
@Controller
@RequestMapping("/")
public class WelcomeController {

    @GetMapping("/welcome")
    @ResponseBody
    public String welcome() {
        return "Welcome!";
    }

    @GetMapping
    public String welcomeAuthenticatedUser() {
        return "main";
    }
}
