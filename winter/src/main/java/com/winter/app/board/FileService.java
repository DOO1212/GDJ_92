// 패키지 선언: 이 클래스가 속한 네임스페이스를 지정
package com.winter.app.board;

// 자바 표준 라이브러리: 파일 경로/폴더 생성, 리스트 반환 등에 사용
import java.io.File;
import java.util.List;

// 스프링: 설정값 주입과 서비스 빈 등록에 사용
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// 롬복: 생성자 자동 생성(생성자 주입)으로 의존성 주입 간결화
import lombok.RequiredArgsConstructor;

// 스프링 MVC: 업로드 파라미터 타입
import org.springframework.web.multipart.MultipartFile;

// 서비스 계층을 나타내는 애노테이션(스프링 빈 등록)
@Service
// final 필드에 대한 생성자를 자동으로 만들어 주입(권장: 생성자 주입)
@RequiredArgsConstructor
public class FileService {

    // DAO(매퍼 인터페이스) 의존성: DB CRUD를 담당
    private FileDAO fileDAO;

    // 업로드 루트 물리 경로 주입(예: D:/upload/)
    @Value("${app.upload}")
    private String baseUploadDir;

    // 게시판별 하위 폴더 경로 주입(예: notice/ , qna/ , member/)
    @Value("${board.notice}")
    private String noticeSubDir;

    // QnA 게시판 하위 폴더
    @Value("${board.qna}")
    private String qnaSubDir;

    // 회원 프로필 하위 폴더
    @Value("${board.member}")
    private String memberSubDir;

    // 업로드(기본형): 하위 폴더 없이 루트에 저장하고 DB 메타데이터를 기록
    public void uploadFile(MultipartFile mf) throws Exception {
        // 업로드 파일 존재 검사: 비어 있으면 예외
        if (mf == null || mf.isEmpty()) throw new IllegalArgumentException("업로드할 파일이 없습니다.");

        // 원본 파일명 추출
        String originName = mf.getOriginalFilename();

        // 확장자 추출(확장자가 없을 수도 있으므로 방어 코드)
        String ext = "";
        if (originName != null) {
            int idx = originName.lastIndexOf('.');
            if (idx > -1) ext = originName.substring(idx);
        }

        // 서버 저장 파일명: 충돌 방지를 위해 UUID 사용
        String uuidName = java.util.UUID.randomUUID().toString() + ext;

        // 물리 저장 디렉토리 객체 생성(루트 폴더)
        File dir = new File(baseUploadDir);

        // 폴더가 없으면 생성
        if (!dir.exists()) dir.mkdirs();

        // 실제 저장 대상 파일 객체 생성(루트/UUID파일명)
        File dest = new File(dir, uuidName);

        // 바이너리 데이터 디스크에 저장
        mf.transferTo(dest);

        // MIME 타입 결정(없으면 기본값 사용)
        String contentType = mf.getContentType();
        if (contentType == null || contentType.isBlank()) contentType = "application/octet-stream";

        // DB에 기록할 메타데이터 준비(savedName은 파일명만 저장)
        FileVO vo = new FileVO();
        vo.setOriginName(originName);
        vo.setSavedName(uuidName);
        vo.setFileSize(mf.getSize());
        vo.setContentType(contentType);

        // INSERT 실행(매퍼 XML의 insertFile과 연결)
        fileDAO.insertFile(vo);
    }

    // 업로드(확장형): 게시판 타입에 따라 하위 폴더에 저장하고, DB에는 상대경로로 기록
    public void uploadFileToBoard(MultipartFile mf, String boardType) throws Exception {
        // 업로드 파일 유효성 체크
        if (mf == null || mf.isEmpty()) throw new IllegalArgumentException("업로드할 파일이 없습니다.");

        // 원본 파일명 추출
        String originName = mf.getOriginalFilename();

        // 확장자 추출
        String ext = "";
        if (originName != null) {
            int idx = originName.lastIndexOf('.');
            if (idx > -1) ext = originName.substring(idx);
        }

        // 서버 저장 파일명(UUID)
        String uuidName = java.util.UUID.randomUUID().toString() + ext;

        // 게시판 타입을 하위 폴더명으로 변환(notice/qna/member/ 또는 "")
        String subDir = resolveSubDir(boardType);

        // 실제 저장 디렉토리(루트 + 하위 폴더)
        File dir = new File(baseUploadDir, subDir);

        // 디렉토리 보장(없으면 생성)
        if (!dir.exists()) dir.mkdirs();

        // 실제 저장 파일(폴더/UUID파일명)
        File dest = new File(dir, uuidName);

        // 디스크에 저장
        mf.transferTo(dest);

        // MIME 타입 보정
        String contentType = mf.getContentType();
        if (contentType == null || contentType.isBlank()) contentType = "application/octet-stream";

        // DB에는 상대경로로 저장(예: "notice/uuid.png")
        String savedRelative = normalizePathForDb(subDir, uuidName);

        // 메타데이터 구성
        FileVO vo = new FileVO();
        vo.setOriginName(originName);
        vo.setSavedName(savedRelative);
        vo.setFileSize(mf.getSize());
        vo.setContentType(contentType);

        // DB INSERT
        fileDAO.insertFile(vo);
    }

    // 파일 목록 조회: 매퍼 XML의 selectFileList 실행
    public List<FileVO> getFileList() {
        // DB에서 전체 목록을 가져와 컨트롤러로 반환
        return fileDAO.selectFileList();
    }

    // 파일 단건 조회: 매퍼 XML의 selectFile 실행
    public FileVO getFile(Long id) {
        // id로 단건을 조회하여 컨트롤러로 반환
        return fileDAO.selectFile(id);
    }

    // 파일 삭제(물리 삭제): 매퍼 XML에 <delete id="deleteFile"> 가 있어야 동작
    public int deleteFile(Long id) {
        // 삭제 실행 후 영향 행 수 반환
        return fileDAO.deleteFile(id);
    }

    // 내부 유틸: 게시판 타입 문자열을 하위 폴더명으로 매핑
    private String resolveSubDir(String boardType) {
        // null 방어: 지정이 없으면 루트에 저장
        if (boardType == null) return "";
        // 소문자 비교로 안전하게 분기
        String t = boardType.toLowerCase();
        if (t.equals("notice")) return noticeSubDir;
        if (t.equals("qna"))    return qnaSubDir;
        if (t.equals("member")) return memberSubDir;
        // 알 수 없는 타입이면 루트 사용
        return "";
    }

    // 내부 유틸: DB에는 경로 구분자를 슬래시로 통일하여 저장
    private String normalizePathForDb(String subDir, String fileName) {
        // 하위 폴더 + 파일명 결합(널 방어)
        String path = (subDir == null ? "" : subDir) + fileName;
        // 윈도우 역슬래시를 슬래시로 통일
        return path.replace('\\', '/');
    }
}
