package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.helpers.SignupTextHelper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;
    private final static String default_view = "signup";
    private final static String default_view_redirect = "redirect:/signup";
    private final static String login_view_redirect = "redirect:/login";

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String signupView(){
        return default_view;
    }

    @PostMapping()
    public String signupUser(@ModelAttribute User user, RedirectAttributes redirectAttributes){
        String signupError = null;

        if(!userService.isUserNameAvailable(user.getUsername())){
            signupError = SignupTextHelper.signupError_userExists;
        }

        if(signupError == null){
            int rowsAdded = userService.createUser(user);
            if(rowsAdded < 0){
                signupError = SignupTextHelper.signupError_standarError;
            }
        }

        if(signupError == null){
            redirectAttributes.addFlashAttribute(SignupTextHelper.signupModel_success,true);
            return login_view_redirect;
        }else{
            redirectAttributes.addFlashAttribute(SignupTextHelper.signupModel_error, signupError);
        }

        return default_view_redirect;
    }
}
