// 패키지 선언: 컨트롤러 클래스의 네임스페이스
package com.winter.app.board;

// 자바 표준: 파일 스트림/경로/인코딩
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.List;

// 스프링: 의존성 주입, 컨트롤러, 뷰 반환에 사용
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

// 스프링 MVC: 라우팅과 파라미터 바인딩
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 스프링 MVC: 업로드 파라미터 타입
import org.springframework.web.multipart.MultipartFile;

// 서블릿: HTTP 응답 스트림 제어
import jakarta.servlet.http.HttpServletResponse;

// 스프링 MVC 컨트롤러 선언(뷰를 반환)
@Controller
// 공통 URL prefix 지정: /file 로 시작하는 요청을 이 컨트롤러가 처리
@RequestMapping("/file")
public class FileController {

    // 서비스 의존성 주입(비즈니스 로직 호출)
    @Autowired
    private FileService fileService;

    // 업로드 루트 물리 경로 주입(예: D:/upload/)
    @Value("${app.upload}")
    private String baseUploadDir;

    // 업로드 폼 페이지: 단순히 JSP 뷰로 이동
    @GetMapping("/uploadForm")
    public String uploadForm() {
        // /WEB-INF/views/file/uploadForm.jsp 로 포워드
        return "file/uploadForm";
    }

    // 업로드 처리(기본형): 루트 폴더에 저장
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, Model model) {
        // 업로드 로직 실행 후 결과 메시지 모델에 담기
        try {
            // 서비스 호출: 루트(D:/upload/)에 저장하고 DB에 기록
            fileService.uploadFile(file);
            // 성공 메시지
            model.addAttribute("message", "업로드 성공");
        } catch (Exception e) {
            // 실패 시 스택 출력(개발 중 참고) 및 사용자 메시지
            e.printStackTrace();
            model.addAttribute("message", "업로드 실패: " + e.getMessage());
        }
        // 결과 뷰 반환
        return "file/uploadResult";
    }

    // 업로드 처리(확장형): 게시판 타입별 하위 폴더에 저장(notice/qna/member)
    @PostMapping("/uploadTo")
    public String uploadToBoard(@RequestParam("file") MultipartFile file,
                                @RequestParam("boardType") String boardType,
                                Model model) {
        // 게시판 타입 파라미터와 함께 업로드 처리
        try {
            // 서비스 호출: 하위 폴더를 선택해 저장하고 DB에 상대경로 기록
            fileService.uploadFileToBoard(file, boardType);
            // 성공 메시지
            model.addAttribute("message", "업로드 성공(" + boardType + ")");
        } catch (Exception e) {
            // 실패 메시지
            e.printStackTrace();
            model.addAttribute("message", "업로드 실패: " + e.getMessage());
        }
        // 결과 뷰 반환
        return "file/uploadResult";
    }

    // 파일 목록 페이지: DB에서 목록을 가져와 JSP로 전달
    @GetMapping("/list")
    public String fileList(Model model) {
        // 서비스에서 VO 리스트 조회
        List<FileVO> list = fileService.getFileList();
        // JSP에서 ${files}로 접근 가능하게 모델 저장
        model.addAttribute("files", list);
        // 목록 뷰 반환
        return "file/list";
    }

    // 파일 다운로드: id로 DB/디스크를 조회하고 바이너리 응답으로 전송
    @GetMapping("/download/{id}")
    public void download(@PathVariable Long id, HttpServletResponse response) throws Exception {
        // 1) DB에서 파일 메타데이터 조회
        FileVO vo = fileService.getFile(id);

        // 2) 조회 결과 없으면 404 반환
        if (vo == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 3) 실제 파일 경로 조립(루트 경로 + savedName)
        File file = Paths.get(baseUploadDir, vo.getSavedName()).toFile();

        // 4) 물리 파일이 존재하지 않으면 404
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 5) 콘텐츠 타입 결정(없으면 기본값)
        String contentType = vo.getContentType();
        if (contentType == null || contentType.isBlank()) contentType = "application/octet-stream";

        // 6) 응답 헤더 설정: 다운로드로 인식하도록 지정
        response.setContentType(contentType);
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + URLEncoder.encode(vo.getOriginName(), "UTF-8") + "\";");
        response.setContentLengthLong(file.length());

        // 7) 파일 스트림을 응답 본문으로 흘려 보내기
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            // 버퍼 크기를 넉넉히 잡아 전송 성능을 확보
            byte[] buf = new byte[8192];
            int len;
            // EOF(-1) 까지 반복해서 전송
            while ((len = fis.read(buf)) != -1) os.write(buf, 0, len);
        }
    }

    // 파일 삭제(물리 삭제): 삭제 후 목록으로 리다이렉트
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        // 서비스 호출: DB에서 삭제 수행(매퍼에 deleteFile 필요)
        fileService.deleteFile(id);
        // PRG 패턴으로 목록 페이지 재요청
        return "redirect:/file/list";
    }
}
