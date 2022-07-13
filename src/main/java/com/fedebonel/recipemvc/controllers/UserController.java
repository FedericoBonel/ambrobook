package com.fedebonel.recipemvc.controllers;

import com.fedebonel.recipemvc.model.Roles;
import com.fedebonel.recipemvc.datatransferobjects.UserDto;
import com.fedebonel.recipemvc.model.User;
import com.fedebonel.recipemvc.model.UserRole;
import com.fedebonel.recipemvc.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(UserController.USER_URI)
public class UserController {

    public static final String USER_URI = "/user";
    public static final String REGISTRATION_FORM_PATH = "user/registrationform";
    public static final String VERIFICATION_CONFIRMED_PATH = "user/verificationconfirmed";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signin")
    public String loginUserForm() {
        log.debug("Showing user sign in form");

        return "user/loginform";
    }

    @GetMapping("/signup")
    public String registerUserForm(Model model) {
        log.debug("Showing user registration form");

        model.addAttribute("user", new User());
        return REGISTRATION_FORM_PATH;
    }

    @PostMapping
    public String registerUserPost(Model model, HttpServletRequest request,
                                   @Valid @ModelAttribute("user") UserDto user,
                                   BindingResult result) {

        log.debug("Registering user with username: " + user.getUsername());

        if (userService.findByUsername(user.getUsername()) != null) {
            result.rejectValue("username",
                    "error.user",
                    "An account already exists with this username.");
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            result.rejectValue("email",
                    "error.user",
                    "An account already exists for this email.");
        }

        if (result.hasErrors()) {
            return REGISTRATION_FORM_PATH;
        }
        user.setUserRoles(List.of(buildUserRole()));
        model.addAttribute("user", userService.create(user, getSiteURLFrom(request)));

        return "user/verificationnotification";
    }

    @GetMapping("/verify")
    private String verifyAccount(Model model, @RequestParam(name = "code") String code) {
        UserDto userToVerify = userService.findByVerification(code);
        userToVerify.setActive(true);
        userService.verify(userToVerify);

        model.addAttribute("user", userToVerify);
        return VERIFICATION_CONFIRMED_PATH;
    }

    private String getSiteURLFrom(HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        return requestUrl.replace(request.getServletPath(),  "");
    }

    private UserRole buildUserRole() {
        UserRole userRole = new UserRole();
        userRole.setRole(Roles.ROLE_USER.toString());
        return userRole;
    }
}
