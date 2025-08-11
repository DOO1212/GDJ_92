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

@Controller
@RequestMapping("/qna/*")
@Slf4j
public class QnaController {
	
	@Autowired
	private QnaService qnaService;
	
	@Value("${board.qna}")
	private String name;
	
	@ModelAttribute("board")
	public String getBoard() {
		return name;
	}
	
	@GetMapping("list")
	public String list(Pager pager, Model model)throws Exception{
		
		model.addAttribute("pager", pager);
		model.addAttribute("list", qnaService.list(pager));
		
		return "board/list";
	}
	
	@GetMapping("detail")
	public String detail(QnaVO qnaVO, Model model)throws Exception{
		
		model.addAttribute("vo", qnaService.detail(qnaVO));
		return "board/detail";
	}
	
	@GetMapping("reply")
	public String reply(QnaVO qnaVO, Model model)throws Exception{
		model.addAttribute("vo", qnaVO);
		
		return "board/add";
	}
	
	@PostMapping("reply")
	public String reply(QnaVO qnaVO)throws Exception{
		
		int result = qnaService.reply(qnaVO);
		
		return "redirect:./list";
	}
	
	@GetMapping("add")
	public String insert()throws Exception{
		return "board/add";
	}
	
	@PostMapping("add")
	public String insert(QnaVO qnaVO, MultipartFile [] attaches)throws Exception{
		
		int result = qnaService.insert(qnaVO, attaches);
		return "redirect:./list";
	}
	@GetMapping("update")
	public String update(BoardVO noticeVO, Model model)throws Exception{
		BoardVO boardVO = qnaService.detail(noticeVO);
		model.addAttribute("vo", boardVO);
		
		return "board/add";
	}
	
	@PostMapping("update")
	public String update(NoticeVO noticeVO,MultipartFile [] attaches, Model model)throws Exception{
		int result = qnaService.update(noticeVO, attaches);
		
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
	public String delete(NoticeVO noticeVO, Model model)throws Exception{
		int result = qnaService.delete(noticeVO);
		String msg = "삭제 실패";
		
		if(result>0) {
			msg="삭제 성공";
		}
		
		String url="./list";
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		
		return "commons/result";
		
	}
	
	
	@PostMapping("fileDelete")
	@ResponseBody
	public int fileDelete(BoardFileVO boardFileVO, Model model)throws Exception{
		int result = qnaService.fileDelete(boardFileVO);
		
		return result;
	}
	
	@GetMapping("fileDown")
	public String fileDown(BoardFileVO boardFileVO, Model model)throws Exception{
		boardFileVO = qnaService.fileDetail(boardFileVO);
		
		model.addAttribute("vo", boardFileVO);
		
		return "fileDownView";
	}

}
