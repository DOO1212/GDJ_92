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

// @Controller : 이 클래스가 Spring의 컨트롤러 역할을 한다는 것을 선언합니다.
// @RequestMapping("/qna/*") : '/qna/'로 시작하는 모든 URL 요청을 이 컨트롤러가 처리하도록 지정합니다.
// @Slf4j : Lombok 어노테이션으로, 로그를 기록할 수 있는 'log' 객체를 자동으로 생성해줍니다.
@Controller
@RequestMapping("/qna/*")
@Slf4j
public class QnaController {
	
	@Autowired
	private QnaService qnaService;
	
	// @Value("${board.qna}") : application.properties 파일에 정의된 'board.qna' 속성 값을 찾아 변수 'name'에 주입합니다.
	@Value("${board.qna}")
	private String name;
	
	// @ModelAttribute("board") : 이 컨트롤러 내의 모든 메서드가 실행되기 전에 먼저 실행됩니다.
	// 여기서 반환된 값("QnA")은 "board"라는 이름으로 Model에 자동으로 담겨 모든 JSP에서 사용할 수 있게 됩니다.
	@ModelAttribute("board")
	public String getBoard() {
		return name;
	}
	
	// GET 방식으로 /qna/list 주소를 요청했을 때 실행됩니다.
	@GetMapping("list")
	public String list(Pager pager, Model model)throws Exception{
		
		// Pager 객체와 게시글 목록을 Model에 담아 JSP로 전달합니다.
		model.addAttribute("pager", pager);
		model.addAttribute("list", qnaService.list(pager));
		
		// "board/list.jsp" 파일을 찾아 사용자에게 보여줍니다.
		return "board/list";
	}
	
	// GET 방식으로 /qna/detail 주소를 요청했을 때 실행됩니다.
	@GetMapping("detail")
	public String detail(QnaVO qnaVO, Model model)throws Exception{
		
		// 서비스 계층에 게시글 번호를 전달하여 상세 정보를 조회하고, 그 결과를 Model에 담습니다.
		model.addAttribute("vo", qnaService.detail(qnaVO));
		// "board/detail.jsp" 파일을 찾아 사용자에게 보여줍니다.
		return "board/detail";
	}
	
	// GET 방식으로 /qna/reply 주소를 요청했을 때 실행됩니다. (답글 폼 페이지)
	@GetMapping("reply")
	public String reply(QnaVO qnaVO, Model model)throws Exception{
		// 부모 글의 정보를 "vo"라는 이름으로 Model에 담아 JSP에 전달합니다.
		model.addAttribute("vo", qnaVO);
		
		// 글 작성/수정/답글 폼으로 사용되는 "board/add.jsp" 파일을 보여줍니다.
		return "board/add";
	}
	
	// POST 방식으로 /qna/reply 주소로 폼 데이터가 전송되었을 때 실행됩니다.
	@PostMapping("reply")
	public String reply(QnaVO qnaVO)throws Exception{
		
		// 서비스 계층에 답글 정보를 전달하여 DB에 저장하도록 요청합니다.
		int result = qnaService.reply(qnaVO);
		
		// 처리가 완료되면 목록 페이지로 리다이렉트(재이동)합니다.
		return "redirect:./list";
	}
	
	// GET 방식으로 /qna/add 주소를 요청했을 때 실행됩니다. (새 글 작성 폼 페이지)
	@GetMapping("add")
	public String insert()throws Exception{
		// "board/add.jsp" 파일을 보여줍니다.
		return "board/add";
	}
	
	// POST 방식으로 /qna/add 주소로 폼 데이터가 전송되었을 때 실행됩니다.
	@PostMapping("add")
	public String insert(QnaVO qnaVO, MultipartFile [] attaches)throws Exception{
		
		// 서비스 계층에 새 글 정보와 첨부파일들을 전달하여 DB에 저장하도록 요청합니다.
		int result = qnaService.insert(qnaVO, attaches);
		// 처리가 완료되면 목록 페이지로 리다이렉트합니다.
		return "redirect:./list";
	}

	// GET 방식으로 /qna/update 주소를 요청했을 때 실행됩니다. (수정 폼 페이지)
	@GetMapping("update")
	public String update(BoardVO noticeVO, Model model)throws Exception{
		// 수정할 게시글의 상세 정보를 DB에서 가져옵니다.
		BoardVO boardVO = qnaService.detail(noticeVO);
		// 가져온 정보를 "vo"라는 이름으로 Model에 담아 JSP의 폼에 채워줍니다.
		model.addAttribute("vo", boardVO);
		
		// "board/add.jsp" 파일을 수정 폼으로 재사용합니다.
		return "board/add";
	}
	
	// POST 방식으로 /qna/update 주소로 폼 데이터가 전송되었을 때 실행됩니다.
	@PostMapping("update")
	public String update(NoticeVO noticeVO,MultipartFile [] attaches, Model model)throws Exception{
		// 서비스 계층에 수정된 내용과 파일들을 전달하여 DB를 업데이트합니다.
		int result = qnaService.update(noticeVO, attaches);
		
		// 처리 결과에 따라 사용자에게 보여줄 메시지를 설정합니다.
		String msg = "수정 실패";
		if(result>0) {
			msg="수정 성공";
		}
		
		// 메시지와 함께 이동할 URL을 Model에 담습니다.
		String url="./detail?boardNum="+noticeVO.getBoardNum();
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		
		// "commons/result.jsp" 페이지를 통해 사용자에게 처리 결과를 보여줍니다.
		return "commons/result";
	}
	
	// POST 방식으로 /qna/delete 주소로 요청이 왔을 때 실행됩니다.
	@PostMapping("delete")
	public String delete(NoticeVO noticeVO, Model model)throws Exception{
		// 서비스 계층에 게시글 번호를 전달하여 삭제를 요청합니다.
		int result = qnaService.delete(noticeVO);
		
		// 처리 결과에 따라 메시지를 설정합니다.
		String msg = "삭제 실패";
		if(result>0) {
			msg="삭제 성공";
		}
		
		// 메시지와 함께 이동할 URL(목록)을 Model에 담습니다.
		String url="./list";
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		
		// "commons/result.jsp" 페이지를 통해 결과를 보여줍니다.
		return "commons/result";
	}
	
	
	// AJAX 요청으로, POST 방식으로 /qna/fileDelete 주소를 요청했을 때 실행됩니다.
	// @ResponseBody : 이 메서드의 반환값은 JSP 페이지가 아니라, 데이터(여기서는 숫자) 그 자체임을 나타냅니다.
	@PostMapping("fileDelete")
	@ResponseBody
	public int fileDelete(BoardFileVO boardFileVO, Model model)throws Exception{
		// 서비스 계층에 파일 번호를 전달하여 파일 삭제를 요청합니다.
		int result = qnaService.fileDelete(boardFileVO);
		
		// 삭제 성공 여부(1 또는 0)를 요청한 클라이언트(JavaScript)에게 반환합니다.
		return result;
	}
	
	// GET 방식으로 /qna/fileDown 주소를 요청했을 때 실행됩니다.
	@GetMapping("fileDown")
	public String fileDown(BoardFileVO boardFileVO, Model model)throws Exception{
		// DB에서 다운로드할 파일의 상세 정보를 가져옵니다.
		boardFileVO = qnaService.fileDetail(boardFileVO);
		
		// 파일 정보를 "vo"라는 이름으로 Model에 담아 View로 전달합니다.
		model.addAttribute("vo", boardFileVO);
		
		// "fileDownView"라는 이름의 커스텀 View를 찾아 파일 다운로드를 처리하도록 합니다.
		return "fileDownView";
	}
}