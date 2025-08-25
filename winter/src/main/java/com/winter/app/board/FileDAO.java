// 패키지 선언: DAO(매퍼 인터페이스)가 속한 네임스페이스
package com.winter.app.board;

// import: 목록 조회 반환타입(List)
import java.util.List;
// import: MyBatis 매퍼 애노테이션(스프링 빈으로 등록)
import org.apache.ibatis.annotations.Mapper;
// import: 단일 파라미터 이름을 XML에서 #{id} 로 정확히 바인딩하기 위해 사용
import org.apache.ibatis.annotations.Param;

// MyBatis 매퍼 인터페이스임을 표시(스프링이 프록시 빈으로 등록)
@Mapper
// 파일 메타데이터 CRUD를 담당하는 DAO(Mapper) 인터페이스
public interface FileDAO {

    // 파일 메타데이터 저장
    // 파라미터: FileVO(원본명, 저장명, 크기, 타입)
    // 반환값: 영향 행 수(성공 시 1)
    int insertFile(FileVO vo);

    // 파일 전체 목록 조회
    // 파라미터: 없음
    // 반환값: FileVO 리스트(최신순 정렬은 XML에서 처리)
    List<FileVO> selectFileList();

    // 파일 단건 조회
    // 파라미터: id(PK) — @Param("id")로 XML의 #{id} 바인딩 보장
    // 반환값: FileVO(없으면 null)
    FileVO selectFile(@Param("id") Long id);

    // 파일 삭제(물리 삭제 버전)
    // 파라미터: id(PK) — @Param("id")로 XML의 #{id} 바인딩 보장
    // 반환값: 영향 행 수(성공 시 1)
    int deleteFile(@Param("id") Long id);
}
