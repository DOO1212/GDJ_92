// FileService.java
// // 목적: 파일 업로드/조회/삭제 등 비즈니스 로직을 담당하고 DAO를 호출한다
package com.winter.app.board;

// // 파일 저장 경로/이름 생성을 위해 사용
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
// // 날짜 기반 하위 폴더 생성을 위해 사용
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
// // 랜덤 파일명을 위해 사용
import java.util.Locale;
import java.util.UUID;
// // 목록 조회 반환을 위해 사용
import java.util.List;
// // 스프링 서비스 빈을 선언하기 위해 사용
import org.springframework.stereotype.Service;
// // 트랜잭션 경계를 선언하기 위해 사용
import org.springframework.transaction.annotation.Transactional;
// // 설정값 주입(@Value)을 사용하기 위해 import
import org.springframework.beans.factory.annotation.Value;
// // 멀티파트 파일 업로드를 위해 사용
import org.springframework.web.multipart.MultipartFile;
// // 생성자 주입을 위한 롬복 어노테이션
import lombok.RequiredArgsConstructor;

// // 서비스 컴포넌트로 등록한다
@Service
// // final 필드 기반의 생성자를 자동 생성하여 생성자 주입을 편하게 한다
@RequiredArgsConstructor
public class FileService {

	// // DAO 프록시를 생성자 주입으로 받는다
	private final FileDAO fileDAO;

	// // application.properties 의 app.upload 값을 주입받아 업로드 루트 디렉터리로 사용한다
	@Value("${app.upload}")
	private String uploadRoot;

	// // 파일 메타데이터 목록을 조회한다(삭제되지 않은 행만 조회하는 SQL을 매퍼가 수행)
	// // 읽기 전용 트랜잭션을 사용한다
	@Transactional(readOnly = true)
	public List<FileVO> list() {
		// // DAO에게 목록 조회를 위임하고 결과를 반환한다
		return fileDAO.selectFileList();
	}

	// // PK 기준 단건을 조회한다
	// // 읽기 전용 트랜잭션을 사용한다
	@Transactional(readOnly = true)
	public FileVO detail(long id) {
		// // DAO에게 단건 조회를 위임하고 결과를 반환한다
		return fileDAO.selectFile(id);
	}

	// // 파일을 디스크에 저장하고 메타데이터를 DB에 INSERT 한다
	// // 성공 시 생성된 PK(file_id)를 반환한다
	@Transactional
	public long uploadAndSave(MultipartFile mf) throws IOException {
		// // 널 또는 빈 파일이면 예외를 던진다
		if (mf == null || mf.isEmpty())
			throw new IllegalArgumentException("empty file");

		// // 원본 파일명을 확보한다
		String originName = mf.getOriginalFilename();

		// // 확장자를 소문자로 계산한다(없으면 bin으로 대체)
		String fileExt = extractExtLower(originName);

		// // 저장할 상대 경로를 yyyy/MM/dd 형태로 만든다(예: 2025/08/26)
		String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

		// // 업로드 루트 + 날짜 경로로 디렉터리 객체를 만든다
		Path dir = Paths.get(uploadRoot, datePath);

		// // 디렉터리가 없으면 생성한다(중첩 디렉터리 포함)
		Files.createDirectories(dir);

		// // 저장할 파일명을 UUID.확장자 형태로 생성한다
		String savedName = UUID.randomUUID().toString() + "." + fileExt;

		// // 최종 저장 경로를 만든다
		Path target = dir.resolve(savedName);

		// // 멀티파트 파일을 디스크로 저장한다
		mf.transferTo(target.toFile());

		// // 파일 메타데이터 객체를 생성한다
		FileVO vo = new FileVO();

		// // 원본 파일명을 세팅한다
		vo.setOriginName(originName);

		// // 저장 파일명을 세팅한다(DB 컬럼: save_name)
		vo.setSavedName(savedName);

		// // MIME 타입을 세팅한다(널일 경우 application/octet-stream 으로 대체)
		vo.setContentType(mf.getContentType() != null ? mf.getContentType() : "application/octet-stream");

		// // 파일 크기를 세팅한다
		vo.setFileSize(mf.getSize());

		// // 확장자를 세팅한다(DB 컬럼: file_ext NOT NULL 이므로 필수)
		vo.setFileExt(fileExt);

		// // 저장 상대 경로를 세팅한다(DB 컬럼: file_path NOT NULL 이므로 필수)
		vo.setFilePath(datePath);

		// // DAO에게 INSERT 를 위임한다(useGeneratedKeys 로 PK가 vo.id 로 채워진다)
		fileDAO.insertFile(vo);

		// // 생성된 PK 값을 반환한다
		return vo.getId();
	}

	// // PK 기준으로 물리 삭제한다(실제 행을 제거)
	@Transactional
	public int removeHard(long id) {
		// // DAO에게 삭제를 위임하고 영향 행 수를 반환한다
		return fileDAO.deleteFile(id);
	}

	// // PK 기준으로 논리 삭제한다(deleted=1 표시)
	@Transactional
	public int removeSoft(long id) {
		// // DAO에게 업데이트를 위임하고 영향 행 수를 반환한다
		return fileDAO.softDeleteFile(id);
	}

	// // 다운로드 성공 시 카운터를 1 증가시킨다
	@Transactional
	public int increaseDownloadCount(long id) {
		// // DAO에게 카운트 증가를 위임하고 영향 행 수를 반환한다
		return fileDAO.increaseDownloadCount(id);
	}

	// // 파일명에서 확장자를 소문자로 추출하는 유틸리티 메서드
	// // 파일명에 점이 없거나 마지막이 점이면 bin 을 반환한다
	private String extractExtLower(String filename) {
		// // 파일명이 널 또는 빈 문자열이면 bin 을 반환한다
		if (filename == null || filename.isEmpty())
			return "bin";
		// // 마지막 점 위치를 찾는다
		int dot = filename.lastIndexOf('.');
		// // 점이 없거나 끝이 점이면 bin 을 반환한다
		if (dot < 0 || dot == filename.length() - 1)
			return "bin";
		// // 점 다음부터 끝까지를 잘라 소문자로 반환한다
		return filename.substring(dot + 1).toLowerCase(Locale.ROOT);
	}
}
