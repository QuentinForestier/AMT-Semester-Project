package com.webapplication.crossport.domain.services;

import com.webapplication.crossport.config.images.ImageConfiguration;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.org.apache.commons.io.FilenameUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Properties;

/**
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
public class FileService {

    private static final String[] extensions = new String[]{"jpeg", "jpg", "gif", "png"};


    public boolean isAnAuthorizedExtension(MultipartFile multipartFile) {
        return Arrays.asList(extensions).contains(FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
    }

    public void saveFile(Integer id, MultipartFile multipartFile) {
        Path uploadPath = Paths.get(ImageConfiguration.uploadDir);
        String fileName = id.toString() + getExtension(multipartFile);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    public void removeFile(Integer id, String extension) {
        Path uploadPath = Paths.get(ImageConfiguration.uploadDir);
        String fileName = id.toString() + extension;
        Path filePath = uploadPath.resolve(fileName);
        try {
            Files.delete(filePath);
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
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
