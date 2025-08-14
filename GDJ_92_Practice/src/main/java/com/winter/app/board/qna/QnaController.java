package com.winter.app.board.qna;

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
import com.winter.app.board.notice.NoticeVO;
import com.winter.app.commons.Pager;

import lombok.extern.slf4j.Slf4j;

/**
 * @Controller
 * 이 클래스가 Spring의 Controller 역할을 하도록 지정합니다.
 * 클라이언트(브라우저)의 요청을 받아 비즈니스 로직을 호출하고, 응답할 View를 결정합니다.
 *
 * @RequestMapping("/qna/*")
 * /qna/ 로 시작하는 모든 URL 요청을 이 컨트롤러가 처리하도록 매핑합니다.
 *
 * @Slf4j
 * Lombok 라이브러리를 통해 로그 객체(log)를 자동으로 생성해 줍니다. log.info(), log.error() 등을 사용할 수 있습니다.
 */
@Controller
@RequestMapping("/qna/*")
@Slf4j
public class QnaController {
	
	@Autowired
	private QnaService qnaService;
	
	/**
	 * @Value("${board.qna}")
	 * application.properties 파일에 정의된 'board.qna' 속성 값을 찾아 이 변수(name)에 주입합니다.
	 * 여기서는 "QnA" 라는 문자열이 주입됩니다.
	 */
	@Value("${board.qna}")
	private String name;
	
	/**
	 * @ModelAttribute("board")
	 * 이 컨트롤러 내의 모든 메서드가 실행되기 전에 먼저 실행됩니다.
	 * 여기서 반환된 값("QnA")은 "board"라는 이름으로 Model에 자동으로 추가됩니다.
	 * 따라서 이 컨트롤러가 반환하는 모든 JSP 페이지에서는 ${board} 라는 이름으로 "QnA" 값을 사용할 수 있습니다.
	 * (예: 게시판 제목 표시 등)
	 */
	@ModelAttribute("board")
	public String getBoard() {
		return name;
	}
	
	/**
	 * Q&A 게시판 목록 페이지를 요청합니다. (GET /qna/list)
	 * @param pager 페이징 및 검색 관련 파라미터(page, kind, search)를 담고 있는 객체입니다. Spring이 URL의 쿼리 스트링을 보고 자동으로 생성 및 주입해 줍니다.
	 * @param model Controller에서 View(JSP)로 데이터를 전달하기 위한 객체입니다.
	 * @return 렌더링할 JSP 파일의 경로 ("board/list")
	 */
	@GetMapping("list")
	public String list(Pager pager, Model model)throws Exception{
		
		model.addAttribute("pager", qnaService.list(pager)); // 페이징 계산이 완료된 pager
		model.addAttribute("list", qnaService.list(pager)); // 조회된 게시글 목록
		
		return "board/list";
	}
	
	/**
	 * 게시글 상세 페이지를 요청합니다. (GET /qna/detail)
	 * @param qnaVO 상세 조회할 게시글 번호(boardNum)를 담고 있는 객체입니다.
	 * @param model View로 데이터를 전달하기 위한 객체입니다.
	 * @return 렌더링할 JSP 파일의 경로 ("board/detail")
	 */
	@GetMapping("detail")
	public String detail(QnaVO qnaVO, Model model)throws Exception{
		
		model.addAttribute("vo", qnaService.detail(qnaVO)); // 조회된 게시글 상세 정보
		return "board/detail";
	}
	
	/**
	 * 답글 작성 폼 페이지를 요청합니다. (GET /qna/reply)
	 * @param qnaVO 부모 글의 정보(boardNum)를 담고 있습니다. 이 정보는 답글 작성 폼에서 어떤 글에 대한 답글인지 알려주는 데 사용됩니다.
	 * @param model View로 데이터를 전달하기 위한 객체입니다.
	 * @return 렌더링할 JSP 파일의 경로 ("board/add")
	 */
	@GetMapping("reply")
	public String reply(QnaVO qnaVO, Model model)throws Exception{
		model.addAttribute("vo", qnaVO); // 부모 글 정보를 'vo'라는 이름으로 JSP에 전달
		
		return "board/add"; // 글 작성/답글/수정 폼은 하나의 JSP를 공유
	}
	
	/**
	 * 답글 데이터를 DB에 저장 처리합니다. (POST /qna/reply)
	 * @param qnaVO 폼에서 입력된 답글 데이터(title, contents 등)와 부모 글 번호(boardNum)를 담고 있습니다.
	 * @return 처리 후 목록 페이지로 리다이렉트합니다.
	 */
	@PostMapping("reply")
	public String reply(QnaVO qnaVO)throws Exception{
		
		int result = qnaService.reply(qnaVO);
		
		return "redirect:./list"; // F5(새로고침) 시 폼이 다시 제출되는 것을 방지하기 위해 redirect 사용
	}
	
	/**
	 * 새 글 작성 폼 페이지를 요청합니다. (GET /qna/add)
	 * @return 렌더링할 JSP 파일의 경로 ("board/add")
	 */
	@GetMapping("add")
	public String insert()throws Exception{
		return "board/add";
	}
	
