package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.helpers.FileTextHelper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;
    private final UserService userService;
    private final int MAX_SIZE_ALLOWED = 10485760;
    private final static int OPERATION_CREATE = 1;
    private final static int OPERATION_DELETE = 2;
    private final static String home_view_redirect = "redirect:/home";

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping()
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, RedirectAttributes redirectAttributes, Authentication authentication) {

        if (file.getSize() > MAX_SIZE_ALLOWED) {
            redirectAttributes.addFlashAttribute(FileTextHelper.errorMessage, FileTextHelper.errorMessage_create_maxlimit);
            return home_view_redirect;
        }

        String username = authentication.getName();
        User user = userService.getUser(username);

        if (!fileService.isFileNameAvailable(file.getOriginalFilename(), user.getUserId())) {
            redirectAttributes.addFlashAttribute(FileTextHelper.errorMessage, FileTextHelper.errorMessage_create_filemeIsUsed);
            return home_view_redirect;
        }

        int result = fileService.uploadFile(file, user.getUserId());
        handleFlashMessageResult(result, redirectAttributes, OPERATION_CREATE);

        return home_view_redirect;
    }

    @GetMapping("/download/{fileid}")
    public ResponseEntity<Resource> downloadFile(@PathVariable int fileid, Authentication authentication) {

        String username = authentication.getName();
        User user = userService.getUser(username);

        Resource  file = fileService.loadFile(user.getUserId(), fileid);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/delete/{fileid}")
    public String deleteFile(@PathVariable int fileid, RedirectAttributes redirectAttributes, Authentication authentication){

        String username = authentication.getName();
        User user = userService.getUser(username);

        int result = fileService.deleteFile(user.getUserId(), fileid);
        handleFlashMessageResult(result, redirectAttributes, OPERATION_DELETE);
        return home_view_redirect;
    }

    private void handleFlashMessageResult(int result, RedirectAttributes redirectAttributes, int operation) {
        redirectAttributes.addFlashAttribute(FileTextHelper.showFilesTab, true);
        if(result > 0){
            switch (operation){
                case OPERATION_CREATE:
                    redirectAttributes.addFlashAttribute(FileTextHelper.successMessage, FileTextHelper.sucessMessage_create);
                    break;
                case OPERATION_DELETE:
                    redirectAttributes.addFlashAttribute(FileTextHelper.successMessage, FileTextHelper.sucessMessage_delete);
                    break;
                default:
                    redirectAttributes.addFlashAttribute(FileTextHelper.successMessage, FileTextHelper.sucessMessage_generic);
            }
        }else{
            switch (operation){
                case OPERATION_CREATE:
                    redirectAttributes.addFlashAttribute(FileTextHelper.errorMessage, FileTextHelper.errorMessage_create);
                    break;
                case OPERATION_DELETE:
                    redirectAttributes.addFlashAttribute(FileTextHelper.errorMessage, FileTextHelper.errorMessage_delete);
                    break;
                default:
                    redirectAttributes.addFlashAttribute(FileTextHelper.errorMessage, FileTextHelper.errorMessage_generic);
            }
        }
    }
}