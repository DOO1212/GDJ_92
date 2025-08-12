package com.winter.app.board.notice;

// 필요한 여러 클래스들을 가져옵니다.
import java.util.List;

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
import com.winter.app.commons.Pager;

// (중략)
import lombok.extern.slf4j.Slf4j;

// @Controller: 이 클래스가 Spring MVC의 '컨트롤러'임을 선언합니다.
@Controller
// @RequestMapping: 이 클래스 내부의 모든 메서드들은 "/notice/"로 시작하는 URL 요청을 처리합니다.
@RequestMapping(value="/notice/*")
// @Slf4j: Lombok 어노테이션으로, 'log'라는 이름의 로깅 객체를 자동으로 만들어줍니다. (예: log.info())
@Slf4j
public class NoticeController {

	// @Autowired: Spring이 미리 만들어 둔 NoticeService 객체(Bean)를 여기에 자동으로 주입(DI)해줍니다.
	@Autowired
	private NoticeService noticeService;
	
	// @Value: application.properties 파일의 'board.notice' 속성 값을 읽어와 name 변수에 저장합니다.
	@Value("${board.notice}")
	private String name;
	
	// @ModelAttribute: 이 컨트롤러의 어떤 메서드가 실행되든, 그 전에 이 메서드가 먼저 실행됩니다.
	// 여기서 반환된 값("notice")은 "board"라는 이름으로 모든 View(JSP)에 자동으로 전달됩니다.
	@ModelAttribute("board")
	public String getBoard() {
		return name;
	}

	// @GetMapping("list"): HTTP GET 방식의 "/notice/list" 요청을 처리합니다.
	public String list(@ModelAttribute Pager pager, Model model)throws Exception{
		// Pager 객체로 페이징/검색 정보를 받고, noticeService를 통해 게시글 목록을 가져옵니다.
		List<BoardVO> list = noticeService.list(pager);
		// model 객체에 "list"라는 이름으로 조회된 목록을 담아 View로 전달합니다.
		model.addAttribute("list", list);
		// "/WEB-INF/views/board/list.jsp" 파일을 사용자에게 보여줍니다.
		return "board/list";
	}
	
	// @GetMapping("detail"): GET 방식의 "/notice/detail" 요청을 처리합니다.
	public String detail(NoticeVO noticeVO, Model model)throws Exception{
		// 요청 파라미터(예: ?boardNum=1)가 NoticeVO 객체에 자동으로 담깁니다.
		BoardVO boardVO = noticeService.detail(noticeVO);
		model.addAttribute("vo", boardVO);
		return "board/detail";
	}
	
	// @GetMapping("add"): GET 방식의 "/notice/add" 요청을 처리합니다.
	// 단순히 글쓰기 폼(add.jsp)을 보여주는 역할을 합니다.
	public String insert()throws Exception{
		return "board/add";
	}
	
	// @PostMapping("add"): HTTP POST 방식의 "/notice/add" 요청을 처리합니다. (글쓰기 폼 제출)
	public String insert(NoticeVO noticeVO, MultipartFile [] attaches)throws Exception{
		// 폼 데이터(noticeVO)와 첨부파일(attaches)을 받아 Service에 넘겨 글 등록을 처리합니다.
		int result = noticeService.insert(noticeVO, attaches);
		// 글 등록 후, 목록 페이지로 리다이렉트(redirect)시킵니다. (새로고침 시 중복 등록 방지)
		return "redirect:./list";
	}
	
	// @GetMapping("update"): GET 방식의 "/notice/update" 요청을 처리합니다. (수정 페이지 이동)
	public String update(BoardVO noticeVO, Model model)throws Exception{
		// 기존 글 정보를 불러와서,
		BoardVO boardVO = noticeService.detail(noticeVO);
		// 모델에 담아,
		model.addAttribute("vo", boardVO);
		// 글쓰기 폼(add.jsp)을 재활용하여 사용자에게 보여줍니다.
		return "board/add";
	}
	
	// @PostMapping("update"): POST 방식의 "/notice/update" 요청을 처리합니다. (수정 폼 제출)
	public String update(NoticeVO noticeVO,MultipartFile [] attaches, Model model)throws Exception{
		int result = noticeService.update(noticeVO, attaches);
		// 수정 성공/실패 메시지와 다음 이동할 URL을 모델에 담습니다.
		String msg = "수정 실패";
		if(result>0) {
			msg="수정 성공";
		}
		String url="./detail?boardNum="+noticeVO.getBoardNum();
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		// 사용자에게 결과를 보여줄 공통 페이지(commons/result.jsp)로 이동합니다.
		return "commons/result";
	}
	
	// @PostMapping("delete"): POST 방식의 "/notice/delete" 요청을 처리합니다.
	public String delete(NoticeVO noticeVO, Model model)throws Exception{
		int result = noticeService.delete(noticeVO);
		// 삭제 결과에 따라 메시지와 URL을 담아 결과 페이지를 보여줍니다.
		String msg = "삭제 실패";
		if(result>0) {
			msg="삭제 성공";
		}
		String url="./list";
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		return "commons/result";
	}
	
	// @PostMapping("fileDelete"): POST 방식의 "/notice/fileDelete" 요청을 처리합니다. (AJAX용)
	// @ResponseBody: 이 어노테이션이 핵심! View(.jsp)를 찾지 않고, return 값을 그대로 응답 데이터로 보냅니다.
	@ResponseBody
	public int fileDelete(BoardFileVO boardFileVO, Model model)throws Exception{
		int result = noticeService.fileDelete(boardFileVO);
		// 성공 여부를 나타내는 숫자(int)가 클라이언트(브라우저)로 바로 전송됩니다.
		return result;
	}
	
	// @GetMapping("fileDown"): GET 방식의 "/notice/fileDown" 요청을 처리합니다. (파일 다운로드)
	public String fileDown(BoardFileVO boardFileVO, Model model)throws Exception{
		boardFileVO = noticeService.fileDetail(boardFileVO);
		model.addAttribute("vo", boardFileVO);
		// 'fileDownView'라는 이름의 커스텀 View를 찾아 파일 다운로드를 처리하게 합니다.
		return "fileDownView";
	}
	
	// 아래 두 메서드는 글쓰기/수정 폼에서 파일을 비동기(AJAX)로 업로드/삭제할 때 사용됩니다.

	// @PostMapping("boardFile"): 임시 파일을 서버에 업로드합니다.
	@ResponseBody
	public String boardFile(MultipartFile bf)throws Exception{
		log.info(bf.getOriginalFilename());
		// 서비스가 파일을 저장하고, 저장된 파일명을 클라이언트에 반환합니다.
		return noticeService.boardFile(bf);
	}
	
	// @PostMapping("boardFileDelete"): 임시 업로드된 파일을 삭제합니다.
	@ResponseBody
	public boolean boardFileDelete(String fileName)throws Exception{
		// 서비스에 파일 삭제를 요청하고, 성공 여부(boolean)를 클라이언트에 반환합니다.
		return noticeService.boardFileDelete(fileName);
	}
}