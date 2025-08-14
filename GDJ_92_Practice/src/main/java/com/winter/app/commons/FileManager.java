package com.winter.app.commons;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

// @Component : 이 클래스를 Spring이 직접 관리하는 객체(Bean)로 등록합니다.
// 이렇게 등록하면 다른 클래스에서 @Autowired를 통해 이 객체를 주입받아 사용할 수 있습니다.
@Component
public class FileManager {

	// 파일을 삭제하는 메서드입니다.
	// dir: 파일이 위치한 폴더 경로 (예: "D:/upload/notice/")
	// fileName: 삭제할 파일의 이름
	public boolean fileDelete(String dir, String fileName) throws Exception {
		// 폴더 경로와 파일 이름을 합쳐서 실제 파일에 대한 File 객체를 생성합니다.
		File file = new File(dir, fileName);

		// file.delete() 메서드를 호출하여 실제 파일을 삭제하고, 성공 여부(true/false)를 반환합니다.
		return file.delete();
	}

	// 파일을 저장하는 메서드입니다.
	// dir: 파일을 저장할 폴더 경로 (예: "D:/upload/notice/")
	// attaces: 사용자가 업로드한 파일 데이터가 담긴 MultipartFile 객체
	public String fileSave(String dir, MultipartFile attaces) throws Exception {
		// 1. 파일을 저장할 디렉토리(폴더)를 준비합니다.
		// 전달받은 경로로 File 객체를 생성합니다.
		File file = new File(dir);
		// 만약 해당 경로의 폴더가 존재하지 않는다면,
		if (!file.exists()) {
			// file.mkdirs()를 호출하여 폴더를 생성합니다. (s가 붙으면 상위 폴더까지 모두 생성)
			file.mkdirs();
		}

		// 2. 저장할 파일명을 고유하게 생성합니다.
		// 다른 사용자가 같은 이름의 파일을 올려도 겹치지 않도록 하기 위함입니다.
		// UUID.randomUUID()를 사용해 절대로 중복되지 않는 랜덤한 문자열을 생성합니다.
		String fileName = UUID.randomUUID().toString();
		// 생성된 랜덤 문자열과 파일의 원본 이름을 '_'로 연결하여 새로운 파일명을 만듭니다.
		fileName = fileName + "_" + attaces.getOriginalFilename();

		// 3. 최종적으로 파일을 하드디스크(HDD)에 저장합니다.
		// 저장할 폴더와 새로 만든 파일명을 합쳐서 최종 저장 경로를 나타내는 File 객체를 다시 생성합니다.
		file = new File(file, fileName);

		// 파일을 저장하는 방법은 두 가지가 있습니다.

		// a. MutipartFile의 transferTo 메서드 사용 (내부적으로 처리되어 더 간편함)
		// attaces.transferTo(file);

		// b. Spring의 FileCopyUtils의 copy 메서드 사용 (byte 배열로 변환 후 저장)
		// attaces.getBytes()로 파일 데이터를 byte 배열로 읽어온 뒤, 지정된 경로(file)에 복사(저장)합니다.
		FileCopyUtils.copy(attaces.getBytes(), file);

		// DB에 저장하기 위해, 새로 만들어진 고유한 파일명을 반환합니다.
		return fileName;
	}
}