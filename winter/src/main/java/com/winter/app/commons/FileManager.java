package com.winter.app.commons;

import java.io.File;
import java.util.UUID; // 고유한 ID를 생성하기 위해 import

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Component // 개발자가 직접 만든 클래스를 Spring이 관리하는 객체(Bean)로 등록
public class FileManager {
	
	/**
	 * 파일을 HDD(하드디스크)에 저장하는 메서드
	 * @param dir 저장할 폴더명 (예: "D:/upload/notice/")
	 * @param file 사용자가 업로드한 파일 데이터 (MultipartFile)
	 * @return 서버에 저장된 고유한 파일명
	 * @throws Exception
	 */
	public String fileSave(String dir, MultipartFile file) throws Exception {
		// 1. 저장할 폴더가 존재하는지 확인하고, 없으면 생성합니다.
		File fileDir = new File(dir);
		if(!fileDir.exists()) {
			// mkdirs(): 만들려는 폴더의 상위 폴더가 없어도 전부 만들어줍니다.
			fileDir.mkdirs();
		}
		
		// 2. 중복되지 않는 고유한 파일명을 생성합니다.
		// UUID: 범용 고유 식별자(Universally Unique Identifier)를 생성해주는 클래스
		String fileName = UUID.randomUUID().toString();
		fileName = fileName + "_" + file.getOriginalFilename(); // UUID + "_" + 원본파일명
		
		// 3. 생성된 파일명으로 실제 파일을 HDD에 저장합니다.
		File newFile = new File(fileDir, fileName);
		
		// FileCopyUtils.copy() 사용: MultipartFile의 byte 데이터를 파일로 복사합니다.
		FileCopyUtils.copy(file.getBytes(), newFile);
		
		// 4. 저장된 파일명을 반환합니다. (이 이름이 DB에 저장될 것입니다)
		return fileName;
	}
}