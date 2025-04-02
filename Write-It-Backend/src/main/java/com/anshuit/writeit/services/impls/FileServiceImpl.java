package com.anshuit.writeit.services.impls;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.anshuit.writeit.services.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public boolean isImageWithValidExtension(MultipartFile file) {
		String filename = file.getOriginalFilename();
		if (filename.endsWith(".png") || filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
			return true;
		}
		return false;
	}
}
