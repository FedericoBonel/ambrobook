package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.config.Roles;
import com.fedebonel.recipemvc.datatransferobjects.UserDto;
import com.fedebonel.recipemvc.model.User;
import com.fedebonel.recipemvc.model.UserRole;
import com.fedebonel.recipemvc.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(UserController.USER_URI)
public class UserController {

    public static final String USER_URI = "/user";
    public static final String REGISTRATION_FORM_PATH = "user/registrationform";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String registerUserForm(Model model) {
        log.debug("Showing user registration form");

        model.addAttribute("user", new User());
        return REGISTRATION_FORM_PATH;
    }

    @PostMapping
    public String registerUserPost(Model model,
                                   @Valid @ModelAttribute("user") UserDto user,
                                   BindingResult result) {
        if (result.hasErrors()) {
            return REGISTRATION_FORM_PATH;
        }
        user.setUserRoles(List.of(buildUserRole()));
        model.addAttribute("user", userService.save(user));

        return "redirect:/";
    }

    private UserRole buildUserRole() {
        UserRole userRole = new UserRole();
        userRole.setRole(Roles.ROLE_USER.toString());
        return userRole;
    }
}
