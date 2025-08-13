package com.winter.app.board.notice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.winter.app.board.BoardFileVO;
import com.winter.app.board.BoardVO;
import com.winter.app.commons.Pager;
import lombok.extern.slf4j.Slf4j;

@Controller
// @RequestMapping: 이 클래스의 모든 메서드에 대한 기본 URL 경로를 "/notice/"로 설정합니다.
@RequestMapping(value = "/notice/*")
// @Slf4j: Lombok을 통해 'log'라는 이름의 로깅 객체를 자동으로 생성합니다.
@Slf4j
public class NoticeController {

	// @Autowired: 스프링이 미리 만들어 둔 NoticeService 객체를 자동으로 주입합니다.
	@Autowired
	private NoticeService noticeService;

	// @Value: application.properties 파일의 'board.notice' 속성 값을 읽어와 주입합니다.
	@Value("${board.notice}")
	private String name;

	// @ModelAttribute: 컨트롤러의 어떤 메서드보다 먼저 실행되어 Model에 공통 값을 추가합니다.
	@ModelAttribute("board")
	public String getBoard() {
		return name; // 반환된 값("notice")이 "board"라는 이름으로 모든 View에 전달됩니다.
	}

	// @GetMapping("list"): HTTP GET 방식의 "/notice/list" 요청을 처리합니다.
	@GetMapping("list")
	public String list(Pager pager, Model model) throws Exception {
		// Pager 정보로 서비스 레이어를 호출해 해당 페이지의 게시글 목록을 가져옵니다.
		List<BoardVO> list = noticeService.list(pager);
		// model 객체에 "list"라는 이름으로 조회된 목록을 담아 View로 전달합니다.
		model.addAttribute("list", list);
		// 사용자에게 보여줄 View의 경로를 반환합니다. (e.g., /WEB-INF/views/board/list.jsp)
		return "board/list";
	}

	// @GetMapping("detail"): GET 방식의 "/notice/detail" 요청을 처리합니다.
	@GetMapping("detail")
	public String detail(NoticeVO noticeVO, Model model) throws Exception {
		// 요청 파라미터(boardNum)로 게시글의 상세 정보를 조회합니다.
		BoardVO boardVO = noticeService.detail(noticeVO);
		// 조회된 상세 정보를 "vo"라는 이름으로 모델에 담습니다.
		model.addAttribute("vo", boardVO);
		// 상세 페이지 View를 반환합니다.
		return "board/detail";
	}

	// @GetMapping("add"): GET 방식의 "/notice/add" 요청을 처리합니다.
	@GetMapping("add")
	public String add() throws Exception {
		// 별도 처리 없이 글쓰기 폼(add.jsp) 페이지만을 보여줍니다.
		return "board/add";
	}

	// @PostMapping("add"): POST 방식의 "/notice/add" 요청을 처리합니다. (글쓰기 폼 제출)
	@PostMapping("add")
	public String add(NoticeVO noticeVO, MultipartFile[] attaches) throws Exception {
		// 폼 데이터(noticeVO)와 첨부파일(attaches)로 DB에 글을 등록합니다.
		int result = noticeService.insert(noticeVO, attaches);
		// 처리가 완료되면 목록 페이지로 리다이렉트(redirect) 시킵니다.
		return "redirect:./list";
	}

	// @GetMapping("update"): GET 방식의 "/notice/update" 요청을 처리합니다. (수정 페이지 이동)
	@GetMapping("update")
	public String update(BoardVO boardVO, Model model) throws Exception {
		// 수정할 게시물의 기존 정보를 불러옵니다.
		boardVO = noticeService.detail(boardVO);
		// 불러온 정보를 모델에 담아 View로 전달합니다.
		model.addAttribute("vo", boardVO);
		// 글쓰기 폼(add.jsp)을 수정 페이지로 재활용합니다.
		return "board/add";
	}

	// @PostMapping("update"): POST 방식의 "/notice/update" 요청을 처리합니다. (수정 폼 제출)
	@PostMapping("update")
	public String update(NoticeVO noticeVO, MultipartFile[] attaches) throws Exception {
		// 수정된 내용과 파일을 DB에 반영합니다.
		int result = noticeService.update(noticeVO, attaches);
		// 수정이 완료되면 해당 글의 상세 페이지로 리다이렉트합니다.
		return "redirect:./detail?boardNum=" + noticeVO.getBoardNum();
	}

	// @PostMapping("delete"): POST 방식의 "/notice/delete" 요청을 처리합니다.
	@PostMapping("delete")
	public String delete(NoticeVO noticeVO) throws Exception {
		// 해당 게시물을 DB에서 삭제합니다.
		int result = noticeService.delete(noticeVO);
		// 삭제 후, 목록 페이지로 리다이렉트합니다.
		return "redirect:./list";
	}

	// @ResponseBody: View를 거치지 않고, 반환값을 그대로 응답 데이터로 전송합니다. (주로 AJAX용)
	// @PostMapping("fileDelete"): POST 방식의 "/notice/fileDelete" 요청을 처리합니다.
	@PostMapping("fileDelete")
	@ResponseBody
	public int fileDelete(BoardFileVO boardFileVO) throws Exception {
		// 첨부파일 번호로 파일을 삭제합니다.
		int result = noticeService.fileDelete(boardFileVO);
		// 성공 여부를 나타내는 숫자(int)가 클라이언트(브라우저)로 바로 전송됩니다.
		return result;
	}
}F