package com.winter.app.board.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.winter.app.board.BoardVO;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="/notice/*")
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
	@GetMapping("list")
	public String list(Model model)throws Exception{

		List<BoardVO> list = noticeService.list();
		
		model.addAttribute("list", list);
		
		return "notice/list";
	}
	
	@GetMapping("detail")
	public String detail(NoticeVO noticeVO, Model model)throws Exception{
//		String n=request.getParameter("boardNum");
//		int num = Integer.parseInt(n);
//		NoticeVO noticeVO = new NoticeVO();
//		noticeVO.setBoardNum(num);
		
		BoardVO boardVO = noticeService.detail(noticeVO);
		
		model.addAttribute("vo", boardVO);
		
		return "notice/detail";
	}
	
	@GetMapping("add")
	public String insert()throws Exception{
		
		return "notice/add";
	}
	
	@PostMapping("add")
	public String insert(NoticeVO noticeVO)throws Exception{
		int result = noticeService.insert(noticeVO);
		return "redirect:./list";
	}
	
	@GetMapping("update")
	public String update(BoardVO noticeVO, Model model)throws Exception{
		BoardVO boardVO = noticeService.detail(noticeVO);
		model.addAttribute("vo", boardVO);
		
		return "notice/add";
	}
	
	@PostMapping("update")
	public String update(NoticeVO noticeVO, Model model)throws Exception{
		int result = noticeService.update(noticeVO);
		
		String msg = "수정 실패";
		
		if(result>0) {
			msg="수정 성공";
		}
		
		String url="./detail?boardNum="+noticeVO.getBoardNum();
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		
		return "commons/result";//"redirect:./detail?boardNum="+noticeVO.getBoardNum();
	}
	
	@PostMapping("delete")
	public String delete(BoardVO noticeVO, Model model) throws Exception {
		int result = noticeService.delete(noticeVO);
		
		String msg = "삭제 실패";
		
		if (result>0) {
			msg ="삭제 성공";
		}
		
		return "notice/list";
	}
	
	
}
