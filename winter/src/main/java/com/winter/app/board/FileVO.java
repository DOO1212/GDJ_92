// FileVO.java
// // 목적: upload_file 테이블의 한 행을 표현하는 값 객체(VO)
// // 매핑: resultMap(FileMap)에서 DB 컬럼(column) → VO 필드(property)로 주입됨
package com.winter.app.board;

// // 롬복이 getter/setter를 생성함(Annotation Processing 활성화 필요)
import lombok.Getter;
import lombok.Setter;
// // 날짜/시간 컬럼 매핑을 위해 사용
import java.time.LocalDateTime;

@Getter
@Setter
public class FileVO {

    // // PK 컬럼 매핑: upload_file.file_id → id
    private Long id;

    // // 원본 파일명 컬럼 매핑: upload_file.origin_name → originName
    private String originName;

    // // 서버 저장 파일명 컬럼 매핑: upload_file.save_name → savedName
    private String savedName;

    // // MIME 타입 컬럼 매핑: upload_file.content_type → contentType
    private String contentType;

    // // 파일 크기(바이트) 컬럼 매핑: upload_file.file_size → fileSize
    private Long fileSize;

    // // 파일 확장자 컬럼 매핑: upload_file.file_ext → fileExt
    private String fileExt;

    // // 서버 저장 경로(상대경로 권장) 컬럼 매핑: upload_file.file_path → filePath
    private String filePath;

    // // 다운로드 횟수 컬럼 매핑: upload_file.download_count → downloadCount
    private Integer downloadCount;

    // // 삭제 여부(0:정상,1:삭제) 컬럼 매핑: upload_file.deleted → deleted
    private Integer deleted;

    // // 등록일 컬럼 매핑: upload_file.reg_date → regDate
    private LocalDateTime regDate;

    // // 수정일 컬럼 매핑: upload_file.mod_date → modDate
    private LocalDateTime modDate;
}
