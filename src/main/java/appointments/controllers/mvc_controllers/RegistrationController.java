package appointments.controllers.mvc_controllers;

import appointments.dto.UserDTO;
import appointments.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @author yanchenko_evgeniya
 */
@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private UsersService usersService;

    @Autowired
    public RegistrationController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "sign-up";
    }

    @PostMapping
    public String addNewUser(
            @Valid @ModelAttribute("user") UserDTO userDTO,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "sign-up";
        }
        usersService.addUser(userDTO);
        model.addAttribute("userRegistered", "Пользователь успешно зарегистрирован");

        return "sign-up";
    }

}
