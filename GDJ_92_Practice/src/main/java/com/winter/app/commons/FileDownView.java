package com.winter.app.commons;

// 필요한 클래스들을 가져옵니다.
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

// @Component: 이 클래스도 Spring이 관리하는 객체(Bean)임을 선언합니다.
// 이렇게 해야 Controller에서 "fileDownView"라는 이름으로 이 뷰를 찾을 수 있습니다.
@Component("fileDownView") // "fileDownView"라는 이름으로 Bean을 등록합니다.
@Slf4j
// AbstractView 클래스를 상속받아 커스텀 뷰의 기본 구조를 가져옵니다.
public class FileDownView extends AbstractView {

	// @Value: application.properties 파일의 'app.upload' 속성 값을 주입받습니다.
	// (파일이 저장된 기본 경로)
	@Value("${app.upload}")
	private String path;

	// 이 클래스의 핵심 메서드입니다. Controller가 이 뷰를 반환하면 이 메서드가 자동으로 실행됩니다.
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		log.info("============= Custom View 실행 =============");

		// 1. Controller에서 Model에 담아 보낸 파일 정보를 꺼냅니다.
		// model.get("vo")는 Controller에서 model.addAttribute("vo", boardFileVO)로 넣은 값입니다.
		BoardFileVO boardFileVO = (BoardFileVO) model.get("vo");

		// 2. 다운로드할 파일의 전체 경로를 만듭니다.
		// 예: "D:/upload/" + "notice"
		String boardName = model.get("board").toString();
		String filePath = path + boardName;

		// 3. 실제 파일 객체를 생성합니다.
		// new File(폴더 경로, 파일명)
		File file = new File(filePath, boardFileVO.getSaveName());

		// --- 지금부터 브라우저에게 "이건 다운로드 파일이야!" 라고 알려주는 과정 ---

		// 4. 응답(Response) 헤더 설정 (HTTP Header)

		// 4-1. 파일의 총 크기를 브라우저에 알려줍니다.
		response.setContentLengthLong(file.length());

		// 4-2. 다운로드 시 표시될 파일 이름을 설정합니다.
		// 한글, 공백 등 특수문자가 깨지지 않도록 파일명을 UTF-8로 인코딩(Encoding)합니다.
		String fileName = URLEncoder.encode(boardFileVO.getOriName(), "UTF-8");

		// 4-3. Content-Disposition 헤더: 이게 가장 중요! 응답을 '첨부파일(attachment)'로 처리하라고 브라우저에
		// 지시합니다.
		response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		// 4-4. Content-Transfer-Encoding 헤더: 데이터가 이진(binary) 데이터임을 알려줍니다.
		response.setHeader("Content-Transfer-Encoding", "binary");

		// 5. 파일을 서버에서 읽어서 클라이언트(브라우저)로 전송하는 과정

		// 5-1. 서버의 하드디스크에 있는 파일을 읽기 위한 입력 스트림(빨대)을 엽니다.
		FileInputStream fi = new FileInputStream(file);

		// 5-2. 브라우저로 데이터를 내보내기 위한 출력 스트림(빨대)을 엽니다.
		OutputStream os = response.getOutputStream();

		// 5-3. Spring의 FileCopyUtils를 사용해 파일을 효율적으로 복사(전송)합니다.
		// fi(입력)에서 읽은 내용을 os(출력)으로 그대로 흘려보냅니다.
		FileCopyUtils.copy(fi, os);

		// 6. 사용이 끝난 스트림(자원)을 반드시 닫아줍니다. (메모리 누수 방지)
		os.close();
		fi.close();
	}

}