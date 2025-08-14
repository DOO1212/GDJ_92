package com.winter.app.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import com.winter.app.board.BoardFileVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// @Component : 이 클래스를 Spring이 직접 관리하는 객체(Bean)로 등록합니다.
// @Slf4j : Lombok을 통해 로그 객체(log)를 자동으로 생성해줍니다.
@Component("fileDownView") // "fileDownView" 라는 이름의 Bean으로 등록. Controller에서 이 이름을 사용해 호출합니다.
@Slf4j
public class FileDownView extends AbstractView {
	
	// @Value("${app.upload}") : application.properties의 'app.upload' 속성 값을 'path' 변수에 주입합니다. (예: "D:/upload/")
	@Value("${app.upload}")
	private String path;
	
	// 이 클래스의 핵심 메서드입니다.
	// Controller가 "fileDownView"를 반환하면 Spring이 이 메서드를 자동으로 실행하여 렌더링(여기서는 파일 전송)을 수행합니다.
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// Controller에서 전달한 Model 객체의 내용을 로그로 출력합니다.
		log.info("============= FileDownView 실행 =============");
		
		// 1. Controller가 Model에 담아 보낸 파일 정보를 꺼냅니다.
		// model.get("vo")는 Object 타입이므로 BoardFileVO 타입으로 형변환(casting)해줍니다.
		BoardFileVO boardFileVO = (BoardFileVO)model.get("vo");
		// Model에서 게시판 이름(예: "notice")을 꺼냅니다.
		String board = (String)model.get("board");
		
		// 2. 다운로드할 파일의 전체 경로를 생성합니다.
		// 예: "D:/upload/" + "notice/"
		String filePath = path + board;
		
		// 3. 파일 경로와 서버에 저장된 파일명을 합쳐서 실제 파일 객체를 만듭니다.
		File file = new File(filePath, boardFileVO.getSaveName());
		
		// --- 여기서부터는 클라이언트(브라우저)에게 응답을 보내기 위한 설정입니다. ---
		
		// 4. 응답 헤더 설정 (1): 파일의 총 크기를 설정합니다.
		response.setContentLengthLong(file.length());
		
		// 5. 응답 헤더 설정 (2): 파일 다운로드 시 표시될 파일 이름을 설정합니다.
		// 한글 등 비영어권 문자가 깨지지 않도록 파일 이름을 UTF-8로 인코딩합니다.
		String fileName = URLEncoder.encode(boardFileVO.getOriName(), "UTF-8");
		
		// 6. 응답 헤더 설정 (3): 가장 중요한 부분.
		// "Content-Disposition"은 응답 본문을 브라우저가 어떻게 처리할지 알려주는 헤더입니다.
		// "attachment"는 내용을 화면에 표시하지 말고, 파일로 다운로드하라는 의미입니다.
		response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		// "Content-Transfer-Encoding"은 전송 데이터의 인코딩 방식을 나타냅니다. "binary"는 이진 파일임을 의미합니다.
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		// --- 실제 파일 데이터 전송 ---
		
		// 7. 서버의 하드디스크에 있는 파일을 읽어올 준비를 합니다. (입력 스트림)
		FileInputStream fi = new FileInputStream(file);
		
		// 8. 읽어온 파일 데이터를 클라이언트(브라우저)에게 전송할 준비를 합니다. (출력 스트림)
		OutputStream os = response.getOutputStream();
		
		// 9. Spring의 FileCopyUtils를 사용해 입력 스트림에서 출력 스트림으로 파일 데이터를 복사(전송)합니다.
		FileCopyUtils.copy(fi, os);
		
		// 10. 데이터 전송이 끝났으므로, 사용한 모든 자원(스트림)을 닫아서 메모리 누수를 방지합니다.
		os.close();
		fi.close();
	}
}