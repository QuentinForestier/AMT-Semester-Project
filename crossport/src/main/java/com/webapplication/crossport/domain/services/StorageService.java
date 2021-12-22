package com.webapplication.crossport.domain.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service file to interract with aws
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Service
public class StorageService {

	@Value("${com.webapplication.crossport.config.aws.bucket}")
	private String bucketName;

	@Autowired
	private AmazonS3 s3Client;

	final String dir = System.getProperty("user.dir");

	/**
	 * Upload a file to AWS S3
	 * @param fileName name of file on S3
	 * @param multipartFile file to upload
	 */
	public void uploadFile(String fileName, MultipartFile multipartFile) {
		File file = convertMultipartfileToFile(multipartFile);
		s3Client.putObject(new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
		file.delete();
	}

	/**
	 * Delete a file to AWS S3
	 * @param fileName name of file on S3
	 */
	public void deleteFile(String fileName) {
		s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
	}

	/**
	 * Get a file stored in AWS S3
	 * @param fileName name of file on S3
	 */
	public S3ObjectInputStream retrieveFile(String fileName) {
		return s3Client.getObject(bucketName, fileName).getObjectContent();
	}

	/**
	 * Convert a multipart file to a standard file
	 * @param file Multipart file to convert
	 * @return Returns the file from multipart file
	 */
	private File convertMultipartfileToFile(MultipartFile file) {
		File convertedFile = new File(dir + "/" +file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
			fos.write(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return convertedFile;
	}
}
