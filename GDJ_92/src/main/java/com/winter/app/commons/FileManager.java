package com.winter.app.commons;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileManager {
	
	public boolean fileDelete(String dir, String fileName)throws Exception{
		File file = new File(dir,fileName);
		
		return file.delete();
	}
	
	
	public String fileSave(String dir, MultipartFile attaces)throws Exception{
		//1. 디렉토리 생성
		File file = new File(dir);
		if(!file.exists()) {
			file.mkdirs();
		}
		
		//2. 저장할 파일명을 생성
		String fileName = UUID.randomUUID().toString();
		fileName = fileName+"_"+attaces.getOriginalFilename();
		
		//3. HDD에 저장
		file = new File(file, fileName);
		
		//a. MutipartFile transferTo메서드 사용
		//attaces.transferTo(file);
		
		//b. FileCopyUtils의 copy 메서드 사용
		FileCopyUtils.copy(attaces.getBytes(), file);
		
		return fileName;
		
	}

}
