// FileController.java
// 목적: 사용자의 요청을 받아 Service를 호출하고, 모델을 뷰(JSP)에 전달한다
package com.winter.app.board;

// 업로드/다운로드에 필요한 타입들을 import 한다
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// 목록/상세 화면에 데이터를 전달하기 위해 사용한다
import org.springframework.ui.Model;

// 파일 다운로드 응답 생성을 위해 사용한다
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

// 스프링 MVC 컨트롤러/매핑을 사용한다
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 업로드 파라미터 바인딩과 리다이렉트 메시지에 사용한다
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// 설정값 주입과 생성자 주입을 위해 사용한다
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;

// 스프링 MVC 컨트롤러로 등록한다
@Controller
// 공통 URL prefix 를 지정한다(/file 로 시작)
@RequestMapping("/file")
// final 필드에 대한 생성자를 자동 생성한다
@RequiredArgsConstructor
public class FileController {

	// 비즈니스 로직을 수행하는 서비스 빈을 주입받는다
	private final FileService fileService;

	// 업로드 루트 경로를 주입받는다(application.properties 의 app.upload)
	@Value("${app.upload}")
	private String uploadRoot;

	// 파일 목록 화면을 처리한다(GET /file/list)
	@GetMapping("/list")
	public String list(Model model) {
		// 서비스에서 파일 목록을 조회한다
		model.addAttribute("list", fileService.list());
		// 뷰 이름을 반환한다(다음 단계에서 /WEB-INF/views/file/list.jsp 를 만들 것이다)
		return "file/list";
	}

	// 업로드 폼 화면을 반환한다(GET /file/upload)
	@GetMapping("/upload")
	public String uploadForm() {
		// 업로드 폼 JSP 로 이동한다
		return "file/uploadForm";
	}

	// 파일 업로드를 처리한다(POST /file/upload)
	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes ra) throws IOException {
		// 서비스에 업로드+DB 저장을 위임하고 생성된 PK 를 받는다
		long id = fileService.uploadAndSave(file);
		// 업로드 성공 메시지를 플래시 속성으로 저장한다
		ra.addFlashAttribute("msg", "업로드 완료 (id=" + id + ")");
		// 목록으로 리다이렉트한다
		return "redirect:/file/list";
	}

	// 파일 상세 화면을 처리한다(GET /file/{id})
	@GetMapping("/{id}")
	public String detail(@PathVariable("id") long id, Model model) {
		// 서비스에서 단건 정보를 조회한다
		FileVO vo = fileService.detail(id);
		// 조회 결과를 모델에 담는다
		model.addAttribute("file", vo);
		// 상세 JSP 로 이동한다
		return "file/detail";
	}

	// 파일을 다운로드한다(GET /file/download/{id})
	@GetMapping("/download/{id}")
	public ResponseEntity<InputStreamResource> download(@PathVariable("id") long id) throws IOException {
		// 다운로드 대상 메타데이터를 조회한다
		FileVO vo = fileService.detail(id);
		// 파일이 존재하지 않으면 404 를 반환한다
		if (vo == null) {
			// 본문이 없는 404 응답을 생성한다
			return ResponseEntity.notFound().build();
		}
		// 실제 저장 파일의 절대 경로를 계산한다(루트/상대경로/저장파일명)
		Path path = Paths.get(uploadRoot, vo.getFilePath(), vo.getSavedName());
		// 파일이 디스크에 없으면 404 를 반환한다
		if (Files.notExists(path)) {
			// 본문이 없는 404 응답을 생성한다
			return ResponseEntity.notFound().build();
		}
		// 콘텐츠 타입을 결정한다(없으면 이진 기본값을 사용한다)
		String contentType = (vo.getContentType() != null) ? vo.getContentType() : "application/octet-stream";
		// 다운로드 파일명을 브라우저 호환을 위해 인코딩한다
		String encodedName = URLEncoder.encode(vo.getOriginName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
		// Content-Disposition 헤더를 구성한다(파일명/파일명* 둘 다 세팅)
		HttpHeaders headers = new HttpHeaders();
		// 다운로드로 동작하도록 attachment 를 설정한다
		headers.add(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + vo.getOriginName() + "\"; filename*=UTF-8''" + encodedName);
		// 파일 길이를 설정한다
		headers.setContentLength(Files.size(path));
		// 서비스에 다운로드 카운트를 증가시키도록 요청한다
		fileService.increaseDownloadCount(id);
		// 입력 스트림을 열어 리소스를 생성한다
		InputStream is = Files.newInputStream(path);
		// ResponseEntity 로 파일 스트림을 반환한다
		return ResponseEntity.ok()
				// MIME 타입을 설정한다
				.contentType(MediaType.parseMediaType(contentType))
				// 헤더를 설정한다
				.headers(headers)
				// 본문으로 스트림 리소스를 보낸다
				.body(new InputStreamResource(is));
	}

	// 파일을 논리 삭제한다(POST /file/delete/soft?id=..)
	@PostMapping("/delete/soft")
	public String deleteSoft(@RequestParam("id") long id, RedirectAttributes ra) {
		// 서비스에 논리 삭제를 요청한다
		int rows = fileService.removeSoft(id);
		// 결과 메시지를 저장한다
		ra.addFlashAttribute("msg", rows == 1 ? "삭제 처리되었습니다" : "삭제 대상이 없습니다");
		// 목록으로 리다이렉트한다
		return "redirect:/file/list";
	}

	// // 파일을 물리 삭제한다(POST /file/delete/hard?id=..)
	@PostMapping("/delete/hard")
	public String deleteHard(@RequestParam("id") long id, RedirectAttributes ra) {
		// 서비스에 물리 삭제를 요청한다
		int rows = fileService.removeHard(id);
		// 결과 메시지를 저장한다
		ra.addFlashAttribute("msg", rows == 1 ? "영구 삭제되었습니다" : "삭제 대상이 없습니다");
		// 목록으로 리다이렉트한다
		return "redirect:/file/list";
	}
}
