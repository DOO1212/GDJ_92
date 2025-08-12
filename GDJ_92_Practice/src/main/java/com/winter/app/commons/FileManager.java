package com.winter.app.commons;

// 파일 시스템을 다루기 위한 File 클래스를 가져옵니다.
import java.io.File;
// 범용 고유 식별자(UUID)를 생성하기 위한 클래스를 가져옵니다.
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

// @Component: 이 클래스를 Spring이 관리하는 객체(Bean)로 등록합니다.
// 이렇게 하면 다른 클래스에서 @Autowired를 통해 이 객체를 주입받아 사용할 수 있습니다.
@Component
public class FileManager {
	
	// 파일을 삭제하는 메서드입니다.
	// dir: 파일이 있는 폴더 경로 (예: "D:/upload/notice")
	// fileName: 삭제할 파일의 이름
	public boolean fileDelete(String dir, String fileName)throws Exception{
		// 1. 폴더 경로와 파일명을 합쳐서 전체 경로를 가진 File 객체를 생성합니다.
		File file = new File(dir,fileName);
		
		// 2. file.delete() 메서드를 호출하여 파일을 실제로 삭제하고,
		//    성공 여부(true/false)를 반환합니다.
		return file.delete();
	}
	
	
	// 파일을 저장하는 메서드입니다.
	// dir: 파일을 저장할 폴더 경로
	// attaches: 사용자가 업로드한 파일 데이터(MultipartFile)
	public String fileSave(String dir, MultipartFile attaces)throws Exception{
		// 1. 파일을 저장할 디렉토리(폴더)가 있는지 확인하고, 없으면 생성합니다.
		File file = new File(dir);
		if(!file.exists()) {
			// mkdirs()는 중간 경로가 없어도 전부 만들어줍니다. (예: D:/upload/notice)
			file.mkdirs();
		}
		
		// 2. 저장할 파일명을 생성합니다. (중복 방지)
		// UUID.randomUUID().toString(): 절대 중복되지 않는 랜덤한 문자열을 생성합니다.
		// 예: "550e8400-e29b-41d4-a716-446655440000"
		String fileName = UUID.randomUUID().toString();
		// 생성된 랜덤 문자열 뒤에 원본 파일명을 붙여서, 어떤 파일이었는지 식별하기 쉽게 만듭니다.
		// 예: "550e8400-e29b-41d4-a716-446655440000_내사진.jpg"
		fileName = fileName+"_"+attaces.getOriginalFilename();
		
		// 3. 최종적으로 저장될 경로와 파일명을 가진 File 객체를 다시 생성합니다.
		file = new File(file, fileName);
		
		// 4. 업로드된 파일 데이터를 하드디스크(HDD)에 실제로 저장합니다.
		
		// 방법 a: MutipartFile의 transferTo 메서드 사용 (더 간편함)
		// attaces.transferTo(file);
		
		// 방법 b: Spring의 FileCopyUtils의 copy 메서드 사용
		// 업로드된 파일의 바이트 데이터(attaces.getBytes())를 목적지 파일(file)에 복사합니다.
		FileCopyUtils.copy(attaces.getBytes(), file);
		
		// 5. 서버에 저장된 파일명을 반환하여, 이 이름을 DB에 기록할 수 있도록 합니다.
		return fileName;
		
	}

}