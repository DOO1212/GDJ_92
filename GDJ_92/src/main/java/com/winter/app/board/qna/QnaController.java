package com.winter.app.board.qna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/qna/*")
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
	public String list(Model model)throws Exception{
		
		model.addAttribute("list", qnaService.list());
		
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
	public String insert(QnaVO qnaVO)throws Exception{
		int result = qnaService.insert(qnaVO);
		return "redirect:./list";
	}

}
