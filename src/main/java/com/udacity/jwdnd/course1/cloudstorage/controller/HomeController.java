package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.helpers.HomeTextHelper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final static String default_view = "home";

    public HomeController(UserService userService, FileService fileService, NoteService noteService, CredentialService credentialService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping()
    public String getHomePage(Model model, Authentication authentication){
        String username = authentication.getName();
        User user = userService.getUser(username);

        model.addAttribute(HomeTextHelper.usernameAttribute, user.getUsername());
        model.addAttribute(HomeTextHelper.userFilesAttribute, fileService.getFiles(user.getUserId()));
        model.addAttribute(HomeTextHelper.userNotesAttribute, noteService.getNotes(user.getUserId()));
        model.addAttribute(HomeTextHelper.userCredentialsAttribute, credentialService.getCredentials(user.getUserId()));

        return default_view;
    }
}
