package com.winter.app.board.qna;

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
// @RequestMapping: 이 클래스의 모든 메서드에 대한 기본 URL 경로를 "/qna/"로 설정합니다.
@RequestMapping("/qna/*")
// @Slf4j: Lombok을 통해 'log'라는 이름의 로깅 객체를 자동으로 생성합니다.
@Slf4j
public class QnaController {

	// @Autowired: 스프링이 관리하는 QnaService 타입의 객체를 자동으로 주입(연결)합니다.
	@Autowired
	private QnaService qnaService;

	// @Value: application.properties 파일에서 'board.qna' 키의 값을 찾아 주입합니다.
	@Value("${board.qna}")
	private String name;

	// @ModelAttribute: 이 컨트롤러의 모든 요청 처리 전에 실행되어 Model에 공통 값을 추가합니다.
	@ModelAttribute("board")
	public String getBoard() {
		return name; // 반환된 값("qna")이 "board"라는 이름으로 모든 View에 전달됩니다.
	}

	// @GetMapping("list"): HTTP GET 방식의 "/qna/list" 요청을 처리합니다.
	@GetMapping("list")
	public String list(Pager pager, Model model) throws Exception {
		// 서비스 레이어를 호출하여 게시물 목록 데이터를 가져옵니다.
		List<BoardVO> ar = qnaService.list(pager);
		// model 바구니에 "list"라는 이름으로 조회된 목록 데이터를 담습니다.
		model.addAttribute("list", ar);
		// 사용자에게 보여줄 View의 경로를 반환합니다. (e.g., /WEB-INF/views/board/list.jsp)
		return "board/list";
	}

	// @GetMapping("detail"): GET 방식의 "/qna/detail" 요청을 처리합니다.
	@GetMapping("detail")
	public String detail(QnaVO qnaVO, Model model) throws Exception {
		// 요청 파라미터(boardNum)로 게시글의 상세 정보를 조회합니다.
		BoardVO boardVO = qnaService.detail(qnaVO);
		// 조회된 상세 정보를 "vo"라는 이름으로 모델에 담습니다.
		model.addAttribute("vo", boardVO);
		// 상세 페이지 View를 반환합니다.
		return "board/detail";
	}

	// @GetMapping("add"): GET 방식의 "/qna/add" 요청을 처리합니다. (원본글 작성)
	@GetMapping("add")
	public String add() throws Exception {
		// 별도 처리 없이 글쓰기 폼(add.jsp) 페이지만을 보여줍니다.
		return "board/add";
	}

	// @PostMapping("add"): POST 방식의 "/qna/add" 요청을 처리합니다. (원본글 폼 제출)
	@PostMapping("add")
	public String add(QnaVO qnaVO, MultipartFile[] attaches) throws Exception {
		// 폼 데이터(qnaVO)와 첨부파일(attaches)로 DB에 글을 등록합니다.
		int result = qnaService.insert(qnaVO, attaches);
		// 처리가 완료되면 목록 페이지로 리다이렉트(redirect) 시킵니다.
		return "redirect:./list";
	}

	// @GetMapping("update"): GET 방식의 "/qna/update" 요청을 처리합니다. (수정 페이지 이동)
	@GetMapping("update")
	public String update(QnaVO qnaVO, Model model) throws Exception {
		// 수정할 게시물의 기존 정보를 불러옵니다.
		BoardVO boardVO = qnaService.detail(qnaVO);
		// 불러온 정보를 모델에 담아 View로 전달합니다.
		model.addAttribute("vo", boardVO);
		// 글쓰기 폼(add.jsp)을 수정 페이지로 재활용합니다.
		return "board/add";
	}

	// @PostMapping("update"): POST 방식의 "/qna/update" 요청을 처리합니다. (수정 폼 제출)
	@PostMapping("update")
	public String update(QnaVO qnaVO, MultipartFile[] attaches) throws Exception {
		// 수정된 내용과 파일을 DB에 반영합니다.
		int result = qnaService.update(qnaVO, attaches);
		// 수정이 완료되면 해당 글의 상세 페이지로 리다이렉트합니다.
		return "redirect:./detail?boardNum=" + qnaVO.getBoardNum();
	}

	// @PostMapping("delete"): POST 방식의 "/qna/delete" 요청을 처리합니다.
	@PostMapping("delete")
	public String delete(QnaVO qnaVO) throws Exception {
		// 해당 게시물을 DB에서 삭제합니다.
		int result = qnaService.delete(qnaVO);
		// 삭제 후, 목록 페이지로 리다이렉트합니다.
		return "redirect:./list";
	}

	// @GetMapping("reply"): GET 방식의 "/qna/reply" 요청을 처리합니다. (답글 폼으로 이동)
	@GetMapping("reply")
	public String reply(QnaVO qnaVO, Model model) throws Exception {
		// 답글을 달 원본글의 정보(boardNum 등)를 View로 전달하기 위해 모델에 담습니다.
		model.addAttribute("vo", qnaVO);
		// 글쓰기 폼(add.jsp)을 답글 작성 폼으로 재활용합니다.
		return "board/add";
	}

	// @PostMapping("reply"): POST 방식의 "/qna/reply" 요청을 처리합니다. (답글 폼 제출)
	@PostMapping("reply")
	public String reply(QnaVO qnaVO, MultipartFile[] attaches) throws Exception {
		// 서비스의 답글 처리 로직을 호출합니다. (원본글 정보와 답글 내용 포함)
		int result = qnaService.reply(qnaVO, attaches);
		// 답글 등록 후, 목록 페이지로 리다이렉트합니다.
		return "redirect:./list";
	}

	// @ResponseBody: View를 거치지 않고, 반환값을 그대로 HTTP 응답 본문에 담아 전송합니다. (주로 AJAX용)
	// @PostMapping("fileDelete"): POST 방식의 "/qna/fileDelete" 요청을 처리합니다.
	@PostMapping("fileDelete")
	@ResponseBody
	public int fileDelete(BoardFileVO boardFileVO) throws Exception {
		// 첨부파일 번호로 파일을 삭제합니다.
		int result = qnaService.fileDelete(boardFileVO);
		// 성공 여부를 나타내는 숫자(int)가 클라이언트(브라우저)로 바로 전송됩니다.
		return result;
	}
}