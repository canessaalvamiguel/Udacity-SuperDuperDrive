package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.helpers.CredentialTextHelper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/credential")
public class CredentialController {

    private final CredentialService credentialService;
    private final UserService userService;
    private final static String home_view_redirect = "redirect:/home";
    private final static int OPERATION_CREATE = 1;
    private final static int OPERATION_DELETE = 2;
    private final static int OPERATION_UPDATE = 3;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping()
    public String upsertNote(@ModelAttribute Credential credential, Authentication authentication, RedirectAttributes redirectAttributes){

        String username = authentication.getName();
        User user = userService.getUser(username);
        credential.setUserid(user.getUserId());

        if(credential.getCredentialid() == null){
            int result = credentialService.createCredential(credential);
            handleFlashMessageResult(result, redirectAttributes, OPERATION_CREATE);
        }else{
            int result = credentialService.updateCredential(credential);
            handleFlashMessageResult(result, redirectAttributes, OPERATION_UPDATE);
        }

        return home_view_redirect;
    }

    @GetMapping("/delete/{credentialid}")
    public String deleteNote(@PathVariable int credentialid , RedirectAttributes redirectAttributes){

        int result = credentialService.deleteCredential(credentialid);
        handleFlashMessageResult(result, redirectAttributes, OPERATION_DELETE);
        return home_view_redirect;
    }

    @PostMapping(value ="/password/{credentialid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getDecryptedPassword(@PathVariable int credentialid, Authentication authentication ){

        String username = authentication.getName();
        User user = userService.getUser(username);

        String decryptedPassword = credentialService.getDecryptedPassword(credentialid, user.getUserId());
        JSONObject response = new JSONObject();
        response.put("response", decryptedPassword);
        return response.toString();
    }

    private void handleFlashMessageResult(int result, RedirectAttributes redirectAttributes, int operation) {

        redirectAttributes.addFlashAttribute(CredentialTextHelper.showCredentialsTab, true);
        if (result > 0) {
            switch (operation) {
                case OPERATION_CREATE:
                    redirectAttributes.addFlashAttribute(CredentialTextHelper.successMessage, CredentialTextHelper.sucessMessage_create);
                    break;
                case OPERATION_UPDATE:
                    redirectAttributes.addFlashAttribute(CredentialTextHelper.successMessage, CredentialTextHelper.sucessMessage_update);
                    break;
                case OPERATION_DELETE:
                    redirectAttributes.addFlashAttribute(CredentialTextHelper.successMessage, CredentialTextHelper.sucessMessage_delete);
                    break;
                default:
                    redirectAttributes.addFlashAttribute(CredentialTextHelper.successMessage, CredentialTextHelper.sucessMessage_generic);
            }
        } else {
            switch (operation) {
                case OPERATION_CREATE:
                    redirectAttributes.addFlashAttribute(CredentialTextHelper.errorMessage, CredentialTextHelper.errorMessage_create);
                    break;
                case OPERATION_UPDATE:
                    redirectAttributes.addFlashAttribute(CredentialTextHelper.errorMessage, CredentialTextHelper.errorMessage_update);
                    break;
                case OPERATION_DELETE:
                    redirectAttributes.addFlashAttribute(CredentialTextHelper.errorMessage, CredentialTextHelper.errorMessage_delete);
                    break;
                default:
                    redirectAttributes.addFlashAttribute(CredentialTextHelper.errorMessage, CredentialTextHelper.errorMessage_generic);
            }
        }
    }
}
