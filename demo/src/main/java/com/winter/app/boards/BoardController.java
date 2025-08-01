package com.winter.app.boards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/board/*")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	// /board/list =>
	// /board/detail =>
	
	@RequestMapping(value = "list" , method=RequestMethod.GET)
	public String list() {
		System.out.println("list");
		boardService.list();
		return "board/list";
	}
	
	@RequestMapping(value = "detail" , method=RequestMethod.GET)
	public void detail(BoardVO boardVO) {
//		String num = request.getParameter("num");
//		int n = Integer.parseInt(num);
		
//		BoardVO boardVO = new BoardVO();
//		boardVO.setName(kind);
//		boardVO.setNum(num);
		
		System.out.println("detail: + " + boardVO);
	}

}
