package com.example.demo.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Repository
public class FileSystemRepository {
	public static void saveFile(String uploadDir , String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		uploadPath = uploadPath.toAbsolutePath();
		InputStream inputStream = multipartFile.getInputStream();
			String pathFile = uploadPath + "\\" + fileName;
			Path filePath = Paths.get(pathFile);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
	}
}
