package com.webapplication.crossport.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

/**
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Service
public class FileService {

    @Autowired
    StorageService storageService;

    private static final String[] extensions = new String[]{"jpeg", "jpg", "gif", "png"};


    public boolean isAnAuthorizedExtension(MultipartFile multipartFile) {
        return Arrays.asList(extensions).contains(FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
    }

    public void saveFile(Integer id, MultipartFile multipartFile) {

        String fileName = id.toString() + getExtension(multipartFile);
        storageService.uploadFile(fileName, multipartFile);

    }

    public void removeFile(Integer id, String extension) {
        String fileName = id.toString() + extension;
        storageService.deleteFile(fileName);
    }

    public String getExtension(MultipartFile multipartFile) {
        String extension = "";
        String fileName = multipartFile.getOriginalFilename();

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = multipartFile.getOriginalFilename().substring(i + 1);
            extension = "." + extension;
        }
        return extension;
    }
}
