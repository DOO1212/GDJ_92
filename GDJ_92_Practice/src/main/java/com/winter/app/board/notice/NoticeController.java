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

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @Controller
 * 이 클래스가 Spring의 Controller 역할을 하도록 지정합니다.
 *
 * @RequestMapping("/notice/*")
 * /notice/ 로 시작하는 모든 URL 요청을 이 컨트롤러가 담당하도록 매핑합니다.
 *
 * @Slf4j
 * Lombok 라이브러리를 통해 로그 객체(log)를 자동으로 생성해 줍니다.
 */
@Controller
@RequestMapping(value="/notice/*")
@Slf4j
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
	/**
	 * @Value("${board.notice}")
	 * application.properties 파일의 'board.notice' 속성 값("Notice")을 이 변수(name)에 주입합니다.
	 */
	@Value("${board.notice}")
	private String name;
	
	/**
	 * @ModelAttribute("board")
	 * 이 컨트롤러의 모든 요청 처리 메서드보다 먼저 실행됩니다.
	 * 반환된 값("Notice")은 "board"라는 이름으로 Model에 자동 추가되어, 모든 View에서 ${board}로 사용할 수 있습니다.
	 */
	@ModelAttribute("board")
	public String getBoard() {
		return name;
	}
	
	/**
	 * 공지사항 목록 페이지를 요청합니다. (GET /notice/list)
	 * @param pager 페이징 및 검색 정보를 담고 있는 객체입니다.
	 * @param model View에 데이터를 전달하는 객체입니다.
	 * @return 렌더링할 JSP 파일 경로 ("board/list")
	 */
	@GetMapping("list")
	public String list(@ModelAttribute Pager pager, Model model)throws Exception{
		List<BoardVO> list = noticeService.list(pager);
		model.addAttribute("list", list);
		
		return "board/list";
	}
	
	/**
	 * 공지사항 상세 페이지를 요청합니다. (GET /notice/detail)
	 * @param noticeVO 상세 조회할 글 번호(boardNum)를 담고 있습니다. Spring이 요청 파라미터를 보고 자동으로 객체를 생성하고 값을 넣어줍니다.
	 * @param model View에 데이터를 전달하는 객체입니다.
	 * @return 렌더링할 JSP 파일 경로 ("board/detail")
	 */
	@GetMapping("detail")
	public String detail(NoticeVO noticeVO, Model model)throws Exception{
		BoardVO boardVO = noticeService.detail(noticeVO);
		model.addAttribute("vo", boardVO);
		
		return "board/detail";
	}
	
	/**
	 * 새 공지사항 작성 폼 페이지를 요청합니다. (GET /notice/add)
	 * @return 렌더링할 JSP 파일 경로 ("board/add")
	 */
	@GetMapping("add")
	public String insert()throws Exception{
		return "board/add";
	}
	
	/**
	 * 새 공지사항 데이터를 DB에 저장 처리합니다. (POST /notice/add)
	 * @param noticeVO 폼에서 입력된 새 글 데이터를 담고 있습니다.
	 * @param attaches 사용자가 업로드한 파일들을 담고 있는 배열입니다.
	 * @return 처리 후 목록 페이지로 리다이렉트합니다.
	 */
	@PostMapping("add")
	public String insert(NoticeVO noticeVO, MultipartFile [] attaches)throws Exception{
		int result = noticeService.insert(noticeVO, attaches);
		return "redirect:./list";
	}
	
	/**
	 * 기존 공지사항 수정 폼 페이지를 요청합니다. (GET /notice/update)
	 * @param noticeVO 수정할 글의 번호(boardNum)를 담고 있습니다.
	 * @param model View에 데이터를 전달하는 객체입니다.
	 * @return 렌더링할 JSP 파일 경로 ("board/add")
	 */
	@GetMapping("update")
	public String update(BoardVO noticeVO, Model model)throws Exception{
		// DB에서 수정할 글의 기존 정보를 가져와서 'vo'라는 이름으로 Model에 담습니다.
		BoardVO boardVO = noticeService.detail(noticeVO);
		model.addAttribute("vo", boardVO);
		
		return "board/add";
	}
	
	/**
	 * 수정된 공지사항 데이터를 DB에 업데이트 처리합니다. (POST /notice/update)
	 * @param noticeVO 폼에서 수정된 글 데이터를 담고 있습니다.
	 * @param attaches 새로 추가되거나 변경된 파일들을 담고 있습니다.
	 * @param model View로 결과 메시지와 이동할 URL을 전달하기 위한 객체입니다.
	 * @return 처리 결과를 보여주는 공통 JSP 페이지 경로 ("commons/result")
	 */
	@PostMapping("update")
	public String update(NoticeVO noticeVO,MultipartFile [] attaches, Model model)throws Exception{
		int result = noticeService.update(noticeVO, attaches);
		
		String msg = "수정 실패";
		if(result>0) {
			msg="수정 성공";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", "./detail?boardNum="+noticeVO.getBoardNum());
		
		return "commons/result";
	}
	
	/**
	 * 공지사항을 삭제 처리합니다. (POST /notice/delete)
	 * @param noticeVO 삭제할 글의 번호(boardNum)를 담고 있습니다.
	 * @param model View로 결과 메시지와 이동할 URL을 전달하기 위한 객체입니다.
	 * @return 처리 결과를 보여주는 공통 JSP 페이지 경로 ("commons/result")
	 */
	@PostMapping("delete")
	public String delete(NoticeVO noticeVO, Model model)throws Exception{
		int result = noticeService.delete(noticeVO);

		String msg = "삭제 실패";
		if(result>0) {
			msg="삭제 성공";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", "./list");
		
		return "commons/result";
	}
	
	/**
	 * 첨부파일 한 개를 삭제합니다. (AJAX 요청 처리) (POST /notice/fileDelete)
	 * @ResponseBody: 이 메서드의 반환값은 View(JSP)가 아닌, 응답 본문에 직접 쓰여집니다. (AJAX 통신용)
	 * @param boardFileVO 삭제할 파일의 번호(fileNum)를 담고 있습니다.
	 * @return 삭제 성공 여부를 나타내는 숫자 (1: 성공, 0: 실패)를 클라이언트에게 반환합니다.
	 */
	@PostMapping("fileDelete")
	@ResponseBody
	public int fileDelete(BoardFileVO boardFileVO, Model model)throws Exception{
		int result = noticeService.fileDelete(boardFileVO);
		return result;
	}
	
	/**
	 * 첨부파일 다운로드를 처리합니다. (GET /notice/fileDown)
	 * @param boardFileVO 다운로드할 파일의 번호(fileNum)를 담고 있습니다.
	 * @param model View로 파일 정보를 전달하기 위한 객체입니다.
	 * @return "fileDownView" 라는 이름의 Custom View 객체를 반환하여 파일 다운로드를 처리합니다.
	 */
	@GetMapping("fileDown")
	public String fileDown(BoardFileVO boardFileVO, Model model)throws Exception{
		boardFileVO = noticeService.fileDetail(boardFileVO);
		model.addAttribute("vo", boardFileVO);
		return "fileDownView";
	}

	/**
	 * Summernote와 같은 에디터에서 이미지 파일을 업로드할 때 사용됩니다. (AJAX 요청 처리) (POST /notice/boardFile)
	 * @param bf 업로드된 이미지 파일 1개
	 * @return @ResponseBody에 의해, 저장된 이미지의 웹 경로(URL)를 문자열로 클라이언트에게 반환합니다.
	 */
	@PostMapping("boardFile")
	@ResponseBody
	public String boardFile(MultipartFile bf)throws Exception{
		log.info("Uploaded File: {}", bf.getOriginalFilename());
		return noticeService.boardFile(bf);
	}

	/**
	 * Summernote와 같은 에디터에서 업로드된 이미지 파일을 삭제할 때 사용됩니다. (AJAX 요청 처리) (POST /notice/boardFileDelete)
	 * @param fileName 삭제할 파일의 이름
	 * @return @ResponseBody에 의해, 삭제 성공 여부를 boolean(true/false) 값으로 클라이언트에게 반환합니다.
	 */
	@PostMapping("boardFileDelete")
	@ResponseBody
	public boolean boardFileDelete(String fileName)throws Exception{
		return noticeService.boardFileDelete(fileName);
	}
}