	/**
	 * 새 글 데이터를 DB에 저장 처리합니다. (POST /qna/add)
	 * @param qnaVO 폼에서 입력된 새 글 데이터(title, writer, contents)를 담고 있습니다.
	 * @param attaches 사용자가 업로드한 파일들을 담고 있는 배열입니다.
	 * @return 처리 후 목록 페이지로 리다이렉트합니다.
	 */
	@PostMapping("add")
	public String insert(QnaVO qnaVO, MultipartFile [] attaches)throws Exception{
		
		int result = qnaService.insert(qnaVO, attaches);
		return "redirect:./list";
	}

	/**
	 * 기존 글 수정 폼 페이지를 요청합니다. (GET /qna/update)
	 * @param boardVO 수정할 글의 번호(boardNum)를 담고 있습니다.
	 * @param model View로 데이터를 전달하기 위한 객체입니다.
	 * @return 렌더링할 JSP 파일의 경로 ("board/add")
	 */
	@GetMapping("update")
	public String update(BoardVO boardVO, Model model)throws Exception{
		// DB에서 수정할 글의 기존 정보를 가져옵니다.
		BoardVO vo = qnaService.detail(boardVO);
		model.addAttribute("vo", vo); // 가져온 정보를 'vo'라는 이름으로 JSP에 전달하여 폼에 값을 채웁니다.
		
		return "board/add"; // 글 작성/수정 폼은 하나의 JSP를 공유
	}
	
	/**
	 * 수정된 글 데이터를 DB에 업데이트 처리합니다. (POST /qna/update)
	 * @param noticeVO 폼에서 수정된 글 데이터(boardNum, title, contents)를 담고 있습니다.
	 * @param attaches 새로 추가되거나 변경된 파일들을 담고 있습니다.
	 * @param model View로 결과 메시지와 이동할 URL을 전달하기 위한 객체입니다.
	 * @return 처리 결과를 보여주는 공통 JSP 페이지 경로 ("commons/result")
	 */
	@PostMapping("update")
	public String update(NoticeVO noticeVO, MultipartFile [] attaches, Model model)throws Exception{
		int result = qnaService.update(noticeVO, attaches);
		
		String msg = "수정 실패";
		if(result > 0) {
			msg = "수정 성공";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", "./detail?boardNum=" + noticeVO.getBoardNum());
		
		return "commons/result";
	}
	
	/**
	 * 게시글을 삭제 처리합니다. (POST /qna/delete)
	 * @param noticeVO 삭제할 글의 번호(boardNum)를 담고 있습니다.
	 * @param model View로 결과 메시지와 이동할 URL을 전달하기 위한 객체입니다.
	 * @return 처리 결과를 보여주는 공통 JSP 페이지 경로 ("commons/result")
	 */
	@PostMapping("delete")
	public String delete(NoticeVO noticeVO, Model model)throws Exception{
		int result = qnaService.delete(noticeVO);
		
		String msg = "삭제 실패";
		if(result > 0) {
			msg = "삭제 성공";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", "./list");
		
		return "commons/result";
	}
	
	
	/**
	 * 첨부파일 한 개를 삭제합니다. (AJAX 요청 처리) (POST /qna/fileDelete)
	 * @ResponseBody: 이 어노테이션이 붙으면 메서드의 반환값이 View(JSP)를 찾는 데 사용되지 않고, HTTP 응답 본문에 직접 쓰여집니다.
	 * AJAX 요청에 대한 응답으로 주로 JSON, XML 또는 단순 텍스트/숫자를 반환할 때 사용됩니다.
	 * @param boardFileVO 삭제할 파일의 번호(fileNum)를 담고 있습니다.
	 * @return 삭제 성공 여부를 나타내는 숫자 (1: 성공, 0: 실패)를 클라이언트(JavaScript)에게 반환합니다.
	 */
	@PostMapping("fileDelete")
	@ResponseBody
	public int fileDelete(BoardFileVO boardFileVO, Model model)throws Exception{
		int result = qnaService.fileDelete(boardFileVO);
		return result;
	}
	
	/**
	 * 첨부파일 다운로드를 처리합니다. (GET /qna/fileDown)
	 * @param boardFileVO 다운로드할 파일의 번호(fileNum)를 담고 있습니다.
	 * @param model View로 파일 정보를 전달하기 위한 객체입니다.
	 * @return "fileDownView" 라는 이름의 Custom View 객체를 반환합니다. 이 View는 실제 파일 다운로드 로직을 처리합니다.
	 */
	@GetMapping("fileDown")
	public String fileDown(BoardFileVO boardFileVO, Model model)throws Exception{
		// DB에서 파일의 상세 정보(실제 저장된 이름 등)를 조회합니다.
		boardFileVO = qnaService.fileDetail(boardFileVO);
		
		model.addAttribute("vo", boardFileVO); // 파일 정보를 'vo'라는 이름으로 FileDownView에 전달
		
		return "fileDownView";
	}
}