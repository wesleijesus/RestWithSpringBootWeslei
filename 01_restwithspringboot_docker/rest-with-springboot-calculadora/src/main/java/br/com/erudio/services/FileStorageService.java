package br.com.erudio.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.erudio.config.FileStorageConfig;
import br.com.erudio.exception.FileStorageException;
import br.com.erudio.exception.MyFileNotFoundException;

@Service
public class FileStorageService {

	private final Path FileStorageLocation;
	
	@Autowired
	public FileStorageService(FileStorageConfig fileStorageConfig) {
		this.FileStorageLocation = Paths.get(fileStorageConfig.getUploadDir()).
				toAbsolutePath().normalize();
		
		try {
			Files.createDirectories(this.FileStorageLocation);
		} catch (Exception e) {
			throw new FileStorageException("Could not create the directory where uploaded files will be stored",e);
			// TODO: handle exception
		}
		// TODO Auto-generated constructor stub
	}
	
	public String storeFile(MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence" + fileName);
			}
			
			Path targetLocation = this.FileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			
			return fileName;
		} catch (Exception e) {
			throw new FileStorageException ("Could not store file " + fileName +". Please try again!", e);
			// TODO: handle exception
		}
		
	}
	
	
	public Resource loadFileAsResource(String filename) {
		try {
			Path filepath = this.FileStorageLocation.resolve(filename).normalize();
			Resource resource = new UrlResource(filepath.toUri());
			if(resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("file not found" + filename);
			}
			
		} catch (Exception e) {
			throw new MyFileNotFoundException("file not found" + filename, e);
			// TODO: handle exception
		}
	}
	
	

}
