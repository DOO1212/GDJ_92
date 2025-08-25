// 패키지 선언: 이 클래스가 속한 네임스페이스(프로젝트 구조 상 board 도메인)
package com.winter.app.board;

// import: DB의 DATETIME을 자바 시간 타입으로 받기 위한 클래스(LocalDateTime 권장)
import java.time.LocalDateTime;
// import: 만약 LocalDateTime 매핑이 환경상 안 될 경우 임시로 사용할 수 있는 대안 타입(Timestamp)
// import java.sql.Timestamp;

// Lombok: 게터/세터/디버깅용 toString 자동 생성으로 보일러플레이트 제거
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
// VO 클래스: upload_file 테이블의 한 행(row)을 담는 값 객체
public class FileVO {

    // PK 컬럼 매핑: upload_file.id
    private Long id;

    // 원본 파일명 컬럼 매핑: upload_file.origin_name
    private String originName;

    // 서버 저장 파일명(또는 하위 폴더 포함 상대경로) 매핑: upload_file.saved_name
    // 예) "notice/uuid.png" 또는 "uuid.png"
    private String savedName;

    // 파일 크기(바이트) 컬럼 매핑: upload_file.file_size
    private Long fileSize;

    // MIME 타입 컬럼 매핑: upload_file.content_type
    // 예) "image/png", "application/pdf"
    private String contentType;

    // 등록 일시 컬럼 매핑: upload_file.reg_date
    // 기본은 LocalDateTime 사용을 권장(스프링+마이바티스에서 타입 핸들러가 지원됨)
    // 만약 환경에 따라 매핑 이슈가 있다면 java.sql.Timestamp로 임시 전환 가능
    private LocalDateTime regDate;
    // private Timestamp regDate; // 필요 시 대체 사용
}
