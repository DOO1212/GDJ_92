// FileDAO.java
// // 목적: MyBatis 매퍼 XML의 각 SQL(id) 과 1:1로 연결되는 인터페이스
package com.winter.app.board;

// // 컬렉션 타입 반환을 위해 List 사용
import java.util.List;
// // MyBatis 매퍼 프록시 빈 생성을 위해 @Mapper 사용
import org.apache.ibatis.annotations.Mapper;

// // 스프링 컨테이너에 MyBatis 매퍼 프록시 빈을 등록한다
@Mapper
public interface FileDAO {

	// // 파일 메타데이터를 INSERT 한다 (매퍼 id: insertFile)
	// // 파라미터: FileVO (originName, savedName, contentType, fileSize, fileExt,
	// filePath 필수)
	// // 반환값: 영향받은 행 수(성공 시 1)
	int insertFile(FileVO vo);

	// // 삭제되지 않은 파일 목록을 조회한다 (매퍼 id: selectFileList)
	// // 파라미터: 없음
	// // 반환값: FileVO 리스트
	List<FileVO> selectFileList();

	// // PK 기준 단건을 조회한다 (매퍼 id: selectFile)
	// // 파라미터: id (upload_file.file_id)
	// // 반환값: FileVO (없으면 null)
	FileVO selectFile(long id);

	// // PK 기준 물리 삭제를 수행한다 (매퍼 id: deleteFile)
	// // 파라미터: id
	// // 반환값: 영향받은 행 수
	int deleteFile(long id);

	// // PK 기준 논리 삭제를 수행한다 (매퍼 id: softDeleteFile)
	// // 파라미터: id
	// // 반환값: 영향받은 행 수
	int softDeleteFile(long id);

	// // PK 기준 다운로드 횟수를 1 증가시킨다 (매퍼 id: increaseDownloadCount)
	// // 파라미터: id
	// // 반환값: 영향받은 행 수
	int increaseDownloadCount(long id);
}
