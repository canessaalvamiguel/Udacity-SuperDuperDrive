package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.helpers.NoteTextHelper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;
    private final static String home_view_redirect = "redirect:/home";
    private final static int OPERATION_CREATE = 1;
    private final static int OPERATION_DELETE = 2;
    private final static int OPERATION_UPDATE = 3;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping()
    public String upsertNote(@ModelAttribute Note note, Authentication authentication, RedirectAttributes redirectAttributes){

        String username = authentication.getName();
        User user = userService.getUser(username);
        note.setUserid(user.getUserId());

        if(note.getNoteid() == null){
            int result = noteService.createNote(note);
            handleFlashMessageResult(result, redirectAttributes, OPERATION_CREATE);
        }else{
            int result = noteService.updateNote(note);
            handleFlashMessageResult(result, redirectAttributes, OPERATION_UPDATE);
        }

        return home_view_redirect;
    }

    @GetMapping("/delete/{noteid}")
    public String deleteNote(@PathVariable int noteid , RedirectAttributes redirectAttributes){

        int result = noteService.deleteNote(noteid);
        handleFlashMessageResult(result, redirectAttributes, OPERATION_DELETE);
        return home_view_redirect;
    }

    private void handleFlashMessageResult(int result, RedirectAttributes redirectAttributes, int operation) {
        redirectAttributes.addFlashAttribute(NoteTextHelper.showNotesTab, true);
        if(result > 0){
            switch (operation){
                case OPERATION_CREATE:
                    redirectAttributes.addFlashAttribute(NoteTextHelper.successMessage, NoteTextHelper.sucessMessage_create);
                    break;
                case OPERATION_UPDATE:
                    redirectAttributes.addFlashAttribute(NoteTextHelper.successMessage, NoteTextHelper.sucessMessage_update);
                    break;
                case OPERATION_DELETE:
                    redirectAttributes.addFlashAttribute(NoteTextHelper.successMessage, NoteTextHelper.sucessMessage_delete);
                    break;
                default:
                    redirectAttributes.addFlashAttribute(NoteTextHelper.successMessage, NoteTextHelper.sucessMessage_generic);
            }
        }else{
            switch (operation){
                case OPERATION_CREATE:
                    redirectAttributes.addFlashAttribute(NoteTextHelper.errorMessage, NoteTextHelper.errorMessage_create);
                    break;
                case OPERATION_UPDATE:
                    redirectAttributes.addFlashAttribute(NoteTextHelper.errorMessage, NoteTextHelper.errorMessage_update);
                    break;
                case OPERATION_DELETE:
                    redirectAttributes.addFlashAttribute(NoteTextHelper.errorMessage, NoteTextHelper.errorMessage_delete);
                    break;
                default:
                    redirectAttributes.addFlashAttribute(NoteTextHelper.errorMessage, NoteTextHelper.errorMessage_generic);
            }
        }
    }
}
