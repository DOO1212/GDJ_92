package com.winter.app.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.winter.app.members.MemberVO; // MemberVO를 사용하기 위해 import

import jakarta.servlet.http.HttpSession; // HttpSession을 사용하기 위해 import

@Controller
@RequestMapping("/notice/*")
public class NoticeController {

	// @Autowired: Spring이 관리하는 NoticeService 타입의 Bean을 자동으로 주입합니다.
	@Autowired
	private NoticeService noticeService;
	
	/**
	 * 글쓰기 폼(add.jsp) 페이지로 이동시키는 메서드
	 * @return "board/add" (View의 이름)
	 */
	@GetMapping("add")
	public String add() throws Exception {
		// "board/add"라는 문자열을 반환하면, Spring은 이를 View의 이름으로 해석하고
		// servlet-context.xml(또는 application.properties)의 설정에 따라
		// 최종적으로 /WEB-INF/views/board/add.jsp 파일을 찾아 사용자에게 보여줍니다.
		return "board/add";
	}
	
	/**
	 * 사용자가 작성한 글과 첨부파일을 받아 DB에 저장하는 메서드
	 * @param boardVO 사용자가 입력한 제목, 내용 등이 자동으로 담기는 객체
	 * @param attaches 사용자가 첨부한 파일들이 자동으로 담기는 배열 객체
	 * @param session 현재 로그인한 사용자의 정보를 얻기 위한 세션 객체
	 * @return "redirect:./list" (처리가 끝난 후 목록 페이지로 이동)
	 */
	@PostMapping("add")
	public String add(BoardVO boardVO, MultipartFile [] attaches, HttpSession session) throws Exception {
		// 1. 세션(HttpSession)에서 현재 로그인한 사용자의 정보(MemberVO)를 꺼냅니다.
		MemberVO memberVO = (MemberVO)session.getAttribute("member");
		
		// 2. 꺼낸 사용자 정보에서 ID(username)를 가져와 BoardVO의 작성자(boardWriter)로 설정합니다.
		boardVO.setBoardWriter(memberVO.getUsername());
		
		// 3. Service에 게시글 정보(boardVO)와 파일 정보(attaches)를 넘겨주어 저장을 요청합니다.
		int result = noticeService.insert(boardVO, attaches);
		
		// 4. 저장이 완료되면, "redirect:" 접두사를 사용하여 /notice/list URL로 재요청(redirect)을 보냅니다.
		//    이를 통해 사용자의 브라우저 주소창은 자연스럽게 목록 페이지로 바뀌고, 새로고침 시 데이터가 중복 등록되는 것을 방지합니다.
		return "redirect:./list";
	}
	
	// ... 목록, 상세보기 등 다른 메서드들 ...
}