package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.tomcat.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class FileService {

    @Value("${app.upload.dir:${user.home}/superduperdrive/}")
    public String uploadDir;

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFiles(Integer userId){
        return fileMapper.getFiles(userId);
    }

    public boolean isFileNameAvailable(String fileName, Integer userId){
        return fileMapper.getFileByFileName(fileName, userId) == null;
    }

    public  Resource loadFile(Integer userId, Integer fileid) {
        try {
            File file = fileMapper.getFile(userId, fileid);
            Path filePath = Paths.get(uploadDir, file.getFilename());
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } /*else {
                throw new FileNotFoundException("Something went wrong while dowloading your file.");
            }*/
        } catch (Exception  e) {
            e.printStackTrace();
        }

        return null;
    }

    public int deleteFile(Integer userId, Integer fileid){

        try {
            File file = fileMapper.getFile(userId, fileid);
            Path filePath = Paths.get(uploadDir, file.getFilename());
            Files.delete(filePath);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return fileMapper.deleteFile(userId, fileid);
        }
    }

    public int uploadFile(MultipartFile file, int userid) {

        try {
            java.io.File directory = new java.io.File(uploadDir);
            if (! directory.exists()){
                directory.mkdir();
            }
            Path copyLocation = Paths
                    .get(uploadDir + java.io.File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            File fileToInsert = new File();
            fileToInsert.setFilename(file.getOriginalFilename());
            fileToInsert.setContenttype(file.getContentType());
            fileToInsert.setFilesize(String.valueOf(file.getSize()));
            fileToInsert.setUserid(userid);
            return fileMapper.insertFile(fileToInsert);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file " + file.getOriginalFilename()+ ". Please try again!");
        }
    }
}
