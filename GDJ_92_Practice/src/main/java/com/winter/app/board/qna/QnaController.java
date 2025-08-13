package com.winter.app.board.qna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.winter.app.board.BoardFileVO;
import com.winter.app.board.BoardVO;
import com.winter.app.board.notice.NoticeVO;
import com.winter.app.commons.Pager;

import lombok.extern.slf4j.Slf4j;

@Controller
// @RequestMapping: 이 클래스 내부의 모든 메서드는 "/qna/"로 시작하는 URL 요청을 처리합니다.
@RequestMapping("/qna/*")
// @Slf4j: 'log'라는 이름의 로깅 객체를 자동으로 만들어줍니다.
@Slf4j
public class QnaController {

	@Autowired
	private QnaService qnaService;

	// @Value: application.properties의 'board.qna' 값을 읽어와 name 변수에 저장합니다.
	@Value("${board.qna}")
	private String name;

	// @ModelAttribute: 이 컨트롤러의 모든 요청 처리 전에 실행되어,
	// "board"라는 이름으로 "qna" 값을 모든 View(JSP)에 전달합니다.
	@ModelAttribute("board")
	public String getBoard() {
		return name;
	}

	// @GetMapping("list"): GET 방식의 "/qna/list" 요청을 처리합니다.
	public String list(Pager pager, Model model) throws Exception {
		// model에 pager와 list 데이터를 담아 View로 전달합니다.
		model.addAttribute("pager", pager);
		model.addAttribute("list", qnaService.list(pager));
		// "board/list.jsp" 뷰를 사용자에게 보여줍니다.
		return "board/list";
	}

	// @GetMapping("detail"): GET 방식의 "/qna/detail" 요청을 처리합니다.
	public String detail(QnaVO qnaVO, Model model) throws Exception {
		model.addAttribute("vo", qnaService.detail(qnaVO));
		return "board/detail";
	}

	// ==========================================================
	// Q&A 게시판의 핵심 기능: 답글 (Reply)
	// ==========================================================

	// @GetMapping("reply"): GET 방식의 "/qna/reply" 요청을 처리합니다. (답글 작성 폼으로 이동)
	public String reply(QnaVO qnaVO, Model model) throws Exception {
		// 파라미터로 받은 원본글 정보(qnaVO)를 모델에 담아 View로 전달합니다.
		// View에서는 이 정보를 이용해 "어떤 글에 대한 답글인지" 표시하거나,
		// 답글 처리에 필요한 원본글의 번호(boardNum)를 hidden input으로 가지고 있게 됩니다.
		model.addAttribute("vo", qnaVO);
		// 글쓰기 폼(add.jsp)을 답글 작성 폼으로 재활용합니다.
		return "board/add";
	}

	// @PostMapping("reply"): POST 방식의 "/qna/reply" 요청을 처리합니다. (답글 폼 제출)
	public String reply(QnaVO qnaVO) throws Exception {
		// 답글 내용과 원본글 정보가 담긴 qnaVO를 Service로 넘겨 답글 등록 로직을 수행합니다.
		int result = qnaService.reply(qnaVO);
		// 답글 등록 후, 목록 페이지로 리다이렉트합니다.
		return "redirect:./list";
	}

	// ----------------------------------------------------------
	// 이하 공지사항(Notice)과 유사한 기본 CRUD 기능들
	// ----------------------------------------------------------

	// @GetMapping("add"): 원본글 작성 폼으로 이동합니다.
	public String insert() throws Exception {
		return "board/add";
	}

	// @PostMapping("add"): 원본글을 등록합니다.
	public String insert(QnaVO qnaVO, MultipartFile[] attaches) throws Exception {
		int result = qnaService.insert(qnaVO, attaches);
		return "redirect:./list";
	}

	// @GetMapping("update"): 글 수정 폼으로 이동합니다.
	public String update(BoardVO noticeVO, Model model) throws Exception {
		BoardVO boardVO = qnaService.detail(noticeVO);
		model.addAttribute("vo", boardVO);
		return "board/add";
	}

	// @PostMapping("update"): 글 수정을 처리합니다.
	// !! 참고: 파라미터 타입이 NoticeVO로 되어있으나, QnaVO를 사용하는 것이 더 정확해 보입니다.
	public String update(NoticeVO noticeVO, MultipartFile[] attaches, Model model) throws Exception {
		int result = qnaService.update(noticeVO, attaches);
		String msg = "수정 실패";
		if (result > 0) {
			msg = "수정 성공";
		}
		String url = "./detail?boardNum=" + noticeVO.getBoardNum();
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		return "commons/result";
	}

	// @PostMapping("delete"): 글 삭제를 처리합니다.
	// !! 참고: 파라미터 타입이 NoticeVO로 되어있으나, QnaVO를 사용하는 것이 더 정확해 보입니다.
	public String delete(NoticeVO noticeVO, Model model) throws Exception {
		int result = qnaService.delete(noticeVO);
		String msg = "삭제 실패";
		if (result > 0) {
			msg = "삭제 성공";
		}
		String url = "./list";
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		return "commons/result";
	}

	// @PostMapping("fileDelete"): AJAX를 이용해 첨부파일 하나를 삭제합니다.
	@ResponseBody
	public int fileDelete(BoardFileVO boardFileVO, Model model) throws Exception {
		return qnaService.fileDelete(boardFileVO);
	}

	// @GetMapping("fileDown"): 파일을 다운로드합니다.
	public String fileDown(BoardFileVO boardFileVO, Model model) throws Exception {
		boardFileVO = qnaService.fileDetail(boardFileVO);
		model.addAttribute("vo", boardFileVO);
		return "fileDownView";
	}
